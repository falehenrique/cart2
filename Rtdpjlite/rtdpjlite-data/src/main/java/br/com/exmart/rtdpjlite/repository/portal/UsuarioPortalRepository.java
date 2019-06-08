package br.com.exmart.rtdpjlite.repository.portal;

import br.com.exmart.rtdpjlite.model.CartorioParceiro;
import br.com.exmart.rtdpjlite.model.Cliente;
import br.com.exmart.rtdpjlite.model.portal.UsuarioPortal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UsuarioPortalRepository extends JpaRepository<UsuarioPortal, Long> {
//    @Query(nativeQuery = true, value = "SELECT * FROM db_portal.tb_usaurio WHERE cartorio_parceiro_id = ?1")
    UsuarioPortal findByCartorioParceiroEquals(CartorioParceiro cartorioParceiro);

    UsuarioPortal findByEmail(String email);


    List<UsuarioPortal> findByClienteOrderByNome(Cliente cliente);
}
