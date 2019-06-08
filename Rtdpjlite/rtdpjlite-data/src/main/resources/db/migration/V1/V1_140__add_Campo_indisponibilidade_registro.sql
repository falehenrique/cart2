ALTER TABLE protocolo ADD COLUMN protocolo_cancelamento_indisponibilidade VARCHAR(250);
ALTER TABLE protocolo ADD COLUMN protocolo_indisponibilidade VARCHAR(250);

ALTER TABLE registro ADD COLUMN protocolo_cancelamento_indisponibilidade VARCHAR(250);
ALTER TABLE registro ADD COLUMN protocolo_indisponibilidade VARCHAR(250);