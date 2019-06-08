package br.com.exmart.rtdpjlite.vo.indisponibilidade;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PessoaXMLVO {
    private Long id;
    private String nome;
    private String nome_fonetico;

    private String cpf_cnpj;
    private Set<QuotaXMLVO> quotas;
    private List<IndisponibilidadePessoaProtocoloVO> protocolos;
    private List<IndisponibilidadePessoaRegistroNegativoVO> registros;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome_fonetico() {
        return nome_fonetico;
    }

    public void setNome_fonetico(String nome_fonetico) {
        this.nome_fonetico = nome_fonetico;
    }

    public String getCpf_cnpj() {
        return cpf_cnpj;
    }

    public void setCpf_cnpj(String cpf_cnpj) {
        this.cpf_cnpj = cpf_cnpj;
    }

    public Set<QuotaXMLVO> getQuotas() {
        return quotas;
    }

    public void setQuotas(Set<QuotaXMLVO> quotas) {
        this.quotas = quotas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<IndisponibilidadePessoaProtocoloVO> getProtocolos() {
        return protocolos;
    }

    public void setProtocolos(List<IndisponibilidadePessoaProtocoloVO> protocolos) {
        this.protocolos = protocolos;
    }

    public List<IndisponibilidadePessoaRegistroNegativoVO> getRegistros() {
        return registros;
    }

    public void setRegistros(List<IndisponibilidadePessoaRegistroNegativoVO> registros) {
        this.registros = registros;
    }

    public List<Long> getProtocolosEncontrados() {
        List<Long> retorno = new ArrayList<>();
        retorno.add(999999999L);
        for(IndisponibilidadePessoaProtocoloVO protocolo: protocolos){
            retorno.add(protocolo.getProtocoloId());
        }
        return retorno;
    }
    public List<String> getRegistrosEncontrados() {
        List<String> retorno = new ArrayList<>();
        retorno.add("0");
        for(IndisponibilidadePessoaProtocoloVO protocolo: protocolos){
            retorno.add(protocolo.getRegistroReferencia());
        }
        return retorno;
    }
    public List<String> getRegistrosNegativos() {
        List<String> retorno = new ArrayList<>();
        retorno.add("0");
        for(IndisponibilidadePessoaRegistroNegativoVO registro: registros){
            retorno.add(registro.getRegistroReferencia());
        }
        return retorno;
    }
}
