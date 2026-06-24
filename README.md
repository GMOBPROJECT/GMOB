# GMOB Spring Boot Backend

Sistema de Gestão de Imóveis migrado de NestJS para Spring Boot 3 + Java 21.

## Pré-requisitos

- Java 21
- Maven 3.9+
- Docker (PostgreSQL 15 + Redis 7)

## Subir infraestrutura

```bash
docker compose up -d
```

O PostgreSQL do GMOB usa a porta **5433** (evita conflito com outros Postgres locais na 5432).

## Executar aplicação

**Importante:** este é um projeto Maven **multi-module**. Sempre execute os comandos a partir da pasta `GMOB/` (raiz), não de dentro de `gmob-app/`.

```bash
# 1. Instalar todos os módulos no repositório local (obrigatório na 1ª vez ou após mudanças)
mvn clean install

# 2. Subir a aplicação
mvn -pl gmob-app spring-boot:run
```

Variáveis de ambiente opcionais (defaults já configurados para o Docker Compose):

| Variável | Default |
|----------|---------|
| `DB_HOST` | localhost |
| `DB_PORT` | 5433 |
| `DB_NAME` | gmob_db |
| `DB_USER` | postgres |
| `DB_PASSWORD` | password |
| `REDIS_HOST` | localhost |
| `REDIS_PORT` | 6379 |

API disponível em `http://localhost:3000/api`

## Módulos

| Módulo | Pacote | Responsabilidade |
|--------|--------|------------------|
| gmob-infra | br.com.gmob.infra | Exceções, enums, CORS, validação |
| gmob-auth | br.com.gmob.auth | JWT, login, register, logout |
| gmob-corretor | br.com.gmob.corretor | CRUD corretores |
| gmob-imovel | br.com.gmob.imovel | CRUD imóveis + tipos + imagens |
| gmob-cliente | br.com.gmob.cliente | CRUD clientes + transações |

## Testes

```bash
mvn test
```

Testes de integração com Testcontainers são ignorados automaticamente quando Docker não está disponível.
