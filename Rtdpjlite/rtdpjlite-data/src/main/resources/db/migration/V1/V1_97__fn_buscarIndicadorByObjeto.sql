CREATE TYPE indicador_pessoal_vo  AS (
   id bigint,
   numero_registro bigint,
   data_registro timestamp without time zone,
   nr_pasta_pj varchar,
   nome varchar,
   objetos jsonb,
   parte jsonb
 );


 CREATE OR REPLACE FUNCTION buscarIndicadorByObjeto(id bigint, nome varchar, valor varchar) RETURNS SETOF indicador_pessoal_vo AS $$
 DECLARE
  _id ALIAS FOR $1;
  _nome ALIAS FOR $2;
  _valor ALIAS FOR $3;
  _where varchar;
 BEGIN
   _where := '{"id":'|| _id ||' , "atributos": [{"nome": "'|| _nome ||'", "valor" : "'|| _valor || '"}]}';
  RETURN QUERY select r.id::bigint, numero_registro,data_registro as dt_registro ,nr_pasta_pj as pasta, n.nome, value as objetos, (select cast ('{"nome" : "' || ip.nome ||'", "documento": "' || cpf_cnpj || '", "qualidade": "'|| q.nome || '"}' as jsonb) as parte from indicador_pessoal ip  JOIN qualidade q ON ip.qualidade_id = q.id WHERE registro_id = r.id limit 1) as parte  FROM registro r cross join lateral jsonb_array_elements(objetos)
 JOIN natureza n ON r.natureza_id = n.id where value @> _where::jsonb;
 END $$ LANGUAGE plpgsql;
