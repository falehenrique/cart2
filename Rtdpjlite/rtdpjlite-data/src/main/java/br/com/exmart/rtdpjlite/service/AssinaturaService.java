package br.com.exmart.rtdpjlite.service;

import br.com.exmart.rtdpjlite.vo.Arquivo;
import br.com.exmart.rtdpjlite.vo.financeiro.ServicoCalculado;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Base64;

@Service
public class AssinaturaService {
    @Autowired
    private ConfiguracaoService configuracaoService;
    @Autowired
    private RestTemplate restTemplate;

    public byte[] getArquivoFromArquivoId(String arquivoId) {

        String url = configuracaoService.findConfiguracao().getSiteAssinaturaDigital()+"arquivos/"+arquivoId;
        System.out.println("buscando: " + arquivoId);
        ResponseEntity<Arquivo> response = restTemplate.getForEntity(url, Arquivo.class);
        Arquivo retorno = response.getBody();
        return Base64.getDecoder().decode(retorno.getBase64());
    }
}
