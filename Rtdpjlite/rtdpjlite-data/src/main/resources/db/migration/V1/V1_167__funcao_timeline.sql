drop index registro_idx_02;
drop index registro_idx_03;
create index registro_idx_02 on registro(registro, tp_especialidade);
/*
update registro r
set registro_referencia = (select
  '123810R' || (select extract(year from data_registro) from registro r1
   where r1.numero_registro = r.numero_registro_referencia and r1.tp_especialidade = r.tp_especialidade) || 'B' ||
  lpad(cast(r.numero_registro_referencia as varchar), 9, '0'))
where
  registro_referencia is null and
  numero_registro_referencia is not null and
  tp_especialidade = 'TD';

update registro r
set registro_referencia = (select
  '123810R' || (select extract(year from data_registro) from registro r1
   where r1.numero_registro = r.numero_registro_referencia and r1.tp_especialidade = r.tp_especialidade) || 'B' ||
  lpad(cast(r.numero_registro_referencia as varchar), 9, '0'))
where
  registro_referencia is null and
  numero_registro_referencia is not null and
  tp_especialidade = 'PJ';
*/


drop function if exists rtdpj.timeline_registro(varchar, varchar);

create function rtdpj.timeline_registro(varchar, varchar) returns table(
  registro_r varchar,
  numero_registro_r bigint,
  data_registro_r timestamp without time zone,
  registro_referencia_r varchar,
  situacao_atual_r text,
  observacao_r text,
  protocolo_id_r bigint,
  tp_especialidade_r varchar,
  nome_r varchar,
  numero_registro_referencia_r bigint
) as
$lumera$
declare
  _registro alias for $1;
  _tipo_registro alias for $2;

  _ultimo_reg varchar;
  _ret record;
  _reg_p integer;
begin

   --Recursividade para identificar o ultimo registro da cadeia
   WITH RECURSIVE timeline (
	registro, numero_registro, data_registro, registro_referencia,
	situacao_atual, observacao, protocolo_id, tp_especialidade, natureza,
	numero_registro_referencia
   ) AS (
		SELECT
		  registro, numero_registro, data_registro, registro_referencia,
		  situacao_atual, observacao, protocolo_id, tp_especialidade, natureza.nome,
	     numero_registro_referencia
		FROM rtdpj.registro JOIN rtdpj.natureza ON natureza.id = registro.natureza_id
		WHERE registro  = _registro and tp_especialidade = _tipo_registro
		UNION ALL
			SELECT
			  r.registro, r.numero_registro, r.data_registro, r.registro_referencia,
			  r.situacao_atual, r.observacao, r.protocolo_id, r.tp_especialidade, natureza.nome,
           r.numero_registro_referencia
			FROM rtdpj.registro r
			JOIN rtdpj.natureza ON natureza.id = r.natureza_id
			INNER JOIN
			timeline ON r.registro_referencia = timeline.registro
	)
	select
	  registro into _ultimo_reg
	from
	  timeline
	order by data_registro desc limit 1;

	return query
	WITH RECURSIVE timeline (
	registro, numero_registro, data_registro, registro_referencia,
	situacao_atual, observacao, protocolo_id, tp_especialidade, natureza,
	numero_registro_referencia
   ) AS (
		SELECT
		  registro, numero_registro, data_registro, registro_referencia,
		  situacao_atual, observacao, protocolo_id, tp_especialidade, natureza.nome,
	     numero_registro_referencia
		FROM rtdpj.registro JOIN rtdpj.natureza ON natureza.id = registro.natureza_id
		WHERE registro  = _ultimo_reg and tp_especialidade = _tipo_registro
		UNION ALL
			SELECT
			  r.registro, r.numero_registro, r.data_registro, r.registro_referencia,
			  r.situacao_atual, r.observacao, r.protocolo_id, r.tp_especialidade, natureza.nome,
           r.numero_registro_referencia
			FROM rtdpj.registro r
			JOIN rtdpj.natureza ON natureza.id = r.natureza_id
			INNER JOIN
			timeline ON r.registro = timeline.registro_referencia
	)
	select
	  registro, numero_registro, data_registro, registro_referencia, situacao_atual,
	  observacao, protocolo_id as protocolo, tp_especialidade as especialidade, natureza,
	  numero_registro_referencia
	from
	  timeline
	order by data_registro desc;

end;
$lumera$
language plpgsql;

--select * from timeline_registro('123810R2018B000327930', 'TD');







