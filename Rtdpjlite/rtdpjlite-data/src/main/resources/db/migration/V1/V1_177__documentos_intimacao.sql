ALTER TABLE rtdpj.configuracao ADD COLUMN ds_intimacao_comparecimento TEXT;
ALTER TABLE rtdpj.configuracao ADD COLUMN ds_intimacao_checklist TEXT;

--corrigir email duplicado
with tb as (
    select
      distinct email from db_portal.tb_usuario
    group by email
    having count(email) > 1
)
update db_portal.tb_usuario set email = email || '(migracao)' || id
where email in (select * from tb);



ALTER TABLE db_portal.tb_usuario ADD CONSTRAINT email_unique UNIQUE (email);