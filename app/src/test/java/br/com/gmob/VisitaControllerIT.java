package br.com.gmob;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers(disabledWithoutDocker = true)
class VisitaControllerIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("gmob_db")
            .withUsername("postgres")
            .withPassword("password");

    @Container
    static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:7-alpine"))
            .withExposedPorts(6379);

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveCriarEListarVisita() throws Exception {
        String registerPayload = """
                {
                  "nome_completo": "Corretor Visita IT",
                  "email": "visita-it@test.com",
                  "telefone": "(82) 99999-9999",
                  "cpf": "987.654.321-00",
                  "senha": "senha123"
                }
                """;

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerPayload))
                .andExpect(status().isOk());

        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"visita-it@test.com","senha":"senha123"}
                                """))
                .andExpect(status().isOk())
                .andReturn();

        String token = com.jayway.jsonpath.JsonPath.read(
                loginResult.getResponse().getContentAsString(),
                "$.access_token"
        );

        String tipoPayload = """
                {"nome_tipo":"Apartamento IT"}
                """;

        MvcResult tipoResult = mockMvc.perform(post("/tipos-imoveis")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(tipoPayload))
                .andExpect(status().isOk())
                .andReturn();

        Long tipoId = ((Number) com.jayway.jsonpath.JsonPath.read(
                tipoResult.getResponse().getContentAsString(),
                "$.tipo_imovel_id"
        )).longValue();

        String imovelPayload = """
                {
                  "tipo_imovel_id": %d,
                  "disponibilidade": "venda",
                  "estado": "AL",
                  "cidade": "Maceió",
                  "rua": "Rua Visita IT",
                  "numero": "100",
                  "valor": 250000,
                  "area": 80,
                  "numero_comodos": 3,
                  "descricao": "Imóvel teste visita"
                }
                """.formatted(tipoId);

        MvcResult imovelResult = mockMvc.perform(post("/imoveis")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(imovelPayload))
                .andExpect(status().isOk())
                .andReturn();

        Long imovelId = ((Number) com.jayway.jsonpath.JsonPath.read(
                imovelResult.getResponse().getContentAsString(),
                "$.imovel_id"
        )).longValue();

        String clientePayload = """
                {
                  "nome_completo": "Cliente Visita IT",
                  "email": "cliente-visita-it@test.com",
                  "telefone": "(82) 97777-7777",
                  "cpf": "456.789.123-00",
                  "tipo_interesse": "compra"
                }
                """;

        MvcResult clienteResult = mockMvc.perform(post("/clientes")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clientePayload))
                .andExpect(status().isOk())
                .andReturn();

        Long clienteId = ((Number) com.jayway.jsonpath.JsonPath.read(
                clienteResult.getResponse().getContentAsString(),
                "$.cliente_id"
        )).longValue();

        LocalDate dataFutura = LocalDate.now().plusDays(10);
        String visitaPayload = """
                {
                  "imovel_id": %d,
                  "cliente_id": %d,
                  "data_visita": "%s",
                  "hora_inicio": "10:00",
                  "hora_termino": "11:00",
                  "observacoes": "Visita integração"
                }
                """.formatted(imovelId, clienteId, dataFutura);

        mockMvc.perform(post("/visitas")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(visitaPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.agendamento_id").isNumber())
                .andExpect(jsonPath("$.status_agendamento").value("agendado"));

        mockMvc.perform(get("/visitas?page=1&limit=10")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.total").value(1));
    }
}
