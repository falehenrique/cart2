CREATE TABLE checklist_item (
  id serial,
  nome varchar(255) NOT NULL,
  obrigatorio boolean not null default false,
  checklist_grupo_id bigint NOT NULL,
  texto_devolucao TEXT
);