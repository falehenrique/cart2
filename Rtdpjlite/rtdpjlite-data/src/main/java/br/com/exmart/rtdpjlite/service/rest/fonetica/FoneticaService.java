package br.com.exmart.rtdpjlite.service.rest.fonetica;

import br.com.exmart.rtdpjlite.service.ProtocoloService;
import br.com.exmart.rtdpjlite.vo.balcaoonline.Pedido;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

@Service
public class FoneticaService {
    @Autowired
    private RestTemplate restTemplate;
    @Value("${urlFonetica}")
    private String urlFonetica;
    protected static Logger logger = LoggerFactory.getLogger(FoneticaService.class);
    public String foneticar(String nome) throws HttpClientErrorException, HttpServerErrorException {

        HttpEntity<String> request = new HttpEntity<>(nome);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(urlFonetica+"/fonetica/", request, String.class);
        //TODO DEVE ENVIAR VIA POST
        String retornoImportador = responseEntity.getBody();
        return retornoImportador;
    }
}
