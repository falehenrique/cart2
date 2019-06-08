CREATE TABLE cartorio_parceiro(
  id SERIAL PRIMARY KEY,
  nome VARCHAR(250) NOT NULL,
  alias VARCHAR(250) NOT NULL,
  email VARCHAR(250) NOT NULL UNIQUE
);

INSERT INTO cartorio_parceiro (nome, alias, email) VALUES ( 'Cartorio 1', 'Alias 1', 'email1@email.com.br');
INSERT INTO cartorio_parceiro (nome, alias, email) VALUES ( 'Cartorio 2', 'Alias 2', 'email2@email.com.br');
INSERT INTO cartorio_parceiro (nome, alias, email) VALUES ( 'Cartorio 3', 'Alias 3', 'email3@email.com.br');


ALTER TABLE protocolo ADD COLUMN cartorio_parceiro_id BIGINT REFERENCES cartorio_parceiro(id);