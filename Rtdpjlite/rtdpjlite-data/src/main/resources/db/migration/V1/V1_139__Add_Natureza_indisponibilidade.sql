INSERT INTO natureza (nome, dias_previsao, dias_vencimento, dias_reentrada, natureza_tipo_id) VALUES
  ('INDISPONIBILIDADE',5,30,5,1);

INSERT INTO sub_natureza (nome, natureza_id) VALUES ('REGISTRO', (SELECT id FROM natureza WHERE nome = 'INDISPONIBILIDADE'));
INSERT INTO sub_natureza (nome, natureza_id) VALUES ('CANCELAMENTO', (SELECT id FROM natureza WHERE nome = 'INDISPONIBILIDADE'));