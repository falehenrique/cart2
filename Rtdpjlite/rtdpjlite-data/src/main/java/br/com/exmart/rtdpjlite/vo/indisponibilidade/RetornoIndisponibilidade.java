package br.com.exmart.rtdpjlite.vo.indisponibilidade;

import java.util.ArrayList;
import java.util.List;

public class RetornoIndisponibilidade {
    List<ParteIndisponivel> partesEncontradasIndisponibilidade = new ArrayList<>();
    List<ParteIndisponivel> partesEncontradasCancelamento = new ArrayList<>();
    List<ParteIndisponivel> partesEncontradasIndisponibilidadeSemDocumento = new ArrayList<>();
    List<ParteIndisponivel> partesEncontradasCancelamentoSemDocumento  = new ArrayList<>();
    List<ParteIndisponivel> partesNaoEncontradas  = new ArrayList<>();

    public RetornoIndisponibilidade(List<ParteIndisponivel> partesEncontradasIndisponibilidade, List<ParteIndisponivel> partesEncontradasCancelamento, List<ParteIndisponivel> partesEncontradasIndisponibilidadeSemDocumento, List<ParteIndisponivel> partesEncontradasCancelamentoSemDocumento, List<ParteIndisponivel> partesNaoEncontradas) {
        this.partesEncontradasIndisponibilidade = partesEncontradasIndisponibilidade;
        this.partesEncontradasCancelamento = partesEncontradasCancelamento;
        this.partesEncontradasIndisponibilidadeSemDocumento = partesEncontradasIndisponibilidadeSemDocumento;
        this.partesEncontradasCancelamentoSemDocumento = partesEncontradasCancelamentoSemDocumento;
        this.partesNaoEncontradas = partesNaoEncontradas;
    }

    public List<ParteIndisponivel> getPartesEncontradasIndisponibilidade() {
        return partesEncontradasIndisponibilidade;
    }

    public void setPartesEncontradasIndisponibilidade(List<ParteIndisponivel> partesEncontradasIndisponibilidade) {
        this.partesEncontradasIndisponibilidade = partesEncontradasIndisponibilidade;
    }

    public List<ParteIndisponivel> getPartesEncontradasCancelamento() {
        return partesEncontradasCancelamento;
    }

    public void setPartesEncontradasCancelamento(List<ParteIndisponivel> partesEncontradasCancelamento) {
        this.partesEncontradasCancelamento = partesEncontradasCancelamento;
    }

    public List<ParteIndisponivel> getPartesEncontradasIndisponibilidadeSemDocumento() {
        return partesEncontradasIndisponibilidadeSemDocumento;
    }

    public void setPartesEncontradasIndisponibilidadeSemDocumento(List<ParteIndisponivel> partesEncontradasIndisponibilidadeSemDocumento) {
        this.partesEncontradasIndisponibilidadeSemDocumento = partesEncontradasIndisponibilidadeSemDocumento;
    }

    public List<ParteIndisponivel> getPartesEncontradasCancelamentoSemDocumento() {
        return partesEncontradasCancelamentoSemDocumento;
    }

    public void setPartesEncontradasCancelamentoSemDocumento(List<ParteIndisponivel> partesEncontradasCancelamentoSemDocumento) {
        this.partesEncontradasCancelamentoSemDocumento = partesEncontradasCancelamentoSemDocumento;
    }

    public List<ParteIndisponivel> getPartesNaoEncontradas() {
        return partesNaoEncontradas;
    }

    public void setPartesNaoEncontradas(List<ParteIndisponivel> partesNaoEncontradas) {
        this.partesNaoEncontradas = partesNaoEncontradas;
    }
}
