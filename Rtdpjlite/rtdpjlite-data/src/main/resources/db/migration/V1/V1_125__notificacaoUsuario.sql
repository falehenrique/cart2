CREATE TABLE usuario_notificacao(
  id SERIAL PRIMARY KEY,
  usuario_id BIGINT NOT NULL REFERENCES usuario(id),
  criada timestamp WITHOUT TIME ZONE,
  lida timestamp WITHOUT TIME ZONE,
  assunto VARCHAR(250) NOT NULL,
  descricao TEXT NOT NULL
);