package br.com.exmart.indicadorRTDPJ.controller;


        import br.com.exmart.indicadorRTDPJ.ui.util.Utils;
        import br.com.exmart.rtdpjlite.model.Protocolo;
        import br.com.exmart.rtdpjlite.model.Registro;
        import br.com.exmart.rtdpjlite.model.StatusProtocolo;
        import br.com.exmart.rtdpjlite.model.TipoProtocolo;
        import br.com.exmart.rtdpjlite.repository.ProtocoloArquivoHashRepository;
        import br.com.exmart.rtdpjlite.service.*;
        import br.com.exmart.rtdpjlite.vo.financeiro.CustasCartorio;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Controller;
        import org.springframework.ui.Model;
        import org.springframework.web.bind.annotation.PathVariable;
        import org.springframework.web.bind.annotation.RequestMapping;
        import org.springframework.web.bind.annotation.RequestMethod;

        import java.io.IOException;
        import java.math.BigDecimal;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.logging.Logger;

@Controller
@RequestMapping("/registro")
public class ReciboController {

    @Autowired
    private RegistroService registroService;
    @Autowired
    private ConfiguracaoService configuracaoService;
    @Autowired
    private ProtocoloArquivoHashRepository protocoloArquivoHashRepository;
    @Autowired
    private ProtocoloService protocoloService;
    @Autowired
    private FinanceiroService financeiroService;
    @Autowired
    private ArquivoService arquivoService;
    @Autowired
    private PDFService pdfService;


    @RequestMapping(method = RequestMethod.GET, value = "/{idTipo}/{registro}/imprimir/capa")
    public String imprimir_capa(Model model, @PathVariable Integer idTipo, @PathVariable String registro){
        Registro registroE = registroService.findByRegistroAndEspecialidade(registro, TipoProtocolo.fromCode(idTipo));
        preencherAtributosCapa(model, registroE);

        Integer paginas = 0;
        try {
            paginas = pdfService.getPaginas(arquivoService.recuperarDocumentoProtocoloRegistroFile(registro, registroE.getNumeroRegistro(), registroE.getEspecialidade(), TipoProtocolo.fromCode(idTipo)));
        }catch (Exception e ){
            e.printStackTrace();
        }
        model.addAttribute("folhaLivroExt", String.format("%02d", paginas-1));
        return "caparegistro";
    }

    private Model preencherAtributosCapa(Model model, Registro registro){
        model.addAttribute("registro", registro);

        Protocolo prot = protocoloService.findById(registro.getProtocolo());
        String numero_protocolo = prot.getNumero().toString();
        model.addAttribute("nr_protocolo", numero_protocolo);

//        model.addAttribute("folhaLivroExt", "XX".toString() );
//        model.addAttribute("folhaLivro", "XX".toString() );
        model.addAttribute("dataRegistroExt", Utils.formatarDataExtenso(registro.getDataRegistro().toLocalDate()) );

        String validaUrl = configuracaoService.findConfiguracao().getUrlQrcode();
        String hash = protocoloArquivoHashRepository.findByProtocolo(registro.getProtocolo()).getHash();

        model.addAttribute("validaUrl", validaUrl);
        model.addAttribute("hashCompleto", validaUrl + hash );
        model.addAttribute("hash", hash );

        model.addAttribute("quantidadeVias", new Integer[2]);

        return model;
    }


    @RequestMapping(method = RequestMethod.GET, value = "/{idTipo}/{nrProtocolo}/imprimir/recibo")
    public String imprimir_recibo(Model model, @PathVariable Integer idTipo, @PathVariable Long nrProtocolo){

        Protocolo protocoloRec = protocoloService.findByNumeroAndTipoProtocolo(nrProtocolo, TipoProtocolo.fromCode(idTipo));
        preencherAtributosRecibo(model, protocoloRec);

        return "reciboregistro";
    }


    private Model preencherAtributosRecibo(Model model, Protocolo protocolo){
        model.addAttribute("protocolo", protocolo);

//        model.addAttribute("totalPagamento", protocolo.getPagamentosTotal().getCustaFormatted());
//        if(protocolo.getSaldo().getValor().isNegative()) {
//            model.addAttribute("labelSaldo", "Total a Devolver");
//        }else{
//            model.addAttribute("labelSaldo", "Total a Receber");
//        }

        model.addAttribute("quantidadeVias", new Integer[2]);
        model.addAttribute("tipoProtocolo", Utils.getNomeCorretoFromTipoProtocolo(protocolo.getTipo()));
        List<CustasCartorio> custas = new ArrayList<>();
        if(protocolo.getTipo().equals(TipoProtocolo.EXAMECALCULO_PJ) || (protocolo.getTipo().equals(TipoProtocolo.EXAMECALCULO_PJ))){
            custas = financeiroService.recuperarCustasServico(protocolo.getServicos(), financeiroService.findAllCustas());
        }else{
            custas = financeiroService.recuperarCustasServico(financeiroService.listarServicosProtocolo(protocolo.getNumero(), protocolo.getTipo()), financeiroService.findAllCustas());
        }
        BigDecimal totalProtocolo = BigDecimal.ZERO;
        for(CustasCartorio custa : custas){
            totalProtocolo = totalProtocolo.add(custa.getValor());
        }
        model.addAttribute("custas", custas);
//        model.addAttribute("saldo", protocolo.getSaldo().getCustaFormatted());
        model.addAttribute("saldo",totalProtocolo);

        return model;
    }
}
