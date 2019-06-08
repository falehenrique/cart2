package br.com.exmart.rtdpjlite.repository.portal;

import br.com.exmart.rtdpjlite.vo.portal.PedidoVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PedidoVoRepository extends JpaRepository<PedidoVO, Long> {

    @Query(nativeQuery = true, value = "select p.numero_registro, p.id, p.nr_contrato, p.data, p.parte, p.parte_documento, u.nome as responsavel_nome, prot.apresentante, p.status_atual_cliente as status, p.ultima_atualizacao " +
            ",DATE(now()) - DATE(p.ultima_atualizacao) as dias_parado " +
            "FROM db_portal.tb_pedido p  " +
            "JOIN rtdpj.cliente c ON c.id =  p.cliente_id  " +
            "LEFT JOIN rtdpj.protocolo prot ON prot.id = p.protocolo_id  " +
            "LEFT JOIN db_portal.tb_usuario u ON u.id = p.responsavel_id  " +
            "WHERE p.cliente_id = ?1 AND dt_finalizado is null ORDER BY p.id DESC ")
    List<PedidoVO> findByClienteId(Long id);

    @Query(nativeQuery = true, value = "select p.numero_registro, p.id, p.nr_contrato, p.data, p.parte, p.parte_documento, u.nome as responsavel_nome, prot.apresentante, p.status_atual_cliente as status, p.ultima_atualizacao " +
            ",DATE(now()) - DATE(p.ultima_atualizacao) as dias_parado " +
            "FROM db_portal.tb_pedido p  " +
            "JOIN rtdpj.cliente c ON c.id =  p.cliente_id  " +
            "LEFT JOIN rtdpj.protocolo prot ON prot.id = p.protocolo_id  " +
            "LEFT JOIN db_portal.tb_usuario u ON u.id = p.responsavel_id  " +
            "WHERE p.cliente_id = ?1 AND (p.nr_contrato = ?2 OR p.id = ?3 OR p.protocolo = ?4)" +
            "ORDER BY p.id DESC ")
    List<PedidoVO> findByClienteId(Long id, String nrContrato, Long pedidoId, Long protocolo);

    @Query(nativeQuery = true, value = "select p.numero_registro, p.id, p.nr_contrato, p.data, p.parte, p.parte_documento, u.nome as responsavel_nome, prot.apresentante, pp.status_atual_parceiro as status, p.ultima_atualizacao " +
            ",DATE(now()) - DATE(pp.ultima_atualizacao) as dias_parado " +
            "FROM db_portal.tb_pedido p " +
            "JOIN rtdpj.cliente c ON c.id =  p.cliente_id " +
            "JOIN rtdpj.protocolo prot ON prot.id = p.protocolo_id  " +
            "JOIN db_portal.tb_pedido_protocolo pp ON pp.pedido_id = p.id " +
            "LEFT JOIN db_portal.tb_usuario u ON u.id = p.responsavel_id " +
            "WHERE pp.cartorio_parceiro_id = ?1 AND dt_finalizado is null ORDER BY p.id DESC ")
    List<PedidoVO> findByCartorioParceiroId(Long id);

    @Query(nativeQuery = true, value = "select p.numero_registro, p.id, p.nr_contrato, p.data, p.parte, p.parte_documento, u.nome as responsavel_nome, prot.apresentante, pp.status_atual_parceiro as status, p.ultima_atualizacao " +
            ",DATE(now()) - DATE(pp.ultima_atualizacao) as dias_parado " +
            "FROM db_portal.tb_pedido p " +
            "JOIN rtdpj.cliente c ON c.id =  p.cliente_id " +
            "JOIN rtdpj.protocolo prot ON prot.id = p.protocolo_id  " +
            "JOIN db_portal.tb_pedido_protocolo pp ON pp.pedido_id = p.id " +
            "LEFT JOIN db_portal.tb_usuario u ON u.id = p.responsavel_id " +
            "WHERE pp.cartorio_parceiro_id = ?1 AND (p.nr_contrato = ?2 OR p.id = ?3 OR p.protocolo = ?4) ORDER BY p.id DESC ")
    List<PedidoVO> findByCartorioParceiroId(Long id , String nrContrato, Long pedidoId, Long protocolo);

    @Query(nativeQuery = true, value = "select DISTINCT p.numero_registro, p.id, p.nr_contrato, p.data, p.parte, p.parte_documento, u.nome as responsavel_nome, prot.apresentante, p.status_atual_cartorio as status, p.ultima_atualizacao " +
            ",DATE(now()) - DATE(p.ultima_atualizacao) as dias_parado " +
            "FROM db_portal.tb_pedido p  " +
            "JOIN rtdpj.cliente c ON c.id =  p.cliente_id " +
            "JOIN rtdpj.protocolo prot ON prot.id = p.protocolo_id  " +
            "LEFT JOIN db_portal.tb_pedido_protocolo pp ON pp.pedido_id = p.id " +
            "LEFT JOIN db_portal.tb_usuario u ON u.id = p.responsavel_id WHERE  dt_finalizado is null ORDER BY p.id DESC ")
    List<PedidoVO> findAllCartorio();

    @Query(nativeQuery = true, value = "select DISTINCT p.numero_registro, p.id, p.nr_contrato, p.data, p.parte, p.parte_documento, u.nome as responsavel_nome, prot.apresentante, p.status_atual_cartorio as status, p.ultima_atualizacao " +
            ",DATE(now()) - DATE(p.ultima_atualizacao) as dias_parado " +
            "FROM db_portal.tb_pedido p  " +
            "JOIN rtdpj.cliente c ON c.id =  p.cliente_id " +
            "JOIN rtdpj.protocolo prot ON prot.id = p.protocolo_id  " +
            "LEFT JOIN db_portal.tb_pedido_protocolo pp ON pp.pedido_id = p.id " +
            "LEFT JOIN db_portal.tb_usuario u ON u.id = p.responsavel_id WHERE p.nr_contrato = ?1 OR p.id = ?2 OR p.protocolo = ?3 or p.numero_registro = ?4 ORDER BY p.id DESC ")
    List<PedidoVO> findAllCartorio(String nrContrato, Long pedidoId, Long protocolo, Long numeroRegistro);
}
