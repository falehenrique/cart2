package br.com.exmart.rtdpjlite.repository;

import br.com.exmart.rtdpjlite.model.Modelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ModeloRepository extends JpaRepository<Modelo, Long> {

    @Query(nativeQuery = true, value = "SELECT m.* FROM rtdpj.modelo m JOIN rtdpj.modelo_natureza_subnatureza mns ON mns.modelo_id = m.id WHERE natureza_id = ?1 AND sub_natureza_id = ?2 AND m.tipo_modelo_id = ?3  ORDER BY ic_padrao desc")
    List<Modelo> findByNaturezaAndSubNatureza(Long idNatureza, Long idSubNatureza, Integer idTipoModelo);

    @Query(nativeQuery = true, value = "SELECT m.* FROM rtdpj.modelo m JOIN rtdpj.modelo_natureza_subnatureza mns ON mns.modelo_id = m.id WHERE natureza_id = ?1 AND m.tipo_modelo_id = ?2  AND sub_natureza_id is null  ORDER BY ic_padrao desc")
    List<Modelo> findByNaturezaAndSubNatureza(Long idNatureza, Integer idTipoModelo);
}
