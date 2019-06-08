ALTER TABLE lote RENAME TO lote_item;

CREATE TABLE lote (
    id SERIAL PRIMARY KEY NOT NULL,
    dia TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    usuario_id BIGINT references usuario(id) NOT NULL,
    cliente_id BIGINT references cliente(id) NOT NULL
);

INSERT INTO lote (dia, usuario_id, cliente_id) SELECT inicio, usuario_id, cliente_id FROM lote_item GROUP BY inicio, usuario_id, cliente_id;

ALTER TABLE lote_item ADD COLUMN lote_id BIGINT REFERENCES lote(id);
UPDATE lote_item set lote_id = 1;
ALTER TABLE lote_item DROP COLUMN usuario_id;
ALTER TABLE lote_item DROP COLUMN cliente_id;

