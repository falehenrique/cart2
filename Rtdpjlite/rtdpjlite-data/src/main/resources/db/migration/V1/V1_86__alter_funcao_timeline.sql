ALTER TYPE card_timeline ADD ATTRIBUTE tipo_protocolo bigint;

CREATE OR REPLACE FUNCTION fn_timeline("numero" bigint, "tipo" integer)
RETURNS SETOF card_timeline AS
$BODY$
	DECLARE
		_registro_referencia varchar;
		_r card_timeline%rowtype;
	BEGIN
		_registro_referencia := (select registro_referencia from protocolo where protocolo.numero = $1 and tipo = $2);


		LOOP
        raise notice 'referencia %', _registro_referencia;
        EXIT WHEN _registro_referencia is null;
        select INTO _r p.id, p.numero_registro, n.nome, 'escrevente', p.dt_registro, p.situacao_atual_registro, registro_referencia, p.numero, p.tipo_protocolo_id
        FROM protocolo p
        JOIN natureza n ON p.natureza_id = n.id
		where numero_registro = _registro_referencia;

		_registro_referencia:= _r.registro_referencia;
		RETURN NEXT _r;
		END LOOP;
	END;
	$BODY$
LANGUAGE plpgsql;