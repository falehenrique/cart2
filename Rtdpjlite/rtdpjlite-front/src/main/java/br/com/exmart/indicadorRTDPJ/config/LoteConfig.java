package br.com.exmart.indicadorRTDPJ.config;

import br.com.exmart.rtdpjlite.service.LoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class LoteConfig {

    LoteService loteService;

    @Autowired
    public LoteConfig(LoteService loteService) {
        this.loteService = loteService;
    }

    @PostConstruct
    public void init(){
        System.out.println("Rodando processo em backgroud");
        loteService.processarArquivosLote();
    }
}
