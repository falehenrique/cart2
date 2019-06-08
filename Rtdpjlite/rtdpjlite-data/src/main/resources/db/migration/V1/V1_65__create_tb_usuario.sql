CREATE TABLE usuario(
  id serial,
  nome VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL unique,
  senha VARCHAR(255) NOT NULL,
  constraint pk_usuario primary key (id)
);