ALTER TABLE reversao RENAME TO reversao_old;

CREATE TABLE reversao(
    id SERIAL PRIMARY KEY,
    usuario_id INTEGER REFERENCES usuario(id),
    data TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    motivo TEXT NOT NULL,

    protocolo_id INTEGER NOT NULL REFERENCES protocolo(id),
    protocolo_tipo_id INTEGER NOT NULL,
    numero_registro BIGINT
);

