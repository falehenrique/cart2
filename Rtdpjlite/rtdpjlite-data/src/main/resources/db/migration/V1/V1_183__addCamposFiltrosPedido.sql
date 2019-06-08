ALTER TABLE db_portal.tb_pedido ADD COLUMN status_atual_cliente VARCHAR(255) NOT NULL DEFAULT 'AGUARDE O CARTÓRIO';
ALTER TABLE db_portal.tb_pedido ADD COLUMN ultima_atualizacao TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now();

ALTER TABLE db_portal.tb_pedido_protocolo ADD COLUMN status_atual_parceiro VARCHAR(255) NOT NULL DEFAULT 'AGUARDANDO PROTOCOLAR';
ALTER TABLE db_portal.tb_pedido_protocolo ADD COLUMN ultima_atualizacao TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now();