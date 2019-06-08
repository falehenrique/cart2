
--drop function retorna_registros(date,date) ;
CREATE OR REPLACE FUNCTION retorna_registros(date,date)
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
	select numero from protocolo p
	JOIN status_protocolo sp ON sp.protocolo_id = p.id where tipo_protocolo_id = 20 and
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


CREATE OR REPLACE FUNCTION retorna_registros_pj(date,date)
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
	select numero from protocolo p
	JOIN status_protocolo sp ON sp.protocolo_id = p.id where tipo_protocolo_id = 50 and
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
CREATE OR REPLACE FUNCTION retorna_registros_exametd(date,date)
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
	select numero from protocolo p
	JOIN status_protocolo sp ON sp.protocolo_id = p.id where tipo_protocolo_id = 10 and
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
CREATE OR REPLACE FUNCTION retorna_registros_examepj(date,date)
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
	select numero from protocolo p
	JOIN status_protocolo sp ON sp.protocolo_id = p.id where tipo_protocolo_id = 40 and
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


