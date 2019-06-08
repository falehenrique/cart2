package br.com.exmart.indicadorRTDPJ;

/**
 * Created by fabioebner on 28/05/17.
 */
public enum ProtocoloTIpo {
    EXAME_CALCULO(1L),
    PRENOTACAO_TD(2L),
    CERTIDAO(3L),
    PRENOTACAO_PJ(4L);

    Long id;

    ProtocoloTIpo(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
