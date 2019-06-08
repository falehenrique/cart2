CREATE TABLE carimbo (
  id SERIAL PRIMARY KEY,
  nome VARCHAR(250) NOT NULL,
  carimbo TEXT NOT NULL
);

INSERT INTO carimbo (nome, carimbo) SELECT 'carimbo padrao', carimbo_certidao FROM configuracao;
ALTER TABLE configuracao DROP COLUMN carimbo_certidao;