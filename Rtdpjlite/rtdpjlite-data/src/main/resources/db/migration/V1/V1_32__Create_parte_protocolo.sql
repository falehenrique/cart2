create table parte_protocolo (
    id serial primary key,
    parte_id bigint,
    protocolo_id bigint,
    qualidade_id bigint,
    vl_participacao numeric(13,2),
    qtd_cotas integer
);