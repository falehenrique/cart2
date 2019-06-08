delete from item_tabela;
--1
insert into item_tabela (id,nome, descricao,quantidade) values(1,'1-Registro ou averbação integral de contrato com conteúdo financeiro','Registro ou averbação integral de contrato, título ou documento com conteúdo financeiro',false);
insert into item_tabela (id,nome, descricao,quantidade) values(2,'1-Registro ou averbação integral de contrato com conteúdo financeiro - guarda e conservação','Registro ou averbação integral de contrato, título ou documento com conteúdo financeiro',true);
--2
insert into item_tabela (id,nome, descricao,quantidade) values(3,'2-Registro ou averbação integral de contrato sem conteúdo financeiro - até uma página','Registro integral de título, documento ou papel, sem conteúdo financeiro, inclusive ata de condomínio',true);
insert into item_tabela (id,nome, descricao,quantidade) values(4,'2-Registro ou averbação integral de contrato sem conteúdo financeiro - por página que acrescer','Registro integral de título, documento ou papel, sem conteúdo financeiro, inclusive ata de condomínio',true);
insert into item_tabela (id,nome, descricao,quantidade) values(5,'2-Registro ou averbação integral de contrato sem conteúdo financeiro - guarda e conservação','Registro integral de título, documento ou papel, sem conteúdo financeiro, inclusive ata de condomínio',true);
--3
insert into item_tabela (id,nome, descricao,quantidade) values(6,'3-Registro para notificação','Registro para fins de notificação, por destinatário, incluindo certidão à margem do registro e na 2a via',true);
--4
insert into item_tabela (id,nome, descricao,quantidade) values(7,'4-Averbação de documento sem conteúdo financeiro','Averbação de documento sem conteúdo financeiro',true);
--5
insert into item_tabela (id,nome, descricao,quantidade) values(93,'5-Registro ou averbação de contrato de alienação fiduciária, leasing ou reserva de domínio, sobre o valor financiado:','5-Registro ou averbação de contrato de alienação fiduciária, leasing ou reserva de domínio, sobre o valor financiado:',true);
--6
insert into item_tabela (id,nome, descricao,quantidade) values(94,'6-Registro de pessoa jurídica com ou sem fins lucrativos (científica, cultural, esportiva, reliogiosa e semelhantes), incluindo todos os atos do processo e arquivamento:','6-Registro de pessoa jurídica com ou sem fins lucrativos (científica, cultural, esportiva, reliogiosa e semelhantes), incluindo todos os atos processo e arquivamento:',false);
--7
insert into item_tabela (id,nome, descricao,quantidade) values(95,'7-Cancelamento de inscrição de pessoa jurídica: 1/3 (um terço) dos valores previstos nas alíneas do item 6','7-Cancelamento de inscrição de pessoa jurídica: 1/3 (um terço) dos valores previstos nas alíneas do item 6',false);

--8
insert into item_tabela (id,nome, descricao,quantidade) values(8,'8-Certidões - pela primeira folha','Certidões',true);
insert into item_tabela (id,nome, descricao,quantidade) values(9,'8-Certidões - por página que acrescer','Certidões',true);
insert into item_tabela (id,nome, descricao,quantidade) values(10,'8-Certidões - cópia de microfilme, por página','Certidões',true);
insert into item_tabela (id,nome, descricao,quantidade) values(11,'8-Certidões - guarda e conservação','Certidões',true);

insert into item_tabela (id,nome, descricao,quantidade) values(50,'Busca verbal','12',true);

--9
insert into item_tabela (id,nome, descricao,quantidade) values(96,'9-Autenticação de microfilme de acordo com o Decreto nº 1.799/1996, e de disco ótico: de microfilme ou disco ótico','Autenticação de microfilme de acordo com o Decreto nº 1.799/1996, e de disco ótico: de microfilme ou disco ótico',true);
insert into item_tabela (id,nome, descricao,quantidade) values(97,'9-Autenticação de microfilme de acordo com o Decreto nº 1.799/1996, e de disco ótico: de cópia extraída de rolo de microfilme ou disco ótico, por página ou fotograma','Autenticação de microfilme de acordo com o Decreto nº 1.799/1996, e de disco ótico: de cópia extraída de rolo de microfilme ou disco ótico, por página ou fotograma',true);

--10
insert into item_tabela (id,nome, descricao,quantidade) values(98,'10-Microfilmagem de qualquer documento referido nesta tabela qualquer que seja o número de páginas:','10-Microfilmagem de qualquer documento referido nesta tabela qualquer que seja o número de páginas:',true);
--11
insert into item_tabela (id,nome, descricao,quantidade) values(99,'11-Autenticação de livros contábeis obrigatórios das sociedades civis, qualquer que seja o número de páginas:','11-Autenticação de livros contábeis obrigatórios das sociedades civis, qualquer que seja o número de páginas:',true);
--12
insert into item_tabela (id,nome, descricao,quantidade) values(100,'12-Informação prestada por qualquer forma ou meio quando o interessado dispensar a certidão:','12-Informação prestada por qualquer forma ou meio quando o interessado dispensar a certidão:',true);
--Outros
insert into item_tabela (id,nome, descricao,quantidade) values(90,'Envio por Correios A.R.','Outros',true);
insert into item_tabela (id,nome, descricao,quantidade) values(91,'Envio por Correios Sedex SP','Outros',true);
insert into item_tabela (id,nome, descricao,quantidade) values(92,'Envio por Correios Sedex Interior','Outros',true);


/*
Página adicional
Microfilme
Busca Verbal
Diligências
Envio por correio A.R.
Envio por correio Sedex SP

Envio por correio Sedex Interior
*/