CREATE TABLE intimacao_protocolo(
  id serial,
  dia TIMESTAMP NOT NULL,
  nome varchar(255) not null,
  resultado TEXT NOT NULL,
  protocolo_id bigint,
  constraint intimacao_protocolo_pk primary key (id)
);