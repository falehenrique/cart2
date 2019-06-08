create index parte_documento_idx on parte(cpf_cnpj);
create index parte_conjuge_idx on parte(uuid_conjuge, id);
create index parte_id_idx on parte(id DESC);