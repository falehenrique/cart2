package br.com.exmart.rtdpjlite.repository;

import br.com.exmart.rtdpjlite.model.Feriado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface FeriadoRepository extends JpaRepository<Feriado, Long> {
    List<Feriado> findByDiaGreaterThan(LocalDate dtInicial);

    List<Feriado> findByRecorrenteTrue();

    Feriado findByDiaEquals(LocalDate dia);
}
