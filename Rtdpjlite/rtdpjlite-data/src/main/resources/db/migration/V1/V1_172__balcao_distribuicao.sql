CREATE SCHEMA db_portal;

CREATE TABLE db_portal.flyway_schema_history (
    installed_rank SERIAL PRIMARY KEY,
    version character varying(50),
    description character varying(200) NOT NULL,
    type character varying(20) NOT NULL,
    script character varying(1000) NOT NULL,
    checksum integer,
    installed_by character varying(100) NOT NULL,
    installed_on timestamp without time zone DEFAULT now() NOT NULL,
    execution_time integer NOT NULL,
    success boolean NOT NULL
);

CREATE TABLE db_portal.tb_mensagem (
    id SERIAL PRIMARY KEY,
    tipo_id bigint,
    remetente_id bigint,
    destinatario_id bigint,
    arquivo text,
    pedido_id bigint,
    conteudo text,
    hash character varying(250),
    data timestamp without time zone DEFAULT now()
);

CREATE TABLE db_portal.tb_modelo_notificacao (
    id SERIAL PRIMARY KEY,
    titulo character varying(250) NOT NULL,
    texto text NOT NULL,
    descricao character varying(250) DEFAULT 'descricao'::character varying NOT NULL
);

CREATE TABLE db_portal.tb_pedido (
    id SERIAL PRIMARY KEY,
    apresentante_id bigint,
    parceiro_id bigint,
    arquivo character varying(250),
    sub_natureza character varying(250),
    tipo character varying(2),
    protocolo bigint,
    registro character varying(250),
    numero_registro bigint,
    observacao text,
    data timestamp without time zone
);
CREATE TABLE db_portal.tb_permissao (
    id SERIAL PRIMARY KEY,
    nome character varying(250) NOT NULL
);
CREATE TABLE db_portal.tb_tipo_mensagem (
    id SERIAL PRIMARY KEY,
    nome character varying(250) NOT NULL
);
CREATE TABLE db_portal.tb_usuario (
    id SERIAL PRIMARY KEY,
    nome character varying(250) NOT NULL,
    usuario character varying(250),
    email character varying(250),
    senha character varying(250),
    reset_token character varying(250),
    dt_cadastro timestamp without time zone
);
CREATE TABLE db_portal.tb_usuario_modelo_notificacao (
    id SERIAL PRIMARY KEY,
    usuario_id bigint,
    modelo_notificacao_id bigint
);
CREATE TABLE db_portal.tb_usuario_permissao (
    id SERIAL PRIMARY KEY,
    usuario_id bigint,
    permissao_id bigint
);


ALTER TABLE db_portal.tb_usuario ADD COLUMN cliente_id  BIGINT REFERENCES rtdpj.cliente(id);
ALTER TABLE db_portal.tb_usuario ADD COLUMN cartorio_parceiro_id  BIGINT REFERENCES rtdpj.cartorio_parceiro(id);


ALTER TABLE db_portal.tb_pedido ADD COLUMN cliente_id BIGINT references rtdpj.cliente(id);
ALTER TABLE db_portal.tb_pedido ADD COLUMN responsavel_id BIGINT REFERENCES db_portal.tb_usuario(id);

ALTER TABLE db_portal.tb_pedido ADD COLUMN dt_finalizado TIMESTAMP WITHOUT TIME ZONE;


CREATE TABLE db_portal.tb_pedido_protocolo(
    id SERIAL PRIMARY KEY,
    pedido_id BIGINT REFERENCES db_portal.tb_pedido(id),
    protocolo_id BIGINT REFERENCES rtdpj.protocolo(id),
    cartorio_parceiro_id BIGINT REFERENCES rtdpj.cartorio_parceiro(id),
    protocolo VARCHAR(250),
    protocolo_data TIMESTAMP WITHOUT TIME ZONE,
    registro VARCHAR(250),
    registro_data TIMESTAMP WITHOUT TIME ZONE
);

ALTER TABLE db_portal.tb_pedido DROP COLUMN parceiro_id;

ALTER TABLE rtdpj.cartorio_parceiro ADD COLUMN ic_envio_eletronico BOOLEAN default false;

ALTER TABLE rtdpj.configuracao ADD COLUMN certidao_automatica_id BIGINT REFERENCES rtdpj.natureza(id);
UPDATE rtdpj.configuracao SET certidao_automatica_id = (SELECT id FROM rtdpj.natureza where nome = 'CERTIDAO ELETRÔNICA' or tipo_emissao_certidao_id = 20 limit 1);
SELECT setval('db_portal.tb_usuario_id_seq', (SELECT max(id) FROM db_portal.tb_usuario), true);
