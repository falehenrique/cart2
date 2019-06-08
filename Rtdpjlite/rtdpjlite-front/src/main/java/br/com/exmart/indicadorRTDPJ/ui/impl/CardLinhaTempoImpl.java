package br.com.exmart.indicadorRTDPJ.ui.impl;

import br.com.exmart.indicadorRTDPJ.ui.CardLinhaTempo;
import br.com.exmart.indicadorRTDPJ.ui.util.GerenciarJanela;
import br.com.exmart.rtdpjlite.model.CardTimeline;
import br.com.exmart.rtdpjlite.model.Registro;
import br.com.exmart.rtdpjlite.service.RegistroService;
import br.com.exmart.util.BeanLocator;
import com.google.common.base.Strings;

import java.time.format.DateTimeFormatter;

public class CardLinhaTempoImpl extends CardLinhaTempo {

    private RegistroService registroService;
    private CardTimeline card;
    public CardLinhaTempoImpl(CardTimeline card, Long registroAtual) {
        this.card = card;
        registroService = BeanLocator.find(RegistroService.class);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");

//        btnVerRegistro.addClickListener(evt-> GerenciarJanela.abrirJanela("", 90,90, new ViewRegistroImpl(null), null, true, true, false, true));
//        escrevente.setValue(card.getEscrevente());
        if(!Strings.isNullOrEmpty(card.getSituacaoAtual())){
            situacaoAtual.setValue(card.getSituacaoAtual());
        }
        natureza.setValue(card.getNatureza());
        registro.setValue(card.getNumeroRegistro().toString());
        if(card.getDataRegistro() != null) {
            dataRegistro.setValue(card.getDataRegistro().format(formatter));
        }
        btnVerRegistro.addClickListener(evt->btnVerRegistroListener());
        if(registroAtual != null){
            if(card.getNumeroRegistro().equals(registroAtual)) {
                barraLateral.removeStyleName("bg-purple-grey-medium");
                barraLateral.addStyleName("bg-purple-red-medium");
            }
        }
    }

    private void btnVerRegistroListener() {
        //FIXME deve arrumar
        Registro registroReferencia = registroService.findByRegistroAndEspecialidade(this.card.getRegistro(), this.card.getEspecialidade());
        GerenciarJanela.abrirJanela("", 90, 90, new ViewRegistroImpl(registroReferencia));
    }
}
