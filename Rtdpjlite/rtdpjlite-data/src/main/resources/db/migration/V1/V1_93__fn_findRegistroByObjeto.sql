CREATE OR REPLACE FUNCTION findRegistroByObjeto(parametros json) RETURNS setof registro AS $$
DECLARE
 _i json;
 _where varchar;
BEGIN
_where = ' 1=1 ';
  FOR _i IN SELECT * FROM json_array_elements($1)
  LOOP
  _where := _where || ' and objetos @> ''[{"id": ' ||  cast(_i->>'codObjeto' as varchar) || ' , "atributos": [{"nome": "' ||cast(_i->>'nomeCampo' as varchar) || '", "valor" : "' || cast(_i->>'valorCampo' as varchar) ||  '"}]}]'' ';
  END LOOP;
  RETURN QUERY EXECUTE 'select * from registro where ' || _where ;--numero = 4699;
END;
$$ Language plpgsql;