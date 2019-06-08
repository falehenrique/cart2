CREATE TABLE objeto_atributo_opcao(
  id serial,
  objeto_atributo_id bigint,
  nome varchar(255) not null,
  constraint pk_objeto_atributo_opcao primary key (id)
);