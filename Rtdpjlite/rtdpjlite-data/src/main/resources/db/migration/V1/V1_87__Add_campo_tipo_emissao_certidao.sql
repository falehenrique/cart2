ALTER TABLE natureza ADD COLUMN tipo_emissao_certidao_id bigint;

insert into natureza (natureza_tipo_id, nome, tipo_emissao_certidao_id) values(2,'CERTIDAO IMAGEM',20);
insert into natureza (natureza_tipo_id, nome, tipo_emissao_certidao_id) values(2,'CERTIDAO TEXTO',10);
insert into natureza (natureza_tipo_id, nome, tipo_emissao_certidao_id) values(2,'CERTIDAO INTEIRO TEOR',30);