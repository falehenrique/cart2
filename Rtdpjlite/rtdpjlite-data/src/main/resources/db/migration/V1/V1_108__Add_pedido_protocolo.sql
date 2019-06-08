ALTER TABLE protocolo ADD COLUMN pedido_id bigint;

INSERT INTO status (nome, bloquear_protocolo, automatico) VALUES ('PROTOCOLO INCOMPLETO',false, true);