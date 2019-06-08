INSERT INTO rtdpj.status(
	nome, automatico, bloquear_protocolo, ic_sistema)
	VALUES ( 'REGISTRAR EM LOTE', false, false, false);
UPDATE rtdpj.status SET ic_sistema = false WHERE nome = 'PROTOCOLO INCOMPLETO';
