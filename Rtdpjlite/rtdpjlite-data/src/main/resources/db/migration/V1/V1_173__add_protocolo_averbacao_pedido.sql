ALTER TABLE db_portal.tb_pedido_protocolo ADD COLUMN protocolo_averbacao_id BIGINT REFERENCES rtdpj.protocolo(id);
ALTER TABLE db_portal.tb_pedido ADD COLUMN protocolo_id BIGINT REFERENCES rtdpj.protocolo(id);

CREATE TABLE db_portal.tb_pedido_status (
    id SERIAL PRIMARY KEY,
    nome_aguardando VARCHAR(250) NOT NULL,
    nome VARCHAR(250) NOT NULL,
    data TIMESTAMP WITHOUT TIME ZONE,
    numero BIGINT,
    mensagem VARCHAR(250),
    cartorio_parceiro_id BIGINT,
    nr_ordem INTEGER NOT NULL,
    cartorio_parceiro_nome VARCHAR(250),
    pedido_id BIGINT REFERENCES db_portal.tb_pedido(id)
);

CREATE TABLE db_portal.tb_pedido_protocolo_status (
    id SERIAL PRIMARY KEY,
    nome_aguardando VARCHAR(250) NOT NULL,
    nome VARCHAR(250) NOT NULL,
    data TIMESTAMP WITHOUT TIME ZONE,
    numero BIGINT,
    mensagem VARCHAR(250),
    remetente VARCHAR(250) NOT NULL,
    arquivo VARCHAR(250),
    nr_ordem INTEGER NOT NULL,
    pedido_protocolo_id BIGINT REFERENCES db_portal.tb_pedido_protocolo(id)
);

