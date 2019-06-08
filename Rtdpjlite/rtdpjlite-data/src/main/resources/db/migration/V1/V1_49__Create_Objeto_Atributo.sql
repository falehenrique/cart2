CREATE TABLE objeto_atributo(
  id serial,
  nome varchar not null,
  objeto_id bigint not null,
  constraint pk_objeto_atributo primary key (id)
);