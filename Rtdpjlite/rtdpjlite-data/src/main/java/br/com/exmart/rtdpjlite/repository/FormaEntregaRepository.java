package br.com.exmart.rtdpjlite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.exmart.rtdpjlite.model.FormaEntrega;

@Repository
public interface FormaEntregaRepository extends JpaRepository<FormaEntrega, Long> {

    FormaEntrega findByNome(String nome);
}
