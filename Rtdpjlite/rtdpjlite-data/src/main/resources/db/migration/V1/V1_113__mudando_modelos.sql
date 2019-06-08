ALTER TABLE modelo DROP COLUMN tipo_modelo_id;
ALTER TABLE modelo ADD COLUMN ic_carimbo BOOLEAN DEFAULT TRUE;
DROP TABLE tipo_modelo;

CREATE TABLE modelo_natureza_subnatureza (
  id SERIAL PRIMARY KEY,
  natureza_id BIGINT REFERENCES natureza(id),
  sub_natureza_id BIGINT REFERENCES sub_natureza(id),
  modelo_id BIGINT REFERENCES modelo(id)
);