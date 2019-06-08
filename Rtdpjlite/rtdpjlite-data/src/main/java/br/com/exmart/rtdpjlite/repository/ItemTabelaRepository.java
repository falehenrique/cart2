package br.com.exmart.rtdpjlite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.exmart.rtdpjlite.model.ItemTabela;

@Repository
public interface ItemTabelaRepository extends JpaRepository<ItemTabela, Long>{

}
