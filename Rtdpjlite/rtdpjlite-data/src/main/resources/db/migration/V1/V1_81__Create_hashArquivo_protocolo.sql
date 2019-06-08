CREATE TABLE protocolo_arquivo_hash (
   id SERIAL NOT NULL PRIMARY KEY,
   protocolo_id BIGINT NOT NULL,
   protocolo_servico_id BIGINT,
   hash VARCHAR(250) NOT NULL
);