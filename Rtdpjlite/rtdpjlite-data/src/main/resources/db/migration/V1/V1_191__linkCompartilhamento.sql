CREATE TABLE link_compartilhado(
    id VARCHAR(250) PRIMARY KEY,
    usuario_id BIGINT REFERENCES usuario(id) NOT NULL,
    data  TIMESTAMP without time zone DEFAULT now() NOT NULL
);

CREATE TABLE link_compartilhado_registro(
  id SERIAL PRIMARY KEY,
  link_compartilhado_id VARCHAR(250) REFERENCES link_compartilhado(id),
  registro_id BIGINT REFERENCES registro(id) NOT NULL,
  numero_registro BIGINT NOT NULL,
  registro VARCHAR(250) NOT NULL,
  data TIMESTAMP  without time zone DEFAULT now() NOT NULL,
  especialidade VARCHAR(250) NOT NULL
);

ALTER TABLE configuracao ADD COLUMN url_portal VARCHAR(250);