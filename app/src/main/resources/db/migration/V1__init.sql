-- Consolidated schema from Prisma migrations

CREATE TYPE "Perfil" AS ENUM ('corretor', 'administrador');
CREATE TYPE "StatusImovel" AS ENUM ('disponivel', 'vendido', 'alugado');
CREATE TYPE "TipoInteresseCliente" AS ENUM ('compra', 'aluguel');
CREATE TYPE "StatusAgendamento" AS ENUM ('agendado', 'confirmado', 'cancelado', 'realizado');
CREATE TYPE "TipoTransacao" AS ENUM ('venda', 'aluguel');
CREATE TYPE "Disponibilidade" AS ENUM ('venda', 'aluguel', 'ambos');

CREATE TABLE "corretores" (
    "corretor_id" SERIAL NOT NULL,
    "nome_completo" VARCHAR(255) NOT NULL,
    "email" VARCHAR(255) NOT NULL,
    "telefone" VARCHAR(20) NOT NULL,
    "cpf" VARCHAR(14) NOT NULL,
    "senha" VARCHAR(255) NOT NULL,
    "data_cadastro" TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "perfil" "Perfil" NOT NULL DEFAULT 'corretor',
    CONSTRAINT "corretores_pkey" PRIMARY KEY ("corretor_id")
);

CREATE UNIQUE INDEX "corretores_email_key" ON "corretores"("email");
CREATE UNIQUE INDEX "corretores_cpf_key" ON "corretores"("cpf");

CREATE TABLE "tipos_imoveis" (
    "tipo_imovel_id" SERIAL NOT NULL,
    "nome_tipo" VARCHAR(50) NOT NULL,
    CONSTRAINT "tipos_imoveis_pkey" PRIMARY KEY ("tipo_imovel_id")
);

CREATE TABLE "imoveis" (
    "imovel_id" SERIAL NOT NULL,
    "corretor_id" INTEGER NOT NULL,
    "tipo_imovel_id" INTEGER NOT NULL,
    "status" "StatusImovel" NOT NULL,
    "disponibilidade" "Disponibilidade" NOT NULL DEFAULT 'ambos',
    "valor_aluguel" DECIMAL(15,2),
    "estado" VARCHAR(50) NOT NULL,
    "cidade" VARCHAR(100) NOT NULL,
    "rua" VARCHAR(255) NOT NULL,
    "numero" VARCHAR(20) NOT NULL,
    "complemento" VARCHAR(100),
    "valor" DECIMAL(15,2) NOT NULL,
    "area" DECIMAL(10,2) NOT NULL,
    "numero_comodos" INTEGER NOT NULL,
    "descricao" TEXT,
    "data_cadastro" TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT "imoveis_pkey" PRIMARY KEY ("imovel_id")
);

CREATE TABLE "clientes" (
    "cliente_id" SERIAL NOT NULL,
    "corretor_id" INTEGER NOT NULL,
    "nome" VARCHAR(255) NOT NULL,
    "cpf" VARCHAR(14) NOT NULL,
    "email" VARCHAR(255) NOT NULL,
    "telefone" VARCHAR(20) NOT NULL,
    "tipo_interesse" "TipoInteresseCliente" NOT NULL,
    "arquivado" BOOLEAN NOT NULL DEFAULT false,
    "data_cadastro" TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT "clientes_pkey" PRIMARY KEY ("cliente_id")
);

CREATE TABLE "agendamentos_visitas" (
    "agendamento_id" SERIAL NOT NULL,
    "corretor_id" INTEGER NOT NULL,
    "imovel_id" INTEGER NOT NULL,
    "cliente_id" INTEGER NOT NULL,
    "data_visita" DATE NOT NULL,
    "hora_inicio" TIME(0) NOT NULL,
    "hora_termino" TIME(0) NOT NULL,
    "observacoes" TEXT,
    "status_agendamento" "StatusAgendamento" NOT NULL DEFAULT 'agendado',
    "data_agendamento" TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT "agendamentos_visitas_pkey" PRIMARY KEY ("agendamento_id")
);

CREATE TABLE "transacoes_imoveis" (
    "transacao_id" SERIAL NOT NULL,
    "imovel_id" INTEGER NOT NULL,
    "cliente_id" INTEGER NOT NULL,
    "corretor_id" INTEGER NOT NULL,
    "tipo_transacao" "TipoTransacao" NOT NULL,
    "data_transacao" TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT "transacoes_imoveis_pkey" PRIMARY KEY ("transacao_id")
);

CREATE TABLE "imagens_imoveis" (
    "imagem_id" SERIAL NOT NULL,
    "url" TEXT NOT NULL,
    "imovel_id" INTEGER NOT NULL,
    CONSTRAINT "imagens_imoveis_pkey" PRIMARY KEY ("imagem_id")
);

CREATE UNIQUE INDEX "tipos_imoveis_nome_tipo_key" ON "tipos_imoveis"("nome_tipo");
CREATE UNIQUE INDEX "imoveis_rua_numero_complemento_key" ON "imoveis"("rua", "numero", "complemento");
CREATE UNIQUE INDEX "clientes_cpf_key" ON "clientes"("cpf");
CREATE UNIQUE INDEX "agendamentos_visitas_imovel_id_data_visita_key" ON "agendamentos_visitas"("imovel_id", "data_visita");
CREATE UNIQUE INDEX "transacoes_imoveis_imovel_id_cliente_id_tipo_transacao_key" ON "transacoes_imoveis"("imovel_id", "cliente_id", "tipo_transacao");

ALTER TABLE "imoveis" ADD CONSTRAINT "imoveis_corretor_id_fkey" FOREIGN KEY ("corretor_id") REFERENCES "corretores"("corretor_id") ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE "imoveis" ADD CONSTRAINT "imoveis_tipo_imovel_id_fkey" FOREIGN KEY ("tipo_imovel_id") REFERENCES "tipos_imoveis"("tipo_imovel_id") ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE "clientes" ADD CONSTRAINT "clientes_corretor_id_fkey" FOREIGN KEY ("corretor_id") REFERENCES "corretores"("corretor_id") ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE "agendamentos_visitas" ADD CONSTRAINT "agendamentos_visitas_corretor_id_fkey" FOREIGN KEY ("corretor_id") REFERENCES "corretores"("corretor_id") ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE "agendamentos_visitas" ADD CONSTRAINT "agendamentos_visitas_imovel_id_fkey" FOREIGN KEY ("imovel_id") REFERENCES "imoveis"("imovel_id") ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE "agendamentos_visitas" ADD CONSTRAINT "agendamentos_visitas_cliente_id_fkey" FOREIGN KEY ("cliente_id") REFERENCES "clientes"("cliente_id") ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE "transacoes_imoveis" ADD CONSTRAINT "transacoes_imoveis_imovel_id_fkey" FOREIGN KEY ("imovel_id") REFERENCES "imoveis"("imovel_id") ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE "transacoes_imoveis" ADD CONSTRAINT "transacoes_imoveis_cliente_id_fkey" FOREIGN KEY ("cliente_id") REFERENCES "clientes"("cliente_id") ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE "transacoes_imoveis" ADD CONSTRAINT "transacoes_imoveis_corretor_id_fkey" FOREIGN KEY ("corretor_id") REFERENCES "corretores"("corretor_id") ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE "imagens_imoveis" ADD CONSTRAINT "imagens_imoveis_imovel_id_fkey" FOREIGN KEY ("imovel_id") REFERENCES "imoveis"("imovel_id") ON DELETE RESTRICT ON UPDATE CASCADE;
