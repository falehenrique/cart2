create table parte (
    id serial,
    cpf_cnpj varchar(255),
    nome varchar(255),

    tipo_documento_id bigint,

    nr_documento varchar(255),
    nm_orgao_expedidor_documento varchar(255),
    dt_emissao_documento DATE,

    telefone varchar(255),
    email varchar(255),
    nacionalidade_id bigint,
    estado_civil_id bigint,
    regime_bens_id bigint,
    profissao_id bigint,
    cep varchar(255),
    tipo_logradouro_id bigint,
    endereco varchar(255),
    numero varchar(255),
    complemento varchar(255),
    bairro varchar(255),
    cidade_id bigint,
    dt_cadastro timestamp,
    uuid_conjuge varchar(255),
    estado_id integer
);
