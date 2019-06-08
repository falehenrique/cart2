package br.com.exmart.indicadorRTDPJ.ui.impl;

import br.com.exmart.indicadorRTDPJ.ui.CardParte;
import br.com.exmart.rtdpjlite.model.IndicadorPessoal;

public class CardParteImpl extends CardParte {

    public CardParteImpl(IndicadorPessoal parte) {
        labelNome.setValue(parte.getNome());
        labelDocumento.setValue(parte.getCpfCnpj());
        labelIdentidade.setValue(parte.getNrDocumento());
        labelQualificacao.setValue(parte.getQualidade().getNome());
        if(parte.getVlParticipacao() != null)
            labelVlParticipacao.setValue(parte.getVlParticipacao().toString());
        if (parte.getQtdCotas() != null)
            labelVlCotas.setValue(parte.getQtdCotas().toString());
        labelEmail.setValue(parte.getEmail());
        labelTelefone.setValue(parte.getTelefone());
        if(parte.getNacionalidade() != null)
            labelNacionalidade.setValue(parte.getNacionalidade().getNome());
        if(parte.getEstadoCivil() != null)
            labelEstadoCivil.setValue(parte.getEstadoCivil().getNome());
        if(parte.getProfissao() != null)
            labelProfissao.setValue(parte.getProfissao().getNome());
        labelEndereco.setValue(parte.getEndereco());
        labelEnderecoNumero.setValue(parte.getNumero());
        labelCep.setValue(parte.getCep());
        labelComplemento.setValue(parte.getComplemento());
        labelBairro.setValue(parte.getBairro());
        if(parte.getCidade() != null)
            labelCidade.setValue(parte.getCidade().getNome());
        if(parte.getEstado() != null)
            labelCidade.setValue(parte.getEstado().getNome());


        btnDetalhes.addClickListener(evt-> detalhes());

    }

    private void detalhes() {
        panelDetalhes.setVisible(!panelDetalhes.isVisible());
        // TODO trocar entre "VaadinIcons.PLUS_CIRCLE_O" e "VaadinIcons.MINUS_CIRCLE_O"


    }

}
