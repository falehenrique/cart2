ALTER TYPE card_timeline DROP ATTRIBUTE tipo_protocolo;
ALTER TYPE card_timeline ADD ATTRIBUTE especialidade VARCHAR(250);

DROP FUNCTION fn_timeline(bigint, integer);
CREATE OR REPLACE FUNCTION fn_timeline("numero" varchar, "tipo" varchar)
RETURNS SETOF card_timeline AS
$BODY$
	DECLARE
		_registro_referencia varchar;
		_r card_timeline%rowtype;
	BEGIN
		_registro_referencia := (select registro_referencia from registro where registro.registro = $1 and tp_especialidade = $2);


	LOOP
        raise notice 'referencia %', _registro_referencia;
        EXIT WHEN _registro_referencia is null;
        select INTO _r p.id, p.numero_registro, n.nome, 'escrevente', p.data_registro, p.situacao_atual, registro_referencia, p.numero_registro, p.tp_especialidade
        FROM registro p
        JOIN natureza n ON p.natureza_id = n.id
		where registro = _registro_referencia;

		_registro_referencia:= _r.registro_referencia;
		RETURN NEXT _r;
		END LOOP;
	END;
	$BODY$
LANGUAGE plpgsql;