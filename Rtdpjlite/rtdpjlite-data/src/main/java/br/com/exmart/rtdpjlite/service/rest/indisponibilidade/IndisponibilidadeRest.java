package br.com.exmart.rtdpjlite.service.rest.indisponibilidade;

import br.com.exmart.rtdpjlite.util.Utils;
import br.com.exmart.rtdpjlite.vo.RestException;
import br.com.exmart.rtdpjlite.vo.indisponibilidade.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class IndisponibilidadeRest {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${urlIndisponibilidade}")
    private String urlServico;
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public List<IndisponibilidadeVO> recuperarUltimosAdicionados(LocalDateTime dataUltimaConsulta) throws Exception {
        if(dataUltimaConsulta == null){
            throw new Exception("Data obrigatório");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        logger.debug(Utils.formatarDataComHora(dataUltimaConsulta));

        ResponseEntity<IndisponibilidadeVO[]> response = restTemplate.getForEntity(urlServico + "/indisponibilidade/oficio/rtdpj", IndisponibilidadeVO[].class);

        return Arrays.asList(response.getBody());
    }

    public boolean informarProtocolado(Long idParte, List<IndisponibilidadePessoaProtocoloVO> protocolos){
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<List<IndisponibilidadePessoaProtocoloVO>> requestEntity = new HttpEntity<List<IndisponibilidadePessoaProtocoloVO>>(protocolos, headers);
        ResponseEntity<Boolean> retorno = restTemplate.exchange(urlServico+ "/indisponibilidade/oficio/pessoa/"+idParte+"/protocolo", HttpMethod.PUT, requestEntity, Boolean.class);
        return retorno.getBody();
    }

    public boolean marcarComoFinalizado(Long idParte){
//        HttpHeaders headers = new HttpHeaders();
//        HttpEntity<List<IndisponibilidadePessoaProtocoloVO>> requestEntity = new HttpEntity<List<IndisponibilidadePessoaProtocoloVO>>(protocolos, headers);
        ResponseEntity<Boolean> retorno = restTemplate.exchange(urlServico + "/indisponibilidade/oficio/pessoa/" + idParte + "/finalizado", HttpMethod.PUT, null, Boolean.class);
        return retorno.getBody();
    }

    public boolean informarRegistroNegativo(Long idParte, List<IndisponibilidadePessoaRegistroNegativoVO> registros){
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<List<IndisponibilidadePessoaRegistroNegativoVO>> requestEntity = new HttpEntity<List<IndisponibilidadePessoaRegistroNegativoVO>>(registros, headers);
        ResponseEntity<Boolean> retorno = restTemplate.exchange(urlServico+ "/indisponibilidade/oficio/pessoa/"+idParte+"/registro", HttpMethod.PUT, requestEntity, Boolean.class);
        return retorno.getBody();
    }

    public List<PessoaIndisponibilidadeVO> isIndisponivel(String documento){
        UriComponentsBuilder builder = UriComponentsBuilder
            .fromUriString(this.urlServico + "/indisponibilidade/rtdpj/pessoa")
            // Add query parameter
            .queryParam("cpfCnpj", documento)
            ;
        logger.debug("Verificando indisponibilidade: "+ builder.toUriString());
        ResponseEntity<PessoaIndisponibilidadeVO[]> responseEntity = restTemplate.getForEntity(builder.toUriString(), PessoaIndisponibilidadeVO[].class);
        return Arrays.asList(responseEntity.getBody());
    }

    public ResultadoImportacaoIndisponibilidadeXMLVO enviarArquivoIndisponibilidade(File arquivo) throws Exception {
        try {
            LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
            map.add("file", new FileSystemResource(arquivo.toPath().toString()));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<LinkedMultiValueMap<String, Object>>(
                    map, headers);
            logger.debug(this.urlServico + "/indisponibilidade/upload");
            ResponseEntity<ResultadoImportacaoIndisponibilidadeXMLVO> result = restTemplate.exchange(
                    this.urlServico + "/indisponibilidade/upload", HttpMethod.POST, requestEntity, ResultadoImportacaoIndisponibilidadeXMLVO.class);
            logger.debug(result.getStatusCode().toString());
            return result.getBody();
        }catch (HttpClientErrorException c){
            ObjectMapper mapper = new ObjectMapper();
            RestException error = mapper.readValue(c.getResponseBodyAsString(), RestException.class);
            throw new Exception(error.getMessage());
        } catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
}
