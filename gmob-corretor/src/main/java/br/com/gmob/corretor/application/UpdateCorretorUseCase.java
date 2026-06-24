package br.com.gmob.corretor.application;

import br.com.gmob.corretor.api.dto.CorretorResponse;
import br.com.gmob.corretor.api.dto.UpdateCorretorRequest;
import br.com.gmob.corretor.api.mapper.CorretorMapper;
import br.com.gmob.corretor.domain.model.Corretor;
import br.com.gmob.corretor.domain.port.CorretorRepositoryPort;
import br.com.gmob.infra.domain.enums.Perfil;
import br.com.gmob.infra.exception.ConflictException;
import br.com.gmob.infra.exception.ForbiddenException;
import br.com.gmob.infra.exception.ResourceNotFoundException;
import br.com.gmob.infra.security.AuthenticatedUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class UpdateCorretorUseCase {

    private final CorretorRepositoryPort corretorRepository;
    private final PasswordEncoder passwordEncoder;

    public UpdateCorretorUseCase(CorretorRepositoryPort corretorRepository, PasswordEncoder passwordEncoder) {
        this.corretorRepository = corretorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public CorretorResponse execute(Long id, UpdateCorretorRequest request, AuthenticatedUser currentUser) {
        if (currentUser.perfil() != Perfil.ADMINISTRADOR && !currentUser.corretorId().equals(id)) {
            throw new ForbiddenException("Você só pode atualizar seu próprio perfil");
        }

        Corretor existingCorretor = corretorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Corretor não encontrado"));

        validateEmailUniqueness(request.email(), existingCorretor.email());
        validateCpfUniqueness(request.cpf(), existingCorretor.cpf());

        String nomeCompleto = StringUtils.hasText(request.nomeCompleto())
                ? request.nomeCompleto()
                : existingCorretor.nomeCompleto();
        String email = StringUtils.hasText(request.email())
                ? request.email()
                : existingCorretor.email();
        String telefone = StringUtils.hasText(request.telefone())
                ? request.telefone()
                : existingCorretor.telefone();
        String cpf = StringUtils.hasText(request.cpf())
                ? request.cpf()
                : existingCorretor.cpf();
        Perfil perfil = request.perfil() != null
                ? request.perfil()
                : existingCorretor.perfil();

        Optional<String> senha = Optional.empty();
        if (StringUtils.hasText(request.senha())) {
            senha = Optional.of(passwordEncoder.encode(request.senha()));
        }

        Corretor updated = corretorRepository.save(new Corretor(
                existingCorretor.id(),
                nomeCompleto,
                email,
                telefone,
                cpf,
                senha,
                perfil,
                existingCorretor.dataCadastro()
        ));

        return CorretorMapper.toResponse(updated);
    }

    private void validateEmailUniqueness(String email, String currentEmail) {
        if (StringUtils.hasText(email) && !email.equals(currentEmail) && corretorRepository.existsByEmail(email)) {
            throw new ConflictException("Email já está em uso");
        }
    }

    private void validateCpfUniqueness(String cpf, String currentCpf) {
        if (StringUtils.hasText(cpf) && !cpf.equals(currentCpf) && corretorRepository.existsByCpf(cpf)) {
            throw new ConflictException("CPF já está em uso");
        }
    }
}
