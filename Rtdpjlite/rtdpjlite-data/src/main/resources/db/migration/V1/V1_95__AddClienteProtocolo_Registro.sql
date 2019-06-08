ALTER TABLE protocolo ADD COLUMN cliente_id BIGINT REFERENCES cliente(id);
ALTER TABLE registro ADD COLUMN cliente_id BIGINT REFERENCES cliente(id);