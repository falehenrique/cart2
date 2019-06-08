package br.com.exmart.rtdpjlite.repository;

import br.com.exmart.rtdpjlite.model.ProtocoloGrid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProtocoloGridRepository extends JpaRepository<ProtocoloGrid, Long> {
    @Query(nativeQuery = true, value = "SELECT p.id, numero, nome as natureza, tipo_protocolo_id as tipo, apresentante, data_vencimento, parte,  " +
            "(SELECT nome FROM rtdpj.status_protocolo sp JOIN rtdpj.status s ON s.id = sp.status_id WHERE sp.protocolo_id = p.id order by data desc  limit 1) as status  " +
            "FROM rtdpj.protocolo p " +
            "JOIN rtdpj.natureza n ON p.natureza_id = n.id  " +
            "order by p.data_protocolo " +
            " offset ?1 limit ?2")
    List<ProtocoloGrid> findAllGrid(int offset, int limit);

    List<ProtocoloGrid> findAll();
}
