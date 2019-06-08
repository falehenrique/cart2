package br.com.exmart.rtdpjlite.repository;

import br.com.exmart.rtdpjlite.model.Objeto;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ObjetoRepository extends JpaRepository<Objeto, Long> {
    Objeto findByNome(String nome);

    @Override
//    @EntityGraph(value = "Objeto.full", type = EntityGraph.EntityGraphType.LOAD)
    List<Objeto> findAll(Sort sort);
}
