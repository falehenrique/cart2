package br.com.exmart.indicadorRTDPJ.ui.impl;

import br.com.exmart.indicadorRTDPJ.ui.ModalAssinaturas;
import br.com.exmart.rtdpjlite.vo.AssinaturaDocumentoVO;

import java.util.List;

public class ModalAssinaturasImpl extends ModalAssinaturas{

    public ModalAssinaturasImpl(List<AssinaturaDocumentoVO> assinaturas) {

        assinaturasGrid.getColumn("signatario").setCaption("Assinado por");
        assinaturasGrid.getColumn("dtEmissao").setCaption("Assinado em");
        assinaturasGrid.setItems(assinaturas);
    }
}
