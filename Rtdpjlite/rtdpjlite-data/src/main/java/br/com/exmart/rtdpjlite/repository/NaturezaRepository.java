package br.com.exmart.rtdpjlite.repository;

import br.com.exmart.rtdpjlite.model.Natureza;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NaturezaRepository extends JpaRepository<Natureza, Long> {
	Page<Natureza> findByNomeLikeIgnoreCase(String nome, Pageable page);
	long countByNome(String nome);
//	@EntityGraph(value = "Natureza.subNaturezas", type = EntityGraph.EntityGraphType.LOAD)
	List<Natureza> findAll();

//	@Query(nativeQuery = true, value = "SELECT * FROM natureza n JOIN sub_natureza sn on sn.natureza_id = n.id WHERE natureza_tipo_id = ?1")
//	@EntityGraph(value = "Natureza.subNaturezas", type = EntityGraph.EntityGraphType.LOAD)
    List<Natureza> findByNaturezaTipoId(Long tipoId);


    Natureza findBySubNaturezas_id(Long id);

	@Override
	@EntityGraph(value = "Natureza.subNaturezas", type = EntityGraph.EntityGraphType.LOAD)
	Natureza findOne(Long aLong);

	@EntityGraph(value = "Natureza.subNaturezas", type = EntityGraph.EntityGraphType.LOAD)
    Natureza findByNome(String nome);
}
