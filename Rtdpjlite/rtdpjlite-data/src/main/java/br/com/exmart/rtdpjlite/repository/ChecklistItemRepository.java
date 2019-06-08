package br.com.exmart.rtdpjlite.repository;

import br.com.exmart.rtdpjlite.model.ChecklistItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChecklistItemRepository extends JpaRepository<ChecklistItem, Long> {
}
