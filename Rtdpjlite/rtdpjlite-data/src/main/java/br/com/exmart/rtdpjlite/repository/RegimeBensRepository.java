package br.com.exmart.rtdpjlite.repository;

import br.com.exmart.rtdpjlite.model.RegimeBens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RegimeBensRepository extends JpaRepository<RegimeBens, Long> {
    @Override
    @Query(nativeQuery = true, value = "SELECT * FROM rtdpj.regime_bens order by nome ")
    List<RegimeBens> findAll();
}
