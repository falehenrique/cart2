INSERT INTO objeto_atributo (objeto_id, nome) VALUES ((select id from objeto where nome = 'AUTOMÓVEL'), 'RENAVAM');
INSERT INTO objeto_atributo (objeto_id, nome) VALUES ((select id from objeto where nome = 'AUTOMÓVEL'), 'MODELO');
INSERT INTO objeto_atributo (objeto_id, nome) VALUES ((select id from objeto where nome = 'AUTOMÓVEL'), 'MARCA');
INSERT INTO objeto_atributo (objeto_id, nome) VALUES ((select id from objeto where nome = 'MÁQUINA AGRÍCOLA'), 'MODELO');
INSERT INTO objeto_atributo (objeto_id, nome) VALUES ((select id from objeto where nome = 'MÁQUINA AGRÍCOLA'), 'MARCA');
INSERT INTO objeto_atributo (objeto_id, nome) VALUES ((select id from objeto where nome = 'ANIMAL'), 'TIPO');
INSERT INTO objeto_atributo (objeto_id, nome) VALUES ((select id from objeto where nome = 'ANIMAL'), 'REGISTRO');