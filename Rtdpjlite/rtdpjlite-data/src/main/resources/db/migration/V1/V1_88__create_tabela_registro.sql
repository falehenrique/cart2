CREATE TABLE registro(
  id SERIAL PRIMARY KEY,
  registro VARCHAR(250),
  numero_registro bigint,
  data_registro timestamp without time zone,
  registro_referencia VARCHAR(250),
  nr_pasta_pj varchar(250),
  situacao_atual TEXT,
  observacao TEXT,
  protocolo_id bigint REFERENCES protocolo(id),
  tp_especialidade VARCHAR(250) NOT NULL,
  objetos jsonb,
  natureza_id BIGINT,
  sub_natureza_id BIGINT
);