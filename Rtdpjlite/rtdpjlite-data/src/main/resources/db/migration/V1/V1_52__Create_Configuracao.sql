CREATE TABLE configuracao(
  id serial,
  diretorio_upload varchar(250) NOT NULL,
  nome_cartorio VARCHAR(255) NOT NULL,
  cnpj_cartorio VARCHAR(18) NOT NULL,
  cep_cartorio VARCHAR(9) NOT NULL,
  cidade_cartorio VARCHAR(255) NOT NULL,
  sg_cartorio VARCHAR(2) NOT NULL,
  bairro_cartorio VARCHAR(255) NOT NULL,
  logradouro_cartorio VARCHAR(255) NOT NULL,
  numero_logradouro_cartorio VARCHAR(255) NOT NULL,
  complemento_logradouro_cartorio VARCHAR(255),
  carimbo_certidao TEXT,
  texto_explicativo_assinatura text,
  url_qrcode VARCHAR(255)
);