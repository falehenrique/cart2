create table servicos_protocolo (
	id serial, 
	protocolo_id bigint, 
	servico_id bigint, 
	valor decimal(12,2)  not null,
	quantidade int, 
	forma_calculo_id bigint not null,
	parte_id bigint,
	registro bigint, 
	pasta_pj varchar(255),
	forma_entrega_id bigint,
	observacoes text,
	servicos_protocolo_custas_id bigint
);
