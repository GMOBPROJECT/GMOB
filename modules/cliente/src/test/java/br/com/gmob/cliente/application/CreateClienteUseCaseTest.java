package br.com.gmob.cliente.application;

import br.com.gmob.cliente.api.dto.CreateClienteRequest;
import br.com.gmob.cliente.domain.model.Cliente;
import br.com.gmob.cliente.domain.port.ClienteRepositoryPort;
import br.com.gmob.infra.domain.enums.Perfil;
import br.com.gmob.infra.domain.enums.TipoInteresseCliente;
import br.com.gmob.infra.exception.ForbiddenException;
import br.com.gmob.infra.security.AuthenticatedUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateClienteUseCaseTest {

    @Mock
    private ClienteRepositoryPort clienteRepository;

    private CreateClienteUseCase useCase;

    private final AuthenticatedUser corretor = new AuthenticatedUser(
            5L, "Corretor", "corretor@test.com", "(82) 98888-8888", "222.222.222-22",
            Perfil.CORRETOR, Instant.now()
    );

    @BeforeEach
    void setUp() {
        useCase = new CreateClienteUseCase(clienteRepository);
    }

    @Test
    void deveCriarClienteVinculadoAoCorretorLogado() {
        CreateClienteRequest request = new CreateClienteRequest(
                "Cliente Teste",
                "cliente@test.com",
                "(82) 97777-7777",
                "333.333.333-33",
                TipoInteresseCliente.COMPRA
        );

        when(clienteRepository.existsByEmail(request.email())).thenReturn(false);
        when(clienteRepository.existsByCpf(request.cpf())).thenReturn(false);
        when(clienteRepository.save(any())).thenAnswer(invocation -> {
            Cliente cliente = invocation.getArgument(0);
            return new Cliente(
                    1L,
                    cliente.corretorId(),
                    cliente.nome(),
                    cliente.cpf(),
                    cliente.email(),
                    cliente.telefone(),
                    cliente.tipoInteresse(),
                    cliente.arquivado(),
                    cliente.dataCadastro()
            );
        });

        var response = useCase.execute(request, corretor);

        assertEquals("Cliente Teste", response.nome());
        assertEquals(TipoInteresseCliente.COMPRA, response.tipoInteresse());
    }

    @Test
    void deveRejeitarEmailDuplicado() {
        CreateClienteRequest request = new CreateClienteRequest(
                "Cliente Teste",
                "cliente@test.com",
                "(82) 97777-7777",
                "333.333.333-33",
                TipoInteresseCliente.ALUGUEL
        );

        when(clienteRepository.existsByEmail(request.email())).thenReturn(true);

        assertThrows(ForbiddenException.class, () -> useCase.execute(request, corretor));
    }
}
