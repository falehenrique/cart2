package br.com.exmart.rtdpjlite.service.rest.seloeletronico;

import br.com.exmart.rtdpjlite.exception.ErrorListException;
import br.com.exmart.rtdpjlite.exception.NotUseSelo;
import br.com.exmart.rtdpjlite.model.Protocolo;
import br.com.exmart.rtdpjlite.service.ConfiguracaoService;
import br.com.exmart.rtdpjlite.util.RetornoSelo;
import br.com.exmart.rtdpjlite.vo.financeiro.ServicoCalculado;
import br.com.exmart.rtdpjlite.vo.seloeletronico.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class SeloEletronicoRest {

    @Autowired
    private ConfiguracaoService configuracaoService;
    @Value("${urlSeloEletronico}")
    private String urlSeloEletronico;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ObjectMapper jsonConverter;


    public void utilizaSelo() throws NotUseSelo {
        if(!configuracaoService.findConfiguracao().getIcSeloEletronico()){
            throw new NotUseSelo();
        }
    }

    public RetornoSelo gerarSelo(SeloRTDPJ selo) throws ErrorListException, HttpClientErrorException, NotUseSelo {
        utilizaSelo();
        try {
            HttpEntity<SeloRTDPJ> entity = new HttpEntity<SeloRTDPJ>(selo);
            ResponseEntity<RetornoSelo> retorno = restTemplate.postForEntity(urlSeloEletronico + "/selo/rtdpj/", entity, RetornoSelo.class);
            return retorno.getBody();
        }catch (HttpClientErrorException e){
            e.printStackTrace();
            throw new HttpClientErrorException(e.getStatusCode());
        }catch (HttpServerErrorException e ){
            try {
                ApiError retorno = jsonConverter.readValue(e.getResponseBodyAsString(), ApiError.class);
                throw new ErrorListException("Erro ao válidar selo", retorno.getErrors());
            } catch (IOException e1) {
                e1.printStackTrace();
                throw new ErrorListException("Erro ao válidar selo", Arrays.asList(e1.getMessage()));
            }
        }
    }

    public List<InformeSelosVO> listarSeloEnvio() throws NotUseSelo {
        utilizaSelo();
        ResponseEntity<InformeSelosVO[]> retorno = restTemplate.getForEntity(urlSeloEletronico + "/selo/rtdpj/envio", InformeSelosVO[].class);
        return Arrays.asList(retorno.getBody());
    }

    public boolean validarSelo(Protocolo protocolo, List<ServicoCalculado> servicos, List<SeloParteRTDPJ> partes) throws ErrorListException, NotUseSelo {
        utilizaSelo();
        HttpEntity<SeloRTDPJ> entity = new HttpEntity<SeloRTDPJ>(new SeloRTDPJ(protocolo, null, configuracaoService.findConfiguracao().getCodigoCnsCartorio(), null, servicos, partes));
        try {
            ResponseEntity<Boolean> retorno = restTemplate.postForEntity(urlSeloEletronico + "/selo/rtdpj/validar", entity, Boolean.class);
        }catch (HttpClientErrorException e ){
            try {
                ApiError retorno = jsonConverter.readValue(e.getResponseBodyAsString(), ApiError.class);
                throw new ErrorListException("Erro ao válidar selo", retorno.getErrors());
            } catch (IOException e1) {
                e1.printStackTrace();
                throw new ErrorListException("Erro ao válidar selo", Arrays.asList(e1.getMessage()));
            }
        }catch (HttpServerErrorException e ){
            try {
                ApiError retorno = jsonConverter.readValue(e.getResponseBodyAsString(), ApiError.class);
                throw new ErrorListException("Erro ao válidar selo", retorno.getErrors());
            } catch (IOException e1) {
                e1.printStackTrace();
                throw new ErrorListException("Erro ao válidar selo", Arrays.asList(e1.getMessage()));
            }
        }

        return true;
    }

    public List<InformeSelosVO> listarSelo(String tipo) {
        ResponseEntity<InformeSelosVO[]> retorno = restTemplate.getForEntity(urlSeloEletronico + "/selo/rtdpj/"+tipo, InformeSelosVO[].class);
        return Arrays.asList(retorno.getBody());
    }

//    public List<InformeSelosVO> listarSeloReenvio() {
//        ResponseEntity<InformeSelosVO[]> retorno = restTemplate.getForEntity(urlSeloEletronico + "/selo/rtdpj/reenvio", InformeSelosVO[].class);
//        return Arrays.asList(retorno.getBody());
//    }
//
    public List<InformeSelosRetornoVO> enviarSelo() {
        ResponseEntity<InformeSelosRetornoVO[]> retorno = restTemplate.exchange(urlSeloEletronico + "/selo/rtdpj/envio",HttpMethod.PUT,null, InformeSelosRetornoVO[].class);
        return Arrays.asList(retorno.getBody());
    }
}
