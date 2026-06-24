package br.com.gmob.corretor.application;

import br.com.gmob.corretor.api.dto.CreateCorretorRequest;
import br.com.gmob.corretor.api.dto.CorretorResponse;
import br.com.gmob.corretor.api.mapper.CorretorMapper;
import br.com.gmob.corretor.domain.model.Corretor;
import br.com.gmob.corretor.domain.port.CorretorRepositoryPort;
import br.com.gmob.infra.domain.enums.Perfil;
import br.com.gmob.infra.exception.ConflictException;
import br.com.gmob.infra.exception.ForbiddenException;
import br.com.gmob.infra.security.AuthenticatedUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class CreateCorretorUseCase {

    private final CorretorRepositoryPort corretorRepository;
    private final PasswordEncoder passwordEncoder;

    public CreateCorretorUseCase(CorretorRepositoryPort corretorRepository, PasswordEncoder passwordEncoder) {
        this.corretorRepository = corretorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public CorretorResponse execute(CreateCorretorRequest request, AuthenticatedUser currentUser) {
        if (currentUser.perfil() != Perfil.ADMINISTRADOR) {
            throw new ForbiddenException("Apenas administradores podem criar corretores");
        }

        validateUniqueness(request.email(), request.cpf());

        String hashedPassword = passwordEncoder.encode(request.senha());
        Perfil perfil = request.perfil() != null ? request.perfil() : Perfil.CORRETOR;

        Corretor corretor = corretorRepository.save(new Corretor(
                null,
                request.nomeCompleto(),
                request.email(),
                request.telefone(),
                request.cpf(),
                Optional.of(hashedPassword),
                perfil,
                Instant.now()
        ));

        return CorretorMapper.toResponse(corretor);
    }

    private void validateUniqueness(String email, String cpf) {
        if (corretorRepository.existsByEmail(email)) {
            throw new ConflictException("Email já está em uso");
        }
        if (corretorRepository.existsByCpf(cpf)) {
            throw new ConflictException("CPF já está em uso");
        }
    }
}
