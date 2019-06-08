package br.com.exmart.rtdpjlite.repository;

import br.com.exmart.rtdpjlite.model.CardTimeline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;

import java.util.List;

public interface TimelineRepository extends JpaRepository<CardTimeline, Long> {
//    @Query(nativeQuery = true, value = "\n" +
//            "\n" +
//            "WITH RECURSIVE timeline (registro, numero_registro, data_registro, registro_referencia, situacao_atual, observacao, protocolo_id, tp_especialidade, natureza) AS\n" +
//            "(\n" +
//            "SELECT registro, numero_registro, data_registro, registro_referencia, situacao_atual, observacao, protocolo_id, tp_especialidade, natureza.nome FROM rtdpj.registro JOIN rtdpj.natureza ON natureza.id = registro.natureza_id WHERE registro  = ?1 and tp_especialidade = ?2\n" +
//            "UNION ALL\n" +
//            "   SELECT r.registro, r.numero_registro, r.data_registro, r.registro_referencia, r.situacao_atual, r.observacao, r.protocolo_id, r.tp_especialidade, natureza.nome\n" +
//            "   FROM rtdpj.registro r\n" +
//            "   JOIN rtdpj.natureza ON natureza.id = r.natureza_id\n" +
//            "   INNER JOIN \n" +
//            "   timeline ON r.registro = timeline.registro_referencia\n" +
//            ")\n" +
//            "select registro, numero_registro, data_registro, registro_referencia, situacao_atual, observacao, protocolo_id as protocolo, tp_especialidade as especialidade, natureza from timeline order by data_registro desc;\n")
//    List<CardTimeline> findTimeline(String numero, String especialidade);
//    @Procedure(value = "rtdpj.timeline_registro"
    @Query(nativeQuery = true, value = "select registro_r as registro, numero_registro_r as numero_registro, data_registro_r as data_registro, registro_referencia_r as registro_referencia, situacao_atual_r as situacao_atual, observacao_r as observacao, protocolo_id_r as protocolo, tp_especialidade_r as especialidade, nome_r as natureza, numero_registro_referencia_r as numero_registro_referencia  from rtdpj.timeline_registro(?1, ?2) order by numero_registro asc")
    List<CardTimeline> findTimeline(String numero, String especialidade);
}
