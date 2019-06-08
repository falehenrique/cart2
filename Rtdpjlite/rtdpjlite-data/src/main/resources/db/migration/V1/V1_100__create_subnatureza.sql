ALTER TABLE natureza ADD CONSTRAINT natureza_pk PRIMARY KEY (id);

CREATE TABLE sub_natureza(
  id SERIAL PRIMARY KEY,
  nome VARCHAR(250) NOT NULL,
  natureza_id BIGINT REFERENCES natureza(id)
);

ALTER TABLE protocolo ADD COLUMN sub_natureza_id BIGINT REFERENCES sub_natureza(id);

INSERT INTO sub_natureza (nome, natureza_id) VALUES ( 'sub1 ', 1);
INSERT INTO sub_natureza (nome, natureza_id) VALUES ( 'sub2 ', 1);
INSERT INTO sub_natureza (nome, natureza_id) VALUES ( 'sub3 ', 1);
INSERT INTO sub_natureza (nome, natureza_id) VALUES ( 'sub4 ', 2);
INSERT INTO sub_natureza (nome, natureza_id) VALUES ( 'sub5 ', 2);
INSERT INTO sub_natureza (nome, natureza_id) VALUES ( 'sub6 ', 3);
INSERT INTO sub_natureza (nome, natureza_id) SELECT 'ssub', id FROM natureza WHERE NOME = 'NOTIFICACAO EXTRAJUDICIAL';

INSERT  INTO  forma_entrega (nome) VALUES ('ENCAMINHAR PARA OUTRO CARTÓRIO');