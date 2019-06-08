package br.com.exmart.rtdpjlite.repository;

import br.com.exmart.rtdpjlite.model.Lote;
import br.com.exmart.rtdpjlite.model.LoteItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoteItemRepository extends JpaRepository<LoteItem, Long> {
    List<LoteItem> findByFinalizadoIsNullAndResultadoIsNull();

    List<LoteItem> findByLoteOrderById(Lote lote);
}
