CREATE OR REPLACE FUNCTION findProtocoloByObjeto(parametros json) RETURNS setof protocolo AS $$
DECLARE
 _i json;
 _where varchar;
BEGIN
_where = ' 1=1 ';
  FOR _i IN SELECT * FROM json_array_elements($1)
  LOOP
  _where := _where || ' and objetos @> ''[{"id": ' ||  cast(_i->>'codObjeto' as varchar) || ' , "atributos": [{"nome": "' ||cast(_i->>'nomeCampo' as varchar) || '", "valor" : "' || cast(_i->>'valorCampo' as varchar) ||  '"}]}]'' ';
  	--raise notice 'Json %', _i->>'codObjeto';
  END LOOP;

  RETURN QUERY EXECUTE 'select * from protocolo where ' || _where ;--numero = 4699;
END;
$$ Language plpgsql;