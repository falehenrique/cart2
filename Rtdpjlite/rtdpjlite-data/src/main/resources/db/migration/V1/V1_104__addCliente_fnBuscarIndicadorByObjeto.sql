DROP TYPE indicador_pessoal_vo CASCADE;

CREATE TYPE indicador_pessoal_vo  AS (
   id bigint,
   registro varchar,
   numero_registro bigint,
   data_registro timestamp without time zone,
   nr_pasta_pj varchar,
   nome varchar,
   objetos jsonb,
   parte jsonb
 );

CREATE OR REPLACE FUNCTION buscarIndicadorByObjeto(id bigint, nome varchar, valor varchar, idCliente bigint) RETURNS SETOF indicador_pessoal_vo AS $$
 DECLARE
  _id ALIAS FOR $1;
  _nome ALIAS FOR $2;
  _valor ALIAS FOR $3;
  _idCliente ALIAS FOR $4;
  _where varchar;
  _whereIdCliente varchar;
 BEGIN
    _where := '{"id":'|| _id ||' , "atributos": [{"nome": "'|| _nome ||'", "valor" : "'|| _valor || '"}]}';
    IF _idCliente > 0 THEN
    RETURN QUERY select r.id::bigint, r.registro as registro, numero_registro,data_registro as dt_registro ,nr_pasta_pj as pasta, n.nome, value as objetos, (select cast ('{"nome" : "' || ip.nome ||'", "documento": "' || cpf_cnpj || '", "qualidade": "'|| q.nome || '"}' as jsonb) as parte from indicador_pessoal ip  JOIN qualidade q ON ip.qualidade_id = q.id WHERE registro_id = r.id limit 1) as parte  FROM registro r cross join lateral jsonb_array_elements(objetos)
 JOIN natureza n ON r.natureza_id = n.id where value @> _where::jsonb  AND r.cliente_id =  _idCliente;
    ELSE
    RETURN QUERY select r.id::bigint, r.registro as registro, numero_registro,data_registro as dt_registro ,nr_pasta_pj as pasta, n.nome, value as objetos, (select cast ('{"nome" : "' || ip.nome ||'", "documento": "' || cpf_cnpj || '", "qualidade": "'|| q.nome || '"}' as jsonb) as parte from indicador_pessoal ip  JOIN qualidade q ON ip.qualidade_id = q.id WHERE registro_id = r.id limit 1) as parte  FROM registro r cross join lateral jsonb_array_elements(objetos)
 JOIN natureza n ON r.natureza_id = n.id where value @> _where::jsonb ;
    END IF;
 END $$ LANGUAGE plpgsql;

 select * from buscarIndicadorByObjeto(3,'TIPO','TIPO',2)