ALTER TABLE protocolo ADD COLUMN ic_registrar_titulo_outra_praca BOOLEAN DEFAULT FALSE;

CREATE TABLE protocolo_cartorio_parceiro (
    id SERIAL PRIMARY KEY,
    protocolo_id BIGINT REFERENCES protocolo (id),
    cartorio_parceiro_id BIGINT REFERENCES cartorio_parceiro(id)
);

INSERT INTO protocolo_cartorio_parceiro (protocolo_id, cartorio_parceiro_id) SELECT id, cartorio_parceiro_id FROM protocolo WHERE cartorio_parceiro_id is not null;

ALTER TABLE protocolo DROP COLUMN cartorio_parceiro_id;