package br.com.exmart.rtdpjlite.repository;

import br.com.exmart.rtdpjlite.model.ChecklistGrupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChecklistGrupoRepository extends JpaRepository<ChecklistGrupo, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM rtdpj.checklist_grupo where sub_natureza_id = ? order by nome")
    List<ChecklistGrupo> findBySubNatureza(Long idNatureza);
}
