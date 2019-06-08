-- Function: rtdpj.rel_movimento_diario(date, date)

-- DROP FUNCTION rtdpj.rel_movimento_diario(date, date);

drop function if exists rtdpj.rel_movimento_diario_faturados_cliente (date,date,integer);
drop type if exists rtdpj.rel_movimentacao_faturados_type;

CREATE TYPE rtdpj.rel_movimentacao_faturados_type AS (protocolo integer,
						registro integer,
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
						custas10 numeric(15,2),
						tabelaCusta varchar(250),
						tipo_protocolo_id varchar(250),
						cliente_faturado varchar(250),
						cliente_id integer
					   );

-- Function: rtdpj.rel_movimento_diario_faturados_cliente(date, date, integer)

-- DROP FUNCTION rtdpj.rel_movimento_diario_faturados_cliente(date, date, integer);

CREATE OR REPLACE FUNCTION rtdpj.rel_movimento_diario_faturados_cliente(
    date,
    date,
    integer)
  RETURNS SETOF rtdpj.rel_movimentacao_faturados_type AS
$BODY$

declare
  _dtI alias for $1;
  _dtF alias for $2;
  _cliente_id alias for $3;
  _conversor record;
  _retorno rtdpj.rel_movimentacao_faturados_type;
begin

IF _cliente_id is not null THEN

  for _conversor in
	with a as (
	SELECT
	  p.numero,
	  p.dt_registro::DATE,
	  p.numero_registro,
	  (select nome from rtdpj.natureza n where n.id = p.natureza_id) as natureza,
	  (select nome from rtdpj.sub_natureza n2 where n2.id = p.sub_natureza_id) as subnatureza,
	  (select nome from rtdpj.cliente c where c.id = p.cliente_id ) as cliente_faturado,
	  p.tipo_protocolo_id,
	  p.apresentante,
	  p.cliente_id,
	 (jsonb_array_elements(p.servicos ) ->> 'custas1')::float AS custasT1,
	 (jsonb_array_elements(p.servicos ) ->> 'custas2')::float AS custasT2,
	 (jsonb_array_elements(p.servicos ) ->> 'custas3')::float AS custasT3,
	 (jsonb_array_elements(p.servicos ) ->> 'custas4')::float AS custasT4,
	 (jsonb_array_elements(p.servicos ) ->> 'custas5')::float AS custasT5,
	 (jsonb_array_elements(p.servicos ) ->> 'custas6')::float AS custasT6,
	 (jsonb_array_elements(p.servicos ) ->> 'custas7')::float AS custasT7,
	 (jsonb_array_elements(p.servicos ) ->> 'custas8')::float AS custasT8,
	 (jsonb_array_elements(p.servicos ) ->> 'custas9')::float AS custasT9,
	 (jsonb_array_elements(p.servicos ) ->> 'custas10')::float AS custasT10,
	 (jsonb_array_elements(p.servicos ) ->> 'tabelaCusta')::varchar AS tabelaCusta

	  FROM rtdpj.protocolo p
	WHERE
		p.dt_registro::DATE between _dtI and _dtF and
		p.tipo_protocolo_id NOT IN (10,40) and
		p.cliente_id = _cliente_id

	)

	select
	distinct
	  case
		when a.tipo_protocolo_id = 20 then 'Registro TD'
		when a.tipo_protocolo_id = 30 then 'Certidão'
		when a.tipo_protocolo_id = 50 then 'Registro PJ'
		when a.tipo_protocolo_id = 60 then 'Certidão'
	 end as tipo_protocolo,
	  a.numero,
	  a.dt_registro::DATE,
	  case
		when a.tipo_protocolo_id = 30 then a.numero::varchar
		when a.tipo_protocolo_id = 60 then a.numero::varchar
	  else a.numero_registro
	  end as numero_registro,
	  a.natureza,
	  a.subnatureza,
	  a.apresentante,
	  a.cliente_faturado,
	 sum(custasT1) over (partition by numero) as custas1,
	  sum(custasT2) over (partition by numero) as custas2,
	  sum(custasT3) over (partition by numero) as custas3,
	  sum(custasT4) over (partition by numero) as custas4,
	  sum(custasT5) over (partition by numero) as custas5,
	  sum(custasT6) over (partition by numero) as custas6,
	  sum(custasT7) over (partition by numero) as custas7,
	  sum(custasT8) over (partition by numero) as custas8,
	  sum(custasT9) over (partition by numero) as custas9,
	  sum(custasT10) over (partition by numero) as custas10,
	--  a.tabelaCusta,
	   case
		when a.tipo_protocolo_id = 30 then 'TD'
		when a.tipo_protocolo_id = 60 then 'PJ'
		when a.tipo_protocolo_id = 20 then 'TD'
		when a.tipo_protocolo_id = 50 then 'PJ'
	  end as tipo_protocolo_id,
	  a.cliente_id
	from a
 loop
  	_retorno.protocolo      := _conversor.numero;
  	_retorno.data_registro  := _conversor.dt_registro;
	_retorno.registro	 := _conversor.numero_registro;
	_retorno.natureza       := _conversor.natureza;
	_retorno.subnatureza    := _conversor.subnatureza;
	_retorno.cliente_faturado := _conversor.cliente_faturado;
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
	--_retorno.tabelaCusta    := _conversor.tabelaCusta;
	_retorno.tipo_protocolo_id := _conversor.tipo_protocolo_id;
	_retorno.cliente_id 	:= _conversor.cliente_id;
	return next _retorno;
  end loop;

ELSE

for _conversor in
	with a as (
	SELECT
	  p.numero,
	  p.dt_registro::DATE,
	  p.numero_registro,
	  (select nome from rtdpj.natureza n where n.id = p.natureza_id) as natureza,
	  (select nome from rtdpj.sub_natureza n2 where n2.id = p.sub_natureza_id) as subnatureza,
	  (select nome from rtdpj.cliente c where c.id = p.cliente_id ) as cliente_faturado,
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
	 (jsonb_array_elements(p.servicos ) ->> 'custas10')::float AS custasT10,
	 (jsonb_array_elements(p.servicos ) ->> 'tabelaCusta')::varchar AS tabelaCusta,
	 p.cliente_id

	  FROM rtdpj.protocolo p
	WHERE
		p.dt_registro::DATE between _dtI and _dtF and
		p.tipo_protocolo_id NOT IN (10,40)
	)

	select
	distinct
	  case
		when a.tipo_protocolo_id = 20 then 'Registro TD'
		when a.tipo_protocolo_id = 30 then 'Certidão'
		when a.tipo_protocolo_id = 50 then 'Registro PJ'
		when a.tipo_protocolo_id = 60 then 'Certidão'
	 end as tipo_protocolo,
	  a.numero,
	  a.dt_registro::DATE,
	  case
		when a.tipo_protocolo_id = 30 then a.numero::varchar
		when a.tipo_protocolo_id = 60 then a.numero::varchar
	  else a.numero_registro
	  end as numero_registro,
	  a.natureza,
	  a.subnatureza,
	  a.apresentante,
	  a.cliente_faturado,
	  a.cliente_id,
	 sum(custasT1) over (partition by numero) as custas1,
	  sum(custasT2) over (partition by numero) as custas2,
	  sum(custasT3) over (partition by numero) as custas3,
	  sum(custasT4) over (partition by numero) as custas4,
	  sum(custasT5) over (partition by numero) as custas5,
	  sum(custasT6) over (partition by numero) as custas6,
	  sum(custasT7) over (partition by numero) as custas7,
	  sum(custasT8) over (partition by numero) as custas8,
	  sum(custasT9) over (partition by numero) as custas9,
	  sum(custasT10) over (partition by numero) as custas10,
	--  a.tabelaCusta,
	   case
		when a.tipo_protocolo_id = 30 then 'TD'
		when a.tipo_protocolo_id = 60 then 'PJ'
		when a.tipo_protocolo_id = 20 then 'TD'
		when a.tipo_protocolo_id = 50 then 'PJ'
	  end as tipo_protocolo_id
	from a
 loop
  	_retorno.protocolo      := _conversor.numero;
  	_retorno.data_registro  := _conversor.dt_registro;
	_retorno.registro	 := _conversor.numero_registro;
	_retorno.natureza       := _conversor.natureza;
	_retorno.subnatureza    := _conversor.subnatureza;
	_retorno.cliente_faturado := _conversor.cliente_faturado;
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
	--_retorno.tabelaCusta    := _conversor.tabelaCusta;
	_retorno.tipo_protocolo_id := _conversor.tipo_protocolo_id;
	_retorno.cliente_id 	:= _conversor.cliente_id;
	return next _retorno;
  end loop;
end if;


  return;

end;

$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION rtdpj.rel_movimento_diario_faturados_cliente(date, date, integer)
  OWNER TO exmart;

 -- select * from rtdpj.rel_movimento_diario_faturados_cliente('2018-10-15'::date, '2018-10-15'::date, null);
