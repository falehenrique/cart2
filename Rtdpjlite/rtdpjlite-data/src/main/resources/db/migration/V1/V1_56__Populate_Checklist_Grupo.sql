INSERT INTO checklist_grupo(nome, natureza_id) VALUES ('CHECKLIST DE EXAME', (select id from natureza where nome = 'ABAIXO ASSINADO'));
INSERT INTO checklist_grupo(nome, natureza_id) VALUES ('CHECKLIST DE EXAME', (select id from natureza where nome = 'ABERTURA DE CREDITO'));
INSERT INTO checklist_grupo(nome, natureza_id) VALUES ('CHECKLIST DE EXAME', (select id from natureza where nome = 'CONTRATO'));
INSERT INTO checklist_grupo(nome, natureza_id) VALUES ('GARANTIAS', (select id from natureza where nome = 'ABAIXO ASSINADO'));
INSERT INTO checklist_grupo(nome, natureza_id) VALUES ('VALORES', (select id from natureza where nome = 'ABERTURA DE CREDITO'));
INSERT INTO checklist_grupo(nome, natureza_id) VALUES ('AUXILIARES', (select id from natureza where nome = 'CONTRATO'));
