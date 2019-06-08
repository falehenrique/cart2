package br.com.exmart.rtdpjlite.repository;

import br.com.exmart.rtdpjlite.model.Parte;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParteRepository extends JpaRepository<Parte,Long>{

    @EntityGraph(value = "Parte.tudo", type = EntityGraph.EntityGraphType.LOAD)
    public Parte findFirstByCpfCnpjOrderByIdDesc(String documento);

    @EntityGraph(value = "Parte.tudo", type = EntityGraph.EntityGraphType.LOAD)
    Parte findFirstByUuidConjugeAndIdNotOrderByIdDesc(String uuidConjuge, Long id);
}
