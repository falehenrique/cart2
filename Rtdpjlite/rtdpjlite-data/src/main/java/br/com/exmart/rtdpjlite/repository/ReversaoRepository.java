package br.com.exmart.rtdpjlite.repository;

import br.com.exmart.rtdpjlite.model.Reversao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReversaoRepository extends JpaRepository<Reversao, Long> {
    Optional<Reversao> findFirstByProtocoloTipoIdAndNumeroRegistro(Integer tipoProtocoloId, Long numeroRegistro);

    Optional<Reversao> findFirstByProtocoloIdAndProtocoloTipoIdAndNumeroRegistroIsNotNull(Long protocoloId, Integer tipoProtocoloId);
}
