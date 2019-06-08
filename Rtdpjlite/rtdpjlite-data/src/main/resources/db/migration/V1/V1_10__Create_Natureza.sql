create table natureza (id serial,
nome varchar(255),
dias_previsao int default 4,
dias_vencimento int default 6,
dias_reentrada int default 4,
natureza_tipo_id bigint
);
