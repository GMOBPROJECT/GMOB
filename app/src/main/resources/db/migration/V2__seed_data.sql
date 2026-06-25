-- Dados iniciais para desenvolvimento e demonstração
-- Login padrão: joao@email.com / senha123 (administrador)

-- Corretores (BCrypt cost 12, senha: senha123)
INSERT INTO corretores (nome_completo, email, telefone, cpf, senha, perfil)
VALUES
    ('João Silva', 'joao@email.com', '(11) 99999-9999', '123.456.789-00',
     '$2a$12$5DVVV.QOUfo2vpX9PyxlLeNlvG.CJoOXbo7Bo/Gmfh6P8M7PLhhvW', 'administrador'),
    ('Maria Santos', 'maria@email.com', '(11) 98888-8888', '987.654.321-00',
     '$2a$12$5DVVV.QOUfo2vpX9PyxlLeNlvG.CJoOXbo7Bo/Gmfh6P8M7PLhhvW', 'corretor')
ON CONFLICT (email) DO NOTHING;

-- Tipos de imóvel
INSERT INTO tipos_imoveis (nome_tipo)
VALUES
    ('Apartamento'),
    ('Casa'),
    ('Cobertura'),
    ('Terreno'),
    ('Comercial')
ON CONFLICT (nome_tipo) DO NOTHING;

-- Imóveis (corretor_id = João)
INSERT INTO imoveis (
    corretor_id, tipo_imovel_id, status, disponibilidade, valor_aluguel,
    estado, cidade, rua, numero, complemento, valor, area, numero_comodos, descricao
)
SELECT
    c.corretor_id,
    t.tipo_imovel_id,
    v.status::"StatusImovel",
    v.disponibilidade::"Disponibilidade",
    v.valor_aluguel,
    v.estado,
    v.cidade,
    v.rua,
    v.numero,
    v.complemento,
    v.valor,
    v.area,
    v.numero_comodos,
    v.descricao
FROM corretores c
CROSS JOIN (VALUES
    ('Apartamento', 'disponivel', 'venda', NULL, 'Alagoas', 'Maceió', 'Rua das Palmeiras', '123', 'Apto 501', 350000.00, 75.50, 3, 'Apartamento espaçoso com vista para o mar, dois quartos e uma suíte.'),
    ('Casa', 'vendido', 'venda', NULL, 'Alagoas', 'Rio Largo', 'Travessa dos Cajueiros', '45', NULL, 280000.00, 120.00, 4, 'Casa térrea com quintal grande e churrasqueira, ideal para família.'),
    ('Apartamento', 'alugado', 'aluguel', 1800.00, 'Alagoas', 'Marechal Deodoro', 'Avenida Atlântica', '789', 'Bloco B, Apto 203', 1800.00, 60.00, 2, 'Apartamento mobiliado, próximo à praia do Francês.'),
    ('Terreno', 'disponivel', 'venda', NULL, 'Alagoas', 'Arapiraca', 'Rua das Flores', 'S/N', NULL, 95000.00, 250.00, 0, 'Terreno plano em área de expansão urbana.'),
    ('Cobertura', 'disponivel', 'venda', NULL, 'Alagoas', 'Maceió', 'Avenida Beira Mar', '500', 'Cobertura 1201', 1200000.00, 180.00, 5, 'Cobertura luxuosa com piscina privativa e vista panorâmica.'),
    ('Comercial', 'disponivel', 'ambos', 4500.00, 'Alagoas', 'Palmeira dos Índios', 'Rua do Comércio', '10', 'Loja 1', 450000.00, 80.00, 1, 'Ponto comercial com ótima localização no centro da cidade.')
) AS v(nome_tipo, status, disponibilidade, valor_aluguel, estado, cidade, rua, numero, complemento, valor, area, numero_comodos, descricao)
JOIN tipos_imoveis t ON t.nome_tipo = v.nome_tipo
WHERE c.email = 'joao@email.com'
ON CONFLICT (rua, numero, complemento) DO NOTHING;

-- Clientes
INSERT INTO clientes (corretor_id, nome, cpf, email, telefone, tipo_interesse)
SELECT c.corretor_id, v.nome, v.cpf, v.email, v.telefone, v.tipo_interesse::"TipoInteresseCliente"
FROM corretores c
CROSS JOIN (VALUES
    ('Ana Beatriz Lima', '111.222.333-44', 'ana.beatriz@example.com', '(82) 91234-5678', 'aluguel'),
    ('Carlos Eduardo Souza', '222.333.444-55', 'carlos.souza@example.com', '(82) 92345-6789', 'compra'),
    ('Fernanda Oliveira', '333.444.555-66', 'fernanda.oliveira@example.com', '(82) 93456-7890', 'compra')
) AS v(nome, cpf, email, telefone, tipo_interesse)
WHERE c.email = 'joao@email.com'
ON CONFLICT (cpf) DO NOTHING;

-- Visitas agendadas (datas futuras relativas a hoje)
INSERT INTO agendamentos_visitas (
    corretor_id, imovel_id, cliente_id, data_visita, hora_inicio, hora_termino, observacoes, status_agendamento
)
SELECT
    c.corretor_id,
    i.imovel_id,
    cl.cliente_id,
    (CURRENT_DATE + v.dias)::date,
    v.hora_inicio::time,
    v.hora_termino::time,
    v.observacoes,
    'agendado'::"StatusAgendamento"
FROM corretores c
CROSS JOIN (VALUES
    ('Rua das Palmeiras', '123', '111.222.333-44', 7, '10:00:00', '11:00:00', 'Cliente interessado em apartamento com 2 quartos.'),
    ('Rua das Flores', 'S/N', '222.333.444-55', 10, '14:00:00', '15:00:00', 'Visita ao terreno para construção.'),
    ('Avenida Beira Mar', '500', '333.444.555-66', 14, '09:00:00', '10:30:00', 'Visita à cobertura de luxo.')
) AS v(imovel_rua, imovel_numero, cliente_cpf, dias, hora_inicio, hora_termino, observacoes)
JOIN imoveis i ON i.corretor_id = c.corretor_id AND i.rua = v.imovel_rua AND i.numero = v.imovel_numero
JOIN clientes cl ON cl.cpf = v.cliente_cpf
WHERE c.email = 'joao@email.com'
ON CONFLICT (imovel_id, data_visita) DO NOTHING;

-- Ajuste das sequences
SELECT setval(pg_get_serial_sequence('corretores', 'corretor_id'), COALESCE((SELECT MAX(corretor_id) FROM corretores), 1));
SELECT setval(pg_get_serial_sequence('tipos_imoveis', 'tipo_imovel_id'), COALESCE((SELECT MAX(tipo_imovel_id) FROM tipos_imoveis), 1));
SELECT setval(pg_get_serial_sequence('imoveis', 'imovel_id'), COALESCE((SELECT MAX(imovel_id) FROM imoveis), 1));
SELECT setval(pg_get_serial_sequence('clientes', 'cliente_id'), COALESCE((SELECT MAX(cliente_id) FROM clientes), 1));
SELECT setval(pg_get_serial_sequence('agendamentos_visitas', 'agendamento_id'), COALESCE((SELECT MAX(agendamento_id) FROM agendamentos_visitas), 1));
