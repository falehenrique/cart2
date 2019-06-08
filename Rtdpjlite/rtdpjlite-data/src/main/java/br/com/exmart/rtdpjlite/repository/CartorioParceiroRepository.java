package br.com.exmart.rtdpjlite.repository;

import br.com.exmart.rtdpjlite.model.CartorioParceiro;
import br.com.exmart.rtdpjlite.model.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartorioParceiroRepository extends JpaRepository<CartorioParceiro, Long>{
    CartorioParceiro findByEmail(String email);

    List<CartorioParceiro> findByCidade(Cidade cidade);
}
