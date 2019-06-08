package br.com.exmart.rtdpjlite.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.exmart.rtdpjlite.model.Tabela;

public interface TabelaRepository extends JpaRepository<Tabela, Long>{

	Tabela findByNomeLikeIgnoreCase(String nome);

}
