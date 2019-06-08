create table status_protocolo (
	id serial primary key,
	protocolo_id bigint,
	status_id bigint,
	data timestamp,
	conteudo jsonb default '{}',
	usuario_id bigint
);
