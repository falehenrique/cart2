
drop function rtdpj.retorna_registros(date,date) ;
CREATE OR REPLACE FUNCTION rtdpj.retorna_registros(date,date)
RETURNS varchar AS
$BODY$
declare
_dtI alias for $1;
_dtF alias for $2;
_protocolo varchar;
_resultado record;

BEGIN
	_protocolo := '';
	FOR _resultado IN
	select distinct(numero) from rtdpj.protocolo p
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

$BODY$
 LANGUAGE 'plpgsql';


CREATE OR REPLACE FUNCTION rtdpj.retorna_registros_pj(date,date)
RETURNS varchar AS
$BODY$
declare
_dtI alias for $1;
_dtF alias for $2;
_protocolo varchar;
_resultado record;

BEGIN
	_protocolo := '';
	FOR _resultado IN
	select distinct(numero) from rtdpj.protocolo p
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

$BODY$
 LANGUAGE 'plpgsql';

--exame e calculo td
CREATE OR REPLACE FUNCTION rtdpj.retorna_registros_exametd(date,date)
RETURNS varchar AS
$BODY$
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

$BODY$
 LANGUAGE 'plpgsql';

--exame e calculo pj
CREATE OR REPLACE FUNCTION rtdpj.retorna_registros_examepj(date,date)
RETURNS varchar AS
$BODY$
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

$BODY$
 LANGUAGE 'plpgsql';




DROP FUNCTION rtdpj.rel_portalextrajudicial(date, date);

DROP TYPE rel_portalextrajudicial_type;
CREATE TYPE rtdpj.rel_portalextrajudicial_type AS
   (protocolo integer,
    data_registro date,
    natureza character varying(250),
    subnatureza character varying(250),
    apresentante character varying(250),
    tipo_protocolo character varying(250),
    tipo_servico character varying(250),
    custas1 numeric(15,2),
    custas2 numeric(15,2),
    custas3 numeric(15,2),
    custas4 numeric(15,2),
    custas5 numeric(15,2),
    custas6 numeric(15,2),
    custas7 numeric(15,2),
    custas8 numeric(15,2),
    custas9 numeric(15,2),
    custas10 numeric(15,2));
ALTER TYPE rtdpj.rel_portalextrajudicial_type
  OWNER TO exmart;

-- DROP FUNCTION rel_portalextrajudicial(date, date);
-- Function: rel_portalextrajudicial(date, date)

-- DROP FUNCTION rel_portalextrajudicial(date, date);

CREATE OR REPLACE FUNCTION rtdpj.rel_portalextrajudicial(
    date,
    date)
  RETURNS SETOF rtdpj.rel_portalextrajudicial_type AS
$BODY$
declare
_dtI alias for $1;
_dtF alias for $2;
_conversor record;
_retorno rtdpj.rel_portalextrajudicial_type;
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
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION rtdpj.rel_portalextrajudicial(date, date)
  OWNER TO exmart;



drop function if exists rtdpj.rel_movimento_diario(date, date);
drop type if exists rtdpj.rel_movimentacao_type;

CREATE TYPE rtdpj.rel_movimentacao_type AS (protocolo integer,
						data_registro date,
						natureza varchar(250),
						apresentante varchar(250),
						subnatureza varchar(250),
						tipo_protocolo varchar(250),
						custas1 numeric(15,2),
						custas2 numeric(15,2),
						custas3 numeric(15,2),
						custas4 numeric(15,2),
						custas5 numeric(15,2),
						custas6 numeric(15,2),
						custas7 numeric(15,2),
						custas8 numeric(15,2),
						custas9 numeric(15,2),
						custas10 numeric(15,2)
					   );

create OR REPLACE function rtdpj.rel_movimento_diario(date, date) returns setof rtdpj.rel_movimentacao_type as
$lumera$
declare
  _dtI alias for $1;
  _dtF alias for $2;
  _conversor record;
  _retorno rtdpj.rel_movimentacao_type;
begin
  for _conversor in
	with a as (
	SELECT
	  p.numero,
	  p.dt_registro::DATE,
	  (select nome from rtdpj.natureza n where n.id = p.natureza_id) as natureza,
	  (select nome from rtdpj.sub_natureza n2 where n2.id = p.sub_natureza_id) as subnatureza,
	  p.tipo_protocolo_id,
	  p.apresentante,
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
	  FROM rtdpj.protocolo p
	WHERE p.dt_registro::DATE between _dtI and _dtF
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
	  a.dt_registro::DATE,
	  a.numero,
	  a.natureza,
	  a.subnatureza,
	  a.apresentante,
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
  	_retorno.protocolo      := _conversor.numero;
  	_retorno.data_registro  := _conversor.dt_registro;
	_retorno.natureza       := _conversor.natureza;
	_retorno.subnatureza    := _conversor.subnatureza;
	_retorno.tipo_protocolo := _conversor.tipo_protocolo;
	_retorno.apresentante   := _conversor.apresentante;
	_retorno.custas1        := _conversor.custas1;
	_retorno.custas2        := _conversor.custas2;
	_retorno.custas3        := _conversor.custas3;
	_retorno.custas4        := _conversor.custas4;
	_retorno.custas5        := _conversor.custas5;
	_retorno.custas6        := _conversor.custas6;
	_retorno.custas7        := _conversor.custas7;
	_retorno.custas8        := _conversor.custas8;
	_retorno.custas9        := _conversor.custas9;
	_retorno.custas10       := _conversor.custas10;
	return next _retorno;
  end loop;

  return;

end;
$lumera$
language plpgsql;
