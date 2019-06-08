
CREATE TYPE rel_movimentacao_type AS
   (protocolo integer,
    data_registro date,
    natureza character varying(250),
    subnatureza character varying(250),
    tipo_protocolo character varying(250),
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

CREATE OR REPLACE FUNCTION rel_movimento_diario(
date,
date)
RETURNS SETOF rel_movimentacao_type AS
$BODY$
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
protocolo p left join
--status_protocolo sp on sp.protocolo_id = p.id and status_id = 8 join
natureza n on n.id = p.natureza_id join
sub_natureza sn on sn.id = p.sub_natureza_id
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
$BODY$
LANGUAGE plpgsql VOLATILE
COST 100
ROWS 1000;