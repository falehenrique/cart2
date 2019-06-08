package br.com.exmart.rtdpjlite.repository;

import br.com.exmart.rtdpjlite.model.Qualidade;
import br.com.exmart.rtdpjlite.model.SubNatureza;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QualidadeRepository extends JpaRepository<Qualidade, Long> {
    Qualidade findByNomeIgnoreCase(String nome);
}
