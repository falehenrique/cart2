create function rtdpj.re(integer, integer) returns integer as
$lumera$
declare
  _numeroProtocolo alias for $1;
  _tipoProtocolo alias for $2;
  _status_id integer;
  _protocolo_id integer;
begin
  _status_id := (select id from rtdpj.status where nome = 'PROTOCOLADO');
  _protocolo_id := (select id from rtdpj.protocolo where numero = _numeroProtocolo and tipo_protocolo_id = _tipoProtocolo);
  delete from rtdpj.status_protocolo
  where
  protocolo_id = _protocolo_id and
  status_id <> _status_id;

  update rtdpj.protocolo
  set
  dt_registro = null
  where id = _protocolo_id;


  return _protocolo_id;
end;
$lumera$
language plpgsql;


create function rtdpj.reverter_registro(varchar,varchar) returns integer as
$lumera$
declare
  _registro alias for $1;
  _especialidade alias for $2;
  _id_registro integer;
  _protocolo_id integer;
  _protocolo_numero integer;
  _tipo_protocolo_id integer;
  _status_id integer;
begin
  _id_registro := (select id from rtdpj.registro where registro = _registro and tp_especialidade = _especialidade);
  _protocolo_id := (select protocolo_id from rtdpj.registro where registro = _registro and tp_especialidade = _especialidade);
  _tipo_protocolo_id := (select tipo_protocolo_id from rtdpj.protocolo where id = _protocolo_id);
  _protocolo_numero := (select numero from rtdpj.protocolo where id = _protocolo_id);

  delete from rtdpj.indicador_pessoal where registro_id = _id_registro;
  delete from rtdpj.registro where id = _id_registro;
  PERFORM rtdpj.reverter_protocolo(_protocolo_numero, _tipo_protocolo_id);
  return _id_registro;
end;
$lumera$
language plpgsql;


CREATE TABLE reversao(
    id SERIAL PRIMARY KEY,
    usuario_id INTEGER REFERENCES usuario(id),
    data TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    motivo TEXT NOT NULL,
    tipo_reversao VARCHAR(250) NOT NULL,
    numero VARCHAR(250) NOT NULL,
    tipo VARCHAR(250) NOT NULL
);


