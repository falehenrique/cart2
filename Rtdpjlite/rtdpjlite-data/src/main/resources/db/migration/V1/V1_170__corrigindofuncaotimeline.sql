CREATE OR REPLACE FUNCTION rtdpj.timeline_registro(
	character varying,
	character varying)
RETURNS TABLE(registro_r character varying, numero_registro_r bigint, data_registro_r timestamp without time zone, registro_referencia_r character varying, situacao_atual_r text, observacao_r text, protocolo_id_r bigint, tp_especialidade_r character varying, nome_r character varying, numero_registro_referencia_r bigint)
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE
    ROWS 1000
AS $BODY$
declare
  _registro alias for $1;
  _tipo_registro alias for $2;

  _ultimo_reg varchar;
  _ret record;
  _reg_p integer;
begin

   --Recursividade para identificar o primeiro registro da cadeia
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
			timeline ON r.registro = timeline.registro_referencia and r.tp_especialidade = _tipo_registro
	)
	select
	  registro into _ultimo_reg
	from
	  timeline
	order by data_registro asc limit 1;
	raise notice 'primeiro registro %', _ultimo_reg;
	--Feito isso pois ele nao estava recuperando o primeiro registro (o que nao tem registro referencia)
	select
	  registro into _ultimo_reg
	from
	  rtdpj.registro where registro = (select registro_referencia FROM rtdpj.registro where registro = _ultimo_reg)
	order by data_registro asc limit 1;
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
		WHERE registro  = _ultimo_reg and registro.tp_especialidade = _tipo_registro
		UNION ALL
			SELECT
			  r.registro, r.numero_registro, r.data_registro, r.registro_referencia,
			  r.situacao_atual, r.observacao, r.protocolo_id, r.tp_especialidade, natureza.nome,
           r.numero_registro_referencia
			FROM rtdpj.registro r
			JOIN rtdpj.natureza ON natureza.id = r.natureza_id
			INNER JOIN
			timeline ON
		     r.registro_referencia = timeline.registro and
		     r.tp_especialidade = _tipo_registro
	  -- and r.registro_referencia <> '0'
	)
	select
	  registro, numero_registro, data_registro, registro_referencia, situacao_atual,
	  observacao, protocolo_id as protocolo, tp_especialidade as especialidade, natureza,
	  numero_registro_referencia
	from
	  timeline
	order by data_registro desc;

end;
$BODY$;