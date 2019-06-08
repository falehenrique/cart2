package br.com.exmart.rtdpjlite.repository;

import br.com.exmart.rtdpjlite.model.SubNatureza;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubNaturezaRepository extends JpaRepository<SubNatureza, Long>{
    SubNatureza findByNome(String nome);
}
