ALTER TABLE cartorio_parceiro ADD COLUMN cidade_id BIGINT REFERENCES cidade(id);
ALTER TABLE cartorio_parceiro ADD COLUMN qtd_certidao_fisica INTEGER;

ALTER TABLE cartorio_parceiro ADD COLUMN cnpj VARCHAR(250);
ALTER TABLE cartorio_parceiro ADD COLUMN oficial VARCHAR(250);
ALTER TABLE cartorio_parceiro ADD COLUMN oficial_cpf VARCHAR(250);
ALTER TABLE cartorio_parceiro ADD COLUMN cns VARCHAR(250) NOT NULL UNIQUE DEFAULT random()::text;
ALTER TABLE cartorio_parceiro ADD COLUMN comarca VARCHAR(250);


ALTER TABLE configuracao ADD COLUMN modelo_certidao_portal TEXT DEFAULT '<body style="font-size: 10pt;"><p style="text-align: center;">ESTADO DE {{CARTORIO_PARCEIRO.estado.nome}}<br />MUNIC&Iacute;PIO E COMARCA DE {{CARTORIO_PARCEIRO.cidade}}<br /><strong>{{CARTORIO_PARCEIRO.nome}}</strong></p><p style="text-align: center;">{{CARTORIO_PARCEIRO.oficial}}<br />Oficial Registrador</p><p style="text-align: center;"><strong>CERTID&Atilde;O DE REGISTRO</strong></p><p style="text-align: justify;">Certifico, a requerimento da parte interessada, que na data de {{DT_PROTOCOLO_CLIENTE}}, foi protocolado sob n<sup>o</sup> {{NR_PROTOCOLO_CLIENTE}} O REGISTRO DE T&Iacute;TULOS E DOCUMENTOS, sendo registrado na data {{DATA_ATUAL}}, sob n<sup>o</sup> _________________, o documento com os seguintes dados:</p><p style="text-align: justify;">Apresentante: {{NOME_CARTORIO}}</p><p style="text-align: justify;">Natureza do t&iacute;tulo: {{NATUREZA}} n<sup>o</sup> {{DOCUMENTO_NUMERO}} EMITIDO EM {{DOCUMENTO_DATA}} no valor de R$ {{DOCUMENTO_VALOR}}</p><p style="text-align: justify;">Indicadores:{% for parte in PARTES %}{{parte.nome}}, {{parte.qualidade}}; {% endfor %}</p><p style="text-align: justify;">Caracteristicas:&nbsp;</p><p style="text-align: center;">{{CARTORIO_PARCEIRO.cidade}} -&nbsp; {{CARTORIO_PARCEIRO.estado}}, {{DATA_ATUAL_EXTENSO}}</p><p style="text-align: left;"><strong>Emolumentos:</strong></p></body>';