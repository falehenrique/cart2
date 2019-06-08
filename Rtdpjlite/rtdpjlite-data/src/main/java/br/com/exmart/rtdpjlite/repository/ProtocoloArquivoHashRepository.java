package br.com.exmart.rtdpjlite.repository;

import br.com.exmart.rtdpjlite.model.ProtocoloArquivoHash;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProtocoloArquivoHashRepository extends JpaRepository<ProtocoloArquivoHash, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM rtdpj.protocolo_arquivo_hash WHERE protocolo_id = ?1 and protocolo_servico_id is null")
    ProtocoloArquivoHash findByProtocolo(Long numeroProtocolo);
    @Query(nativeQuery = true, value = "SELECT * FROM rtdpj.protocolo_arquivo_hash WHERE protocolo_id = ?1 and protocolo_servico_id = ?2")
    ProtocoloArquivoHash findByProtocoloAndServico(Long numeroProtocolo, Long idServico);
    @Query(nativeQuery = true, value = "SELECT * FROM rtdpj.protocolo_arquivo_hash WHERE hash = ?1")
    ProtocoloArquivoHash findByHash(String hash);

}

