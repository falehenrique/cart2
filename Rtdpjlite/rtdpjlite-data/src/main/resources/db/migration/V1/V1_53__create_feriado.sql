CREATE TABLE feriado(
  id serial,
  nome varchar(250) not null,
  dia date not null,
  recorrente boolean not null
);