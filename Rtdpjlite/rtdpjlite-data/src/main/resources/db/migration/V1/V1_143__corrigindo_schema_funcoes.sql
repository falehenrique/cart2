CREATE OR REPLACE FUNCTION rtdpj.buscarindicadorbyobjeto(
	id bigint,
	nome character varying,
	valor character varying,
	idcliente bigint)
RETURNS SETOF rtdpj.indicador_pessoal_vo
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE
    ROWS 1000
AS $BODY$

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
    RETURN QUERY select r.id::bigint, r.registro as registro, numero_registro,data_registro as dt_registro ,nr_pasta_pj as pasta, n.nome, value as objetos, (select cast ('{"nome" : "' || ip.nome ||'", "documento": "' || cpf_cnpj || '", "qualidade": "'|| q.nome || '"}' as jsonb) as parte from rtdpj.indicador_pessoal ip  JOIN rtdpj.qualidade q ON ip.qualidade_id = q.id WHERE registro_id = r.id limit 1) as parte  FROM rtdpj.registro r cross join lateral jsonb_array_elements(objetos)
 JOIN rtdpj.natureza n ON r.natureza_id = n.id where value @> _where::jsonb  AND r.cliente_id =  _idCliente;
    ELSE
    RETURN QUERY select r.id::bigint, r.registro as registro, numero_registro,data_registro as dt_registro ,nr_pasta_pj as pasta, n.nome, value as objetos, (select cast ('{"nome" : "' || ip.nome ||'", "documento": "' || cpf_cnpj || '", "qualidade": "'|| q.nome || '"}' as jsonb) as parte from rtdpj.indicador_pessoal ip  JOIN rtdpj.qualidade q ON ip.qualidade_id = q.id WHERE registro_id = r.id limit 1) as parte  FROM rtdpj.registro r cross join lateral jsonb_array_elements(objetos)
 JOIN rtdpj.natureza n ON r.natureza_id = n.id where value @> _where::jsonb ;
    END IF;
 END
$BODY$;




CREATE OR REPLACE FUNCTION rtdpj.findprotocolobyobjeto(
  parametros json)
RETURNS SETOF rtdpj.protocolo
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE
    ROWS 1000
AS $BODY$

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

  RETURN QUERY EXECUTE 'select * from rtdpj.protocolo where ' || _where ;--numero = 4699;
END;

$BODY$;



CREATE OR REPLACE FUNCTION rtdpj.findregistrobyobjeto(
  parametros json)
RETURNS SETOF rtdpj.registro
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE
    ROWS 1000
AS $BODY$

DECLARE
 _i json;
 _where varchar;
BEGIN
_where = ' 1=1 ';
  FOR _i IN SELECT * FROM json_array_elements($1)
  LOOP
  _where := _where || ' and objetos @> ''[{"id": ' ||  cast(_i->>'codObjeto' as varchar) || ' , "atributos": [{"nome": "' ||cast(_i->>'nomeCampo' as varchar) || '", "valor" : "' || cast(_i->>'valorCampo' as varchar) ||  '"}]}]'' ';
  END LOOP;
  RETURN QUERY EXECUTE 'select * rtdpj.from registro where ' || _where ;--numero = 4699;
END;

$BODY$;



CREATE OR REPLACE FUNCTION rtdpj.fn_timeline(
  numero character varying,
  tipo character varying)
RETURNS SETOF rtdpj.card_timeline
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE
    ROWS 1000
AS $BODY$

  DECLARE
    _registro_referencia varchar;
    _r card_timeline%rowtype;
  BEGIN
    _registro_referencia := (select registro_referencia from rtdpj.registro where registro.registro = $1 and tp_especialidade = $2);

  LOOP
        raise notice 'referencia %', _registro_referencia;
        EXIT WHEN _registro_referencia is null;
        select INTO _r p.id, p.numero_registro, n.nome, 'escrevente', p.data_registro, p.situacao_atual, registro_referencia, p.numero_registro, p.tp_especialidade
        FROM rtdpj.registro p
        JOIN rtdpj.natureza n ON p.natureza_id = n.id
    where registro = _registro_referencia;

    _registro_referencia:= _r.registro_referencia;
    RETURN NEXT _r;
    END LOOP;
  END;

$BODY$;






CREATE OR REPLACE FUNCTION rtdpj.rel_movimento_diario(
  date,
  date)
RETURNS SETOF rtdpj.rel_movimentacao_type
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE
    ROWS 1000
AS $BODY$

declare
_dtI alias for $1;
_dtF alias for $2;
_conversor record;
_retorno rel_movimentacao_type;
begin
for _conversor in
with a as (
SELECT
p.numero,
p.dt_registro,
n.nome as natureza,
sn.nome as subnatureza,
p.tipo_protocolo_id,
--sp.data data_certidao,
(jsonb_array_elements(p.servicos ) ->> 'custas1')::float AS custasT1,
(jsonb_array_elements(p.servicos ) ->> 'custas2')::float AS custasT2,
(jsonb_array_elements(p.servicos ) ->> 'custas3')::float AS custasT3,
(jsonb_array_elements(p.servicos ) ->> 'custas4')::float AS custasT4,
(jsonb_array_elements(p.servicos ) ->> 'custas5')::float AS custasT5,
(jsonb_array_elements(p.servicos ) ->> 'custas6')::float AS custasT6,
(jsonb_array_elements(p.servicos ) ->> 'custas7')::float AS custasT7,
(jsonb_array_elements(p.servicos ) ->> 'custas8')::float AS custasT8,
(jsonb_array_elements(p.servicos ) ->> 'custas9')::float AS custasT9,
(jsonb_array_elements(p.servicos ) ->> 'custas10')::float AS custasT10
FROM
rtdpj.protocolo p left join
--status_protocolo sp on sp.protocolo_id = p.id and status_id = 8 join
rtdpj.natureza n on n.id = p.natureza_id join
rtdpj.sub_natureza sn on sn.id = p.sub_natureza_id
WHERE
dt_registro between _dtI and _dtF --or
--sp.data between _dtI and _dtF
)
select
distinct
case
when a.tipo_protocolo_id = 10 then 'Exame e Cálculo TD'
when a.tipo_protocolo_id = 20 then 'Registro TD'
when a.tipo_protocolo_id = 30 then 'Certidão TD'
when a.tipo_protocolo_id = 40 then 'Exame e Cálculo PJ'
when a.tipo_protocolo_id = 50 then 'Registro PJ'
when a.tipo_protocolo_id = 60 then 'Certidão PJ'
end as tipo_protocolo,
a.numero,
a.dt_registro,
a.numero,
a.natureza,
a.subnatureza,
sum(custasT1) over (partition by numero) as custas1,

sum(custasT2) over (partition by numero) as custas2,
sum(custasT3) over (partition by numero) as custas3,
sum(custasT4) over (partition by numero) as custas4,
sum(custasT5) over (partition by numero) as custas5,
sum(custasT6) over (partition by numero) as custas6,
sum(custasT7) over (partition by numero) as custas7,
sum(custasT8) over (partition by numero) as custas8,
sum(custasT9) over (partition by numero) as custas9,
sum(custasT10) over (partition by numero) as custas10
from a
loop
_retorno.protocolo := _conversor.numero;
_retorno.data_registro := _conversor.dt_registro;
_retorno.natureza := _conversor.natureza;
_retorno.subnatureza := _conversor.subnatureza;
_retorno.tipo_protocolo := _conversor.tipo_protocolo;
_retorno.custas1 := _conversor.custas1;
_retorno.custas2 := _conversor.custas2;
_retorno.custas3 := _conversor.custas3;
_retorno.custas4 := _conversor.custas4;
_retorno.custas5 := _conversor.custas5;
_retorno.custas6 := _conversor.custas6;
_retorno.custas7 := _conversor.custas7;
_retorno.custas8 := _conversor.custas8;
_retorno.custas9 := _conversor.custas9;
_retorno.custas10 := _conversor.custas10;
return next _retorno;
end loop;
end;

$BODY$;








CREATE OR REPLACE FUNCTION rtdpj.rel_portalextrajudicial(
  date,
  date)
RETURNS SETOF rtdpj.rel_portalextrajudicial_type
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE
    ROWS 1000
AS $BODY$

declare
_dtI alias for $1;
_dtF alias for $2;
_conversor record;
_retorno rel_portalextrajudicial_type;
begin
for _conversor in
with a as (
SELECT
p.numero,
p.dt_registro,
p.apresentante,
n.nome as natureza,
sn.nome as subnatureza,
p.tipo_protocolo_id,
--sp.data data_certidao,
(jsonb_array_elements(p.servicos ) ->> 'nmServico')::character varying(250) AS tipo_servico,
(jsonb_array_elements(p.servicos ) ->> 'custas1')::float AS custasT1,
(jsonb_array_elements(p.servicos ) ->> 'custas2')::float AS custasT2,
(jsonb_array_elements(p.servicos ) ->> 'custas3')::float AS custasT3,
(jsonb_array_elements(p.servicos ) ->> 'custas4')::float AS custasT4,
(jsonb_array_elements(p.servicos ) ->> 'custas5')::float AS custasT5,
(jsonb_array_elements(p.servicos ) ->> 'custas6')::float AS custasT6,
(jsonb_array_elements(p.servicos ) ->> 'custas7')::float AS custasT7,
(jsonb_array_elements(p.servicos ) ->> 'custas8')::float AS custasT8,
(jsonb_array_elements(p.servicos ) ->> 'custas9')::float AS custasT9,
(jsonb_array_elements(p.servicos ) ->> 'custas10')::float AS custasT10
FROM
rtdpj.protocolo p left join
--status_protocolo sp on sp.protocolo_id = p.id and status_id = 8 join
rtdpj.natureza n on n.id = p.natureza_id join
rtdpj.sub_natureza sn on sn.id = p.sub_natureza_id
WHERE
dt_registro::date between _dtI and _dtF --or
--sp.data between _dtI and _dtF
)
select
distinct
case
when a.tipo_protocolo_id = 10 then 'Exame e Cálculo TD'
when a.tipo_protocolo_id = 20 then 'Registro TD'
when a.tipo_protocolo_id = 30 then 'Certidão TD'
when a.tipo_protocolo_id = 40 then 'Exame e Cálculo PJ'
when a.tipo_protocolo_id = 50 then 'Registro PJ'
when a.tipo_protocolo_id = 60 then 'Certidão PJ'
end as tipo_protocolo,
a.numero,
a.dt_registro,
a.numero,
a.apresentante,
a.natureza,
a.subnatureza,
a.tipo_servico,
sum(custasT1) over (partition by numero) as custas1,
sum(custasT2) over (partition by numero) as custas2,
sum(custasT3) over (partition by numero) as custas3,
sum(custasT4) over (partition by numero) as custas4,
sum(custasT5) over (partition by numero) as custas5,
sum(custasT6) over (partition by numero) as custas6,
sum(custasT7) over (partition by numero) as custas7,
sum(custasT8) over (partition by numero) as custas8,
sum(custasT9) over (partition by numero) as custas9,
sum(custasT10) over (partition by numero) as custas10
from a
loop
_retorno.protocolo := _conversor.numero;
_retorno.data_registro := _conversor.dt_registro;
_retorno.natureza := _conversor.natureza;
_retorno.subnatureza := _conversor.subnatureza;
_retorno.apresentante := _conversor.apresentante;
_retorno.tipo_protocolo := _conversor.tipo_protocolo;
_retorno.tipo_servico := _conversor.tipo_servico;
_retorno.custas1 := _conversor.custas1;
_retorno.custas2 := _conversor.custas2;
_retorno.custas3 := _conversor.custas3;
_retorno.custas4 := _conversor.custas4;
_retorno.custas5 := _conversor.custas5;
_retorno.custas6 := _conversor.custas6;
_retorno.custas7 := _conversor.custas7;
_retorno.custas8 := _conversor.custas8;
_retorno.custas9 := _conversor.custas9;
_retorno.custas10 := _conversor.custas10;
return next _retorno;
end loop;
end;

$BODY$;





CREATE OR REPLACE FUNCTION rtdpj.retorna_registros(
  date,
  date)
RETURNS character varying
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE
AS $BODY$

declare
_dtI alias for $1;
_dtF alias for $2;
_protocolo varchar;
_resultado record;

BEGIN
  _protocolo := '';
  FOR _resultado IN
  select numero from rtdpj.protocolo p
  JOIN rtdpj.status_protocolo sp ON sp.protocolo_id = p.id where tipo_protocolo_id = 20 and
  (data_protocolo::date between _dtI and _dtF
  or
  dt_registro::date between _dtI and _dtF)
  and sp.status_id IN (1,2,6,7,10,11,12)
  order by numero

  LOOP
    _protocolo := _protocolo || ', ' || _resultado.numero ;

  END LOOP;

  RETURN SUBSTRING(_protocolo,2,LENGTH(_protocolo));

END;

$BODY$;




CREATE OR REPLACE FUNCTION rtdpj.retorna_registros_examepj(
  date,
  date)
RETURNS character varying
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE
AS $BODY$

declare
_dtI alias for $1;
_dtF alias for $2;
_protocolo varchar;
_resultado record;

BEGIN
  _protocolo := '';
  FOR _resultado IN
  select numero from rtdpj.protocolo p
  JOIN rtdpj.status_protocolo sp ON sp.protocolo_id = p.id where tipo_protocolo_id = 40 and
  (data_protocolo::date between _dtI and _dtF
  or
  dt_registro::date between _dtI and _dtF)
  and sp.status_id IN (1,2,6,7,10,11,12)
  order by numero

  LOOP
    _protocolo := _protocolo || ', ' || _resultado.numero ;

  END LOOP;

  RETURN SUBSTRING(_protocolo,2,LENGTH(_protocolo));

END;

$BODY$;




CREATE OR REPLACE FUNCTION rtdpj.retorna_registros_exametd(
  date,
  date)
RETURNS character varying
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE
AS $BODY$

declare
_dtI alias for $1;
_dtF alias for $2;
_protocolo varchar;
_resultado record;

BEGIN
  _protocolo := '';
  FOR _resultado IN
  select numero from rtdpj.protocolo p
  JOIN rtdpj.status_protocolo sp ON sp.protocolo_id = p.id where tipo_protocolo_id = 10 and
  (data_protocolo::date between _dtI and _dtF
  or
  dt_registro::date between _dtI and _dtF)
  and sp.status_id IN (1,2,6,7,10,11,12)
  order by numero

  LOOP
    _protocolo := _protocolo || ', ' || _resultado.numero ;

  END LOOP;

  RETURN SUBSTRING(_protocolo,2,LENGTH(_protocolo));

END;

$BODY$;





CREATE OR REPLACE FUNCTION rtdpj.retorna_registros_pj(
  date,
  date)
RETURNS character varying
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE
AS $BODY$

declare
_dtI alias for $1;
_dtF alias for $2;
_protocolo varchar;
_resultado record;

BEGIN
  _protocolo := '';
  FOR _resultado IN
  select numero from rtdpj.protocolo p
  JOIN rtdpj.status_protocolo sp ON sp.protocolo_id = p.id where tipo_protocolo_id = 50 and
  (data_protocolo::date between _dtI and _dtF
  or
  dt_registro::date between _dtI and _dtF)
  and sp.status_id IN (1,2,6,7,10,11,12)
  order by numero

  LOOP
    _protocolo := _protocolo || ', ' || _resultado.numero ;

  END LOOP;

  RETURN SUBSTRING(_protocolo,2,LENGTH(_protocolo));

END;

$BODY$;




