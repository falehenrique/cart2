INSERT INTO cidade (nome, estado_id) VALUES ('SAO PAULO', (select id from estado where sg = 'SP'));
INSERT INTO cidade (nome, estado_id) VALUES ('SANTOS', (select id from estado where sg = 'SP'));
INSERT INTO cidade (nome, estado_id) VALUES ('GUARUJA', (select id from estado where sg = 'SP'));

INSERT INTO cidade (nome, estado_id) VALUES ('RIO DE JANEIRO', (select id from estado where sg = 'RJ'));
