CREATE TABLE lote (
    id SERIAL PRIMARY KEY,
    usuario_id BIGINT references usuario(id) NOT NULL,
    cliente_id BIGINT references cliente(id) NOT NULL,
    inicio TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    termo_abertura VARCHAR(250) NOT NULL,
    arquivo VARCHAR(250) NOT NULL,
    termo_encerramento  VARCHAR(250) NOT NULL,
    protocolo_id BIGINT REFERENCES protocolo(id),
    dt_protocolo  TIMESTAMP WITHOUT TIME ZONE,
    dt_registro  TIMESTAMP WITHOUT TIME ZONE,
    dt_faturado  TIMESTAMP WITHOUT TIME ZONE,
    finalizado TIMESTAMP WITHOUT TIME ZONE,
    resultado TEXT
);