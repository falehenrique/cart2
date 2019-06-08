package br.com.exmart.rtdpjlite.service;

import br.com.exmart.rtdpjlite.model.Cliente;
import br.com.exmart.rtdpjlite.model.TipoProtocolo;
import br.com.exmart.rtdpjlite.model.Usuario;
import br.com.exmart.rtdpjlite.service.interceptor.MyClientHttpRequestInterceptor;
import br.com.exmart.rtdpjlite.service.rest.balcaoonline.PedidoService;
import br.com.exmart.rtdpjlite.vo.financeiro.*;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class FinanceiroService {

    @Value("${cdCartorioNaturezaTd}")
    private Integer cdCartorioNaturezaTd;
    @Value("${cdCartorioNaturezaPj}")
    private Integer cdCartorioNaturezaPj;
    @Value("${urlFinanceiro}")
    private String urlFinanceiro;
    private String naturezaTd = "Títulos e Documentos";
    private String naturezaPJ = "Pessoa Jurídica";

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private UsuarioService usuarioService;

    public List<ServicoFinanceiro> listarServicos(TipoProtocolo tipoProtocolo){
        try{
            Integer cdCartorioNatureza = this.cdCartorioNaturezaTd;
            if(TipoProtocolo.isPj(tipoProtocolo)){
                cdCartorioNatureza = this.cdCartorioNaturezaPj;
            }
            ResponseEntity<ServicoFinanceiro[]> responseEntity = restTemplate.getForEntity(this.urlFinanceiro+"/caixa/servicos/"+cdCartorioNatureza, ServicoFinanceiro[].class);
            List<ServicoFinanceiro> retornoImportador = Arrays.asList(responseEntity.getBody());
            return retornoImportador;
        }catch (HttpClientErrorException e){
            return new ArrayList<>();
        }catch (HttpServerErrorException e){
            return new ArrayList<>();
        }
    }

    public List<FormaCalculoFinanceiro> listarFormaCalculo(){
        try {
            ResponseEntity<FormaCalculoFinanceiro[]> responseEntity = restTemplate.getForEntity(this.urlFinanceiro + "/caixa/formacalculo", FormaCalculoFinanceiro[].class);

            List<FormaCalculoFinanceiro> retornoImportador = Arrays.asList(responseEntity.getBody());
            return retornoImportador;
        }catch (HttpClientErrorException e){
            return new ArrayList<>();
        }catch (HttpServerErrorException e){
            return new ArrayList<>();
        }
    }

    public ServicoCalculado calcularServico(Long idServico, BigDecimal vlBase, Integer idFormaCalculo, TipoProtocolo tipoProtocolo){
        ResponseEntity<ServicoCalculado> responseEntity = restTemplate.getForEntity(this.urlFinanceiro+"/caixa/calcular/"+recuperarNatureza(tipoProtocolo)+"/"+idServico+"/"+vlBase+"/"+idFormaCalculo, ServicoCalculado.class);
        ServicoCalculado retornoImportador = responseEntity.getBody();
        return retornoImportador;
    }

    public boolean excluirServico(Long idProtocoloServico){
        try {
            String url = this.urlFinanceiro + "/caixa/protocolo/servico/" + idProtocoloServico;
            restTemplate.delete(url);
            return true;
        }catch (HttpClientErrorException e){
            return false;
        }
    }

    public Boolean protocolar(Long protocoloNumero, String apresentante, String apresentanteRg, String idAtendimento, List<ServicoCalculado> servicos, TipoProtocolo tipoProtocolo, BigDecimal vlDepositoPrevio) {
        try {
            if(Strings.isNullOrEmpty(apresentanteRg)){
                apresentanteRg = "Nao informado";
            }
            if(Strings.isNullOrEmpty(apresentante)){
                apresentante = "Nao informado";
            }
            if(Strings.isNullOrEmpty(idAtendimento)){
                idAtendimento = protocoloNumero.toString();
            }
            PedidoFinanceiro pedido = new PedidoFinanceiro(protocoloNumero,recuperarNatureza(tipoProtocolo), apresentante, apresentanteRg,idAtendimento ,TipoProtocolo.isCertidao(tipoProtocolo), servicos, vlDepositoPrevio);
            String url = this.urlFinanceiro + "/caixa/protocolar/";// + protocoloNumero + "/" + recuperarNatureza(tipoProtocolo) + "/" + apresentante + "/" + apresentanteRg + "/" + idAtendimento + "/" + TipoProtocolo.isCertidao(tipoProtocolo);
            HttpEntity<PedidoFinanceiro> requestUpdate = new HttpEntity<PedidoFinanceiro>(pedido, null);
            ResponseEntity<Boolean> responseEntity = restTemplate.postForEntity(url, requestUpdate, Boolean.class);

            return responseEntity.getBody();
        }catch (HttpClientErrorException e){
            return false;
        }
    }


    public Boolean faturarProtocoloCliente(Long protocolo, Cliente cliente, Usuario usuarioLogado, TipoProtocolo tipoProtocolo){
        FaturarProtocolo faturarProtocolo = new FaturarProtocolo(protocolo,cliente.getNome(), cliente.getDocumento(), cliente.getNomeFantasia(),cliente.getApresentante(), usuarioLogado.getId(),recuperarNatureza(tipoProtocolo), TipoProtocolo.isCertidao(tipoProtocolo));

        String url = this.urlFinanceiro + "/caixa/protocolo/faturar/";
        HttpEntity<FaturarProtocolo> requestUpdate = new HttpEntity<FaturarProtocolo>(faturarProtocolo, null);
        ResponseEntity<Boolean> responseEntity = restTemplate.postForEntity(url, requestUpdate, Boolean.class);
        return responseEntity.getBody();
    }


    public List<ServicoCalculado> listarServicosProtocolo(Long protocoloNumero, TipoProtocolo tipoProtocolo) {
        try{
            String url =  this.urlFinanceiro+"/caixa/protocolo/"+protocoloNumero+"/"+recuperarNatureza(tipoProtocolo)+"/"+TipoProtocolo.isCertidao(tipoProtocolo)+ "/servicos";
            ResponseEntity<ServicoCalculado[]> responseEntity = restTemplate.getForEntity(url, ServicoCalculado[].class);

            List<ServicoCalculado> retornoImportador = Arrays.asList(responseEntity.getBody());
            return retornoImportador;
        }catch (HttpClientErrorException e){
            return new ArrayList<>();
        }catch (HttpServerErrorException e){
            return new ArrayList<>();
        }
    }

    public BigDecimal somarServicosProtocolo(Long protocoloNumero,  TipoProtocolo tipoProtocolo) {
        try{
            String url =  this.urlFinanceiro+"/caixa/protocolo/"+protocoloNumero+"/"+recuperarNatureza(tipoProtocolo)+"/"+TipoProtocolo.isCertidao(tipoProtocolo)+ "/servicos";
            ResponseEntity<ServicoCalculado[]> responseEntity = restTemplate.getForEntity(url, ServicoCalculado[].class);

            List<ServicoCalculado> retornoImportador = Arrays.asList(responseEntity.getBody());
            BigDecimal retorno = BigDecimal.ZERO;
            for(ServicoCalculado servico : retornoImportador){
                retorno = retorno.add(servico.getTotal());
            }
            return retorno;
        }catch (HttpClientErrorException e){
            return BigDecimal.ZERO;
        }catch (HttpServerErrorException e){
            return BigDecimal.ZERO;
        }
    }

    public List<PagamentoProtocoloFinanceiro> listarPagamentosProtocolo(Long protocoloNumero, TipoProtocolo tipoProtocolo) {
        try{
            String url =  this.urlFinanceiro+"/caixa/protocolo/"+protocoloNumero+"/"+recuperarNatureza(tipoProtocolo)+"/"+TipoProtocolo.isCertidao(tipoProtocolo)+ "/pagamentos";
            ResponseEntity<PagamentoProtocoloFinanceiro[]> responseEntity = restTemplate.getForEntity(url, PagamentoProtocoloFinanceiro[].class);

            List<PagamentoProtocoloFinanceiro> retornoImportador = Arrays.asList(responseEntity.getBody());
            return retornoImportador;
        }catch (HttpClientErrorException e){
            return new ArrayList<>();
        }catch (HttpServerErrorException e){
            return new ArrayList<>();
        }
    }

    public Boolean marcarComoDevolvido(Long protocoloNumero,  TipoProtocolo tipoProtocolo) {
        try{
            String url =  this.urlFinanceiro+"/caixa/protocolo/"+protocoloNumero+"/"+recuperarNatureza(tipoProtocolo)+"/"+TipoProtocolo.isCertidao(tipoProtocolo)+ "/devolvido";
            ResponseEntity<Boolean> responseEntity = restTemplate.postForEntity(url,null, Boolean.class);
            return responseEntity.getBody();
        }catch (HttpClientErrorException e){
            return false;
        }
    }

    private String recuperarNatureza(TipoProtocolo tipoProtocolo) {
        String natureza;
        if(TipoProtocolo.isPj(tipoProtocolo)) {
            natureza = this.naturezaPJ;
        }else{
            natureza = this.naturezaTd;
        }
        return natureza;
    }

    public Boolean marcarComoConcluido(Long protocoloNumero, TipoProtocolo tipoProtocolo, Long numeroRegistro) {
        try{
            String url =  this.urlFinanceiro+"/caixa/protocolo/"+protocoloNumero+"/"+recuperarNatureza(tipoProtocolo)+"/"+TipoProtocolo.isCertidao(tipoProtocolo)+ "/concluido/"+numeroRegistro;
            ResponseEntity<Boolean> responseEntity = restTemplate.postForEntity(url,null, Boolean.class);
            return responseEntity.getBody();
        }catch (HttpClientErrorException e){
            return false;
        }
    }
    public List<CustaProtocolo> getCustasProtocolo(Long protocoloNumero, TipoProtocolo tipoProtocolo){
        String url =  this.urlFinanceiro+"/caixa/protocolo/"+protocoloNumero+"/"+recuperarNatureza(tipoProtocolo)+"/"+TipoProtocolo.isCertidao(tipoProtocolo)+ "/custas";
        ResponseEntity<CustaProtocolo[]> responseEntity = restTemplate.getForEntity(url, CustaProtocolo[].class);

        List<CustaProtocolo> retornoImportador = Arrays.asList(responseEntity.getBody());
        return retornoImportador;
    }

    public List<CustasCartorio> findAllCustas() {
        String url =  this.urlFinanceiro+"/caixa/custas";
        ResponseEntity<CustasCartorio[]> responseEntity = restTemplate.getForEntity(url, CustasCartorio[].class);

        List<CustasCartorio> retornoImportador = Arrays.asList(responseEntity.getBody());
        return retornoImportador;
    }

    public List<ServicoEtiqueta> getEtiquetasServico(Long idServico){
        String url =  this.urlFinanceiro+"/caixa/servicos/"+idServico+"/etiqueta";
        ResponseEntity<ServicoEtiqueta[]> responseEntity = restTemplate.getForEntity(url, ServicoEtiqueta[].class);

        List<ServicoEtiqueta> retornoImportador = Arrays.asList(responseEntity.getBody());
        return retornoImportador;
    }


    public List<CustasCartorio> recuperarCustasServico(List<ServicoCalculado> servicos, List<CustasCartorio> custas) {
        for(CustasCartorio custa: custas){
            custa.setValor(BigDecimal.ZERO);
        }
        Class<ServicoCalculado> classeServico = ServicoCalculado.class;
        for(ServicoCalculado servico : servicos){
                for(CustasCartorio custa : custas){
                    Method metodo = null;
                    try {
                        metodo = classeServico.getMethod("getCustas"+custa.getNumero());
                        custa.setValor(custa.getValor().add((BigDecimal) metodo.invoke(servico)));
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
        }
        return custas;
    }
}
