package br.com.exmart.rtdpjlite.repository;

import br.com.exmart.rtdpjlite.model.IndicadorPessoal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IndicadorPessoalRepository extends JpaRepository<IndicadorPessoal, Long> {

    List<IndicadorPessoal> findByCpfCnpjOrderByIdDesc(String documento);
}
