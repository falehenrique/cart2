package br.com.exmart.rtdpjlite.repository.portal;

import br.com.exmart.rtdpjlite.model.Cliente;
import br.com.exmart.rtdpjlite.model.portal.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByCliente(Cliente cliente);

    @Query(nativeQuery = true, value = "Select * from db_portal.tb_pedido WHERE id = ?1")
    Pedido findOne(Long id);


    List<Pedido> findByProtocoloIsNull();
}
