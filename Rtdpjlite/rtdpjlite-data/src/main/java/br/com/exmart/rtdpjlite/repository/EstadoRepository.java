package br.com.exmart.rtdpjlite.repository;

import br.com.exmart.rtdpjlite.model.Estado;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EstadoRepository extends JpaRepository<Estado, Long> {

//    @EntityGraph(value = "Estado.cidades", type = EntityGraph.EntityGraphType.LOAD)
    @Override
    List<Estado> findAll(Sort sort);
}
