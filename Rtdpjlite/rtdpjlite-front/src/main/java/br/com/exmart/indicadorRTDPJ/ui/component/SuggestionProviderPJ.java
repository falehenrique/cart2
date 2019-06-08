package br.com.exmart.indicadorRTDPJ.ui.component;

import br.com.exmart.indicadorRTDPJ.ui.util.Utils;
import br.com.exmart.rtdpjlite.model.Protocolo;
import br.com.exmart.rtdpjlite.model.TipoProtocolo;
import br.com.exmart.rtdpjlite.service.ProtocoloService;
import br.com.exmart.util.BeanLocator;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteQuery;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestion;
import eu.maxschuster.vaadin.autocompletetextfield.provider.CollectionSuggestionProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class SuggestionProviderPJ extends CollectionSuggestionProvider {

    private ProtocoloService protocoloService;
    public SuggestionProviderPJ() {
        this.protocoloService = BeanLocator.find(ProtocoloService.class);

    }

    @Override
    public Collection<AutocompleteSuggestion> querySuggestions(AutocompleteQuery query) {
//        return super.querySuggestions(query);
        List<AutocompleteSuggestion> retorno = new ArrayList<>();

        List<Protocolo> protocoloList = this.protocoloService.findByNumeroAndTipoProtocoloIn(Long.parseLong(query.getTerm()), Arrays.asList(TipoProtocolo.CERTIDAO_PJ,TipoProtocolo.EXAMECALCULO_PJ, TipoProtocolo.PRENOTACAO_PJ));
        for(Protocolo protocolo : protocoloList){
            retorno.add(new AutocompleteSuggestion(Utils.getNomeCorretoFromTipoProtocolo(protocolo.getTipo()) + " : " + query.getTerm(), protocolo.getStatus().getStatus().getNome(), Utils.getIconCorretoFromTipoProtocolo(protocolo.getTipo())).withData(protocolo));
        }
        return retorno;
    }
}
