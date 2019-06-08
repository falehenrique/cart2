ALTER TABLE protocolo ADD COLUMN responsavel_id BIGINT REFERENCES db_portal.tb_usuario(id);
--ALTER TABLE protocolo ADD COLUMN ic_averbacao_pedido BOOLEAN DEFAULT FALSE;