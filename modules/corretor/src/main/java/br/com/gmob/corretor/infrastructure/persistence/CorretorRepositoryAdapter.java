package br.com.gmob.corretor.infrastructure.persistence;

import br.com.gmob.corretor.application.port.CorretorAuthPort;
import br.com.gmob.corretor.domain.model.Corretor;
import br.com.gmob.corretor.domain.port.CorretorRepositoryPort;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CorretorRepositoryAdapter implements CorretorRepositoryPort, CorretorAuthPort {

    private final SpringDataCorretorRepository repository;

    public CorretorRepositoryAdapter(SpringDataCorretorRepository repository) {
        this.repository = repository;
    }

    @Override
    public Corretor save(Corretor corretor) {
        CorretorJpaEntity entity = toEntity(corretor);
        if (corretor.id() != null && corretor.senha().isEmpty()) {
            repository.findById(corretor.id())
                    .ifPresent(existing -> entity.setSenha(existing.getSenha()));
        }
        return toDomainWithoutSenha(repository.save(entity));
    }

    @Override
    public Optional<Corretor> findById(Long id) {
        return repository.findById(id).map(this::toDomainWithoutSenha);
    }

    @Override
    public Optional<Corretor> findByEmail(String email) {
        return repository.findByEmail(email).map(this::toDomainWithoutSenha);
    }

    @Override
    public Optional<Corretor> findByCpf(String cpf) {
        return repository.findByCpf(cpf).map(this::toDomainWithoutSenha);
    }

    @Override
    public List<Corretor> findAll(int page, int limit) {
        return repository.findAllByOrderByDataCadastroDesc(PageRequest.of(page - 1, limit))
                .stream()
                .map(this::toDomainWithoutSenha)
                .toList();
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public boolean existsByCpf(String cpf) {
        return repository.existsByCpf(cpf);
    }

    @Override
    public Optional<Corretor> findByEmailWithPassword(String email) {
        return repository.findByEmail(email).map(this::toDomain);
    }

    @Override
    public Optional<Corretor> findByIdForAuth(Long id) {
        return repository.findById(id).map(this::toDomainWithoutSenha);
    }

    private Corretor toDomain(CorretorJpaEntity entity) {
        return new Corretor(
                entity.getCorretorId(),
                entity.getNomeCompleto(),
                entity.getEmail(),
                entity.getTelefone(),
                entity.getCpf(),
                Optional.ofNullable(entity.getSenha()),
                entity.getPerfil(),
                entity.getDataCadastro()
        );
    }

    private Corretor toDomainWithoutSenha(CorretorJpaEntity entity) {
        return toDomain(entity).withoutSenha();
    }

    private CorretorJpaEntity toEntity(Corretor corretor) {
        CorretorJpaEntity entity = new CorretorJpaEntity();
        entity.setCorretorId(corretor.id());
        entity.setNomeCompleto(corretor.nomeCompleto());
        entity.setEmail(corretor.email());
        entity.setTelefone(corretor.telefone());
        entity.setCpf(corretor.cpf());
        corretor.senha().ifPresent(entity::setSenha);
        entity.setPerfil(corretor.perfil() != null ? corretor.perfil() : br.com.gmob.infra.domain.enums.Perfil.CORRETOR);
        entity.setDataCadastro(corretor.dataCadastro());
        return entity;
    }
}
