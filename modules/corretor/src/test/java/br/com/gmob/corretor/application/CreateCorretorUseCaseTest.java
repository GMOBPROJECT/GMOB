package br.com.gmob.corretor.application;

import br.com.gmob.corretor.api.dto.CreateCorretorRequest;
import br.com.gmob.corretor.domain.model.Corretor;
import br.com.gmob.corretor.domain.port.CorretorRepositoryPort;
import br.com.gmob.infra.domain.enums.Perfil;
import br.com.gmob.infra.exception.ForbiddenException;
import br.com.gmob.infra.security.AuthenticatedUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateCorretorUseCaseTest {

    @Mock
    private CorretorRepositoryPort corretorRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private CreateCorretorUseCase useCase;

    private final AuthenticatedUser admin = new AuthenticatedUser(
            1L, "Admin", "admin@test.com", "(82) 99999-9999", "111.111.111-11",
            Perfil.ADMINISTRADOR, Instant.now()
    );

    private final AuthenticatedUser corretor = new AuthenticatedUser(
            2L, "Corretor", "corretor@test.com", "(82) 98888-8888", "222.222.222-22",
            Perfil.CORRETOR, Instant.now()
    );

    @BeforeEach
    void setUp() {
        useCase = new CreateCorretorUseCase(corretorRepository, passwordEncoder);
    }

    @Test
    void deveCriarCorretorQuandoUsuarioAdmin() {
        CreateCorretorRequest request = new CreateCorretorRequest(
                "Novo Corretor",
                "novo@test.com",
                "(82) 97777-7777",
                "333.333.333-33",
                "senha123",
                null
        );

        when(corretorRepository.existsByEmail(request.email())).thenReturn(false);
        when(corretorRepository.existsByCpf(request.cpf())).thenReturn(false);
        when(passwordEncoder.encode(request.senha())).thenReturn("hash");
        when(corretorRepository.save(any())).thenAnswer(invocation -> {
            Corretor corretor = invocation.getArgument(0);
            return new Corretor(
                    10L,
                    corretor.nomeCompleto(),
                    corretor.email(),
                    corretor.telefone(),
                    corretor.cpf(),
                    corretor.senha(),
                    corretor.perfil(),
                    corretor.dataCadastro()
            );
        });

        var response = useCase.execute(request, admin);

        assertEquals("novo@test.com", response.email());
        assertEquals(Perfil.CORRETOR, response.perfil());

        ArgumentCaptor<Corretor> captor = ArgumentCaptor.forClass(Corretor.class);
        verify(corretorRepository).save(captor.capture());
        assertEquals(Optional.of("hash"), captor.getValue().senha());
    }

    @Test
    void deveNegarCriacaoQuandoUsuarioNaoAdmin() {
        CreateCorretorRequest request = new CreateCorretorRequest(
                "Novo Corretor",
                "novo@test.com",
                "(82) 97777-7777",
                "333.333.333-33",
                "senha123",
                null
        );

        assertThrows(ForbiddenException.class, () -> useCase.execute(request, corretor));
    }
}
