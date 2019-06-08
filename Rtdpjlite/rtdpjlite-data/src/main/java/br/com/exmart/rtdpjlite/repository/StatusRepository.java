package br.com.exmart.rtdpjlite.repository;

import br.com.exmart.rtdpjlite.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StatusRepository extends JpaRepository<Status, Long> {

    Status findByNome(String nome);

    List<Status> findByIcSistemaIsFalseOrderByNome();
}
