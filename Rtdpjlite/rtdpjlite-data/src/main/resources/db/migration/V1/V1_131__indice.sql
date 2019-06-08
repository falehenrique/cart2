drop index rtdpj_protocolo_idx;

create index registro_idx_02 on registro(tp_especialidade, registro);
create index registro_idx_03 on registro(tp_especialidade, registro);
create index registro_idx_04 on registro using gin (objetos);

create index protocolo_idx_02 on protocolo (data_vencimento);
create index protocolo_idx_03 on protocolo (tipo_protocolo_id, data_protocolo);