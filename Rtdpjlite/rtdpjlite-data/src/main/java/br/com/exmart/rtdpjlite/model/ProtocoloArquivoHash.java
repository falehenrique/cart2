package br.com.exmart.rtdpjlite.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity()
@Table(name="protocolo_arquivo_hash", schema = "rtdpj")
public class ProtocoloArquivoHash extends AbstractEntity{
    @Column
    @NotNull
    private Long protocoloId;
    @Column
    private Long protocoloServicoId;
    @Column
    @NotEmpty
    private String hash;

    public ProtocoloArquivoHash(Long numeroProtocolo, Long idServico, String novoHash) {
        this.protocoloId = numeroProtocolo;
        this.protocoloServicoId = idServico;
        this.hash = novoHash;
    }

    public ProtocoloArquivoHash() {
    }

    public Long getProtocoloId() {
        return protocoloId;
    }

    public void setProtocoloId(Long protocoloId) {
        this.protocoloId = protocoloId;
    }

    public Long getProtocoloServicoId() {
        return protocoloServicoId;
    }

    public void setProtocoloServicoId(Long protocoloServicoId) {
        this.protocoloServicoId = protocoloServicoId;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
