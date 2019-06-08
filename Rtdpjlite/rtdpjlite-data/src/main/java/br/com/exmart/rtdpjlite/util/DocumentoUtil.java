package br.com.exmart.rtdpjlite.util;

import br.com.exmart.rtdpjlite.model.*;
import br.com.exmart.rtdpjlite.service.ArquivoService;
import br.com.exmart.rtdpjlite.service.ConfiguracaoService;
import br.com.exmart.rtdpjlite.service.FinanceiroService;
import br.com.exmart.rtdpjlite.service.PDFService;
import br.com.exmart.rtdpjlite.vo.ObjetoProtocoloVO;
import br.com.exmart.rtdpjlite.vo.financeiro.CustasCartorio;
import org.jtwig.JtwigModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class DocumentoUtil {

    @Autowired
    private ConfiguracaoService configuracaoService;
    @Autowired
    private ArquivoService arquivoService;
    @Autowired
    private PDFService pdfService;
    @Autowired
    private FinanceiroService financeiroService;

    public JtwigModel iniciarVariaveisModelo(Registro registro, Protocolo protocolo, List<ChecklistItem> itensNaoChecados, List<CustasCartorio> custasCartorio, String statusNotificacao, Set<ParteProtocolo> partes, List<ObjetoProtocoloVO> objetos, Set<IntimacaoProtocolo> intimacao, CartorioParceiro cartorioParceiro){
        Configuracao configuracao = configuracaoService.findConfiguracao();
        JtwigModel jtwigModel = JtwigModel.newModel()
                .with("NOME_CARTORIO", configuracao.getNomeCartorio())
                .with("CNPJ_CARTORIO", configuracao.getCnpjCartorio())
                .with("DATA_ATUAL", Utils.formatarData(LocalDateTime.now()))
                .with("CHECKLIST", itensNaoChecados)
                .with("STATUS_NOTIFICACAO", statusNotificacao)
                .with("DATA_ATUAL_EXTENSO", Utils.formatarDataExtenso(LocalDate.now()))
                .with("CARTORIO_OFICIAL",configuracao.getOficial())
                .with("CARTORIO_UF",configuracao.getSgCartorio())
                .with("CARTORIO_CIDADE",configuracao.getCidadeCartorio())
                .with("CARTORIO_CEP",configuracao.getCepCartorio())
                .with("CARTORIO_BAIRRO",configuracao.getBairroCartorio())
                .with("CARTORIO_LOGRADOURO",configuracao.getLogradouroCartorio())
                .with("CARTORIO_LOGRADOURO_NUMERO",configuracao.getNumeroLogradouroCartorio())
                .with("CARTORIO_LOGRADOURO_COMPLEMENTO",configuracao.getComplementoLogradouroCartorio());
        if(registro != null) {
            jtwigModel
                    .with("NUMERO_REGISTRO", registro.getNumeroRegistro())
                    .with("REGISTRO", registro.getRegistro())
                    .with("DATA_REGISTRO", Utils.formatarData(registro.getDataRegistro()))
                    .with("DATA_REGISTRO_EXTENSO", Utils.formatarDataExtenso(registro.getDataRegistro().toLocalDate()))
                    .with("NATUREZA", registro.getNatureza().getNome())
                    .with("NUMERO_PASTA", registro.getNrPastaPj())
                    .with("SUB_NATUREZA", registro.getSubNatureza().getNome());
        }
        if(protocolo != null){
            Integer numeroPaginasDocumento = 0;
            if (arquivoService.isDocumentoProtocoloExists(protocolo.getNumero(), TipoProtocolo.recuperaEspecialidade(protocolo.getTipo()), protocolo.getTipo())) {
                try {
                    numeroPaginasDocumento = pdfService.getPaginas(arquivoService.recuperarDocumentoProtocoloFile(protocolo.getNumero(), TipoProtocolo.recuperaEspecialidade(protocolo.getTipo()), protocolo.getTipo()));
                    if (TipoProtocolo.isCertidao(protocolo.getTipo())) {
                        numeroPaginasDocumento ++;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            List<CustasCartorio> custas = new ArrayList<>();
            if(custasCartorio != null) {
                if (TipoProtocolo.isExameCalculo(protocolo.getTipo())) {
                    custas = financeiroService.recuperarCustasServico(protocolo.getServicos(), custasCartorio);
                } else {
                    custas = financeiroService.recuperarCustasServico(financeiroService.listarServicosProtocolo(protocolo.getNumero(), protocolo.getTipo()), custasCartorio);
                }
            }
            BigDecimal totalCustas = BigDecimal.ZERO;
            for(CustasCartorio custa : custas){
                totalCustas = totalCustas.add(custa.getValor());
            }
            jtwigModel
                    .with("NUMERO_PROTOCOLO", protocolo.getNumero())
                    .with("DATA_PROTOCOLO", Utils.formatarData(protocolo.getDataProtocolo()))
                    .with("DATA_PROTOCOLO_EXTENSO", Utils.formatarDataExtenso(protocolo.getDataProtocolo().toLocalDate()))
                    .with("APRESENTANTE_NOME", protocolo.getApresentante())
                    .with("NUMERO_PASTA", protocolo.getPastaPJ())
                    .with("IC_GUARDA_CONSERVACAO", protocolo.getIcGuardaConservacao())
                    .with("DOCUMENTO_NUMERO", protocolo.getNrContrato())
                    .with("DOCUMENTO_DATA", Utils.formatarData(protocolo.getDataDoc()))
                    .with("DOCUMENTO_VALOR", protocolo.getValor())
                    .with("IC_POSSUI_SIGILO_LEGAL", protocolo.getIcPossuiSigiloLegal())
                    .with("APRESENTANTE_DOCUMENTO", protocolo.getApresentanteRg())
                    .with("APRESENTANTE_TELEFONE", protocolo.getTelefone())
                    .with("REGISTRO_REFERENCIA", protocolo.getNumeroRegistroReferencia())
                    .with("NATUREZA", protocolo.getNatureza().getNome())
                    .with("CUSTAS", custas)
                    .with("ESPECIALIDADE", TipoProtocolo.recuperaEspecialidade(protocolo.getTipo()))
                    .with("TOTAL_CUSTAS"," R$: " + br.com.exmart.rtdpjlite.util.Utils.formataValor(totalCustas))
                    .with("PAGINAS", numeroPaginasDocumento);
            if(protocolo.getSubNatureza() != null)
                jtwigModel.with("SUB_NATUREZA", protocolo.getSubNatureza().getNome());
        }
        if(partes != null){
            jtwigModel.with("PARTES",partes);
        }
        if(cartorioParceiro != null){
            jtwigModel.with("CARTORIO_PARCEIRO",cartorioParceiro);
        }
        if(objetos != null){
            jtwigModel.with("OBJETOS",objetos);
        }
        if(intimacao != null){
            jtwigModel.with("INTIMACOES",intimacao);
        }
        if(protocolo.getCliente() != null){
            jtwigModel.with("CLIENTE",protocolo.getCliente());
        }
        return jtwigModel;
    }

}
