CREATE INDEX protocolo_data_protocolo_idx ON protocolo(data_protocolo);
CREATE INDEX protocolo_cliente_id_idx ON protocolo(cliente_id);
CREATE INDEX cliente_id_idx ON cliente(id);
CREATE INDEX protocolo_natureza_id_idx ON protocolo(natureza_id);
CREATE INDEX status_protocolo_protocolo_id_idx ON status_protocolo(protocolo_id);
CREATE INDEX status_id_idx ON status(id);