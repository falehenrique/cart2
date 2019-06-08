package br.com.exmart.rtdpjlite.repository;

import br.com.exmart.rtdpjlite.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClienteRepository  extends JpaRepository<Cliente, Long>{
    Cliente findByIdentificacao(String identificacao);

    @Query(nativeQuery = true, value = "SELECT * FROM rtdpj.cliente WHERE email ilike ?1")
    Cliente findByEmailIgnoreCase(String email);
}
