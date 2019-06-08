CREATE TABLE usuario_dados(
  id serial,
  id_usuario integer not null references usuario(id),
  nome varchar(255) not null,
  id_cargo integer not null references cargo(id),
  constraint pk_usuario_dados primary key (id)
);