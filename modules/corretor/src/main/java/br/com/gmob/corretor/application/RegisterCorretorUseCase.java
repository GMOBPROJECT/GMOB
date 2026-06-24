package br.com.gmob.corretor.application;

import br.com.gmob.corretor.api.dto.CreateCorretorRequest;
import br.com.gmob.corretor.api.dto.CorretorResponse;
import br.com.gmob.corretor.api.mapper.CorretorMapper;
import br.com.gmob.corretor.domain.model.Corretor;
import br.com.gmob.corretor.domain.port.CorretorRepositoryPort;
import br.com.gmob.infra.domain.enums.Perfil;
import br.com.gmob.infra.exception.ConflictException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class RegisterCorretorUseCase {

    private final CorretorRepositoryPort corretorRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterCorretorUseCase(CorretorRepositoryPort corretorRepository, PasswordEncoder passwordEncoder) {
        this.corretorRepository = corretorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public RegisterCorretorResult execute(CreateCorretorRequest request) {
        if (corretorRepository.existsByEmail(request.email())) {
            throw new ConflictException("Email já está em uso");
        }
        if (corretorRepository.existsByCpf(request.cpf())) {
            throw new ConflictException("CPF já está em uso");
        }

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

        return new RegisterCorretorResult(
                "Corretor registrado com sucesso",
                CorretorMapper.toResponse(corretor)
        );
    }

    public record RegisterCorretorResult(String message, CorretorResponse corretor) {
    }
}
