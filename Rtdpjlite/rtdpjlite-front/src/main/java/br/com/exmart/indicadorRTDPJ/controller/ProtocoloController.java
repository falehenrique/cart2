package br.com.exmart.indicadorRTDPJ.controller;


import br.com.exmart.indicadorRTDPJ.ui.util.Utils;
import br.com.exmart.rtdpjlite.model.Protocolo;
import br.com.exmart.rtdpjlite.model.Registro;
import br.com.exmart.rtdpjlite.model.StatusProtocolo;
import br.com.exmart.rtdpjlite.model.TipoProtocolo;
import br.com.exmart.rtdpjlite.service.*;
import br.com.exmart.rtdpjlite.util.DocumentoUtil;
import br.com.exmart.rtdpjlite.vo.financeiro.CustasCartorio;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.w3c.tidy.Tidy;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/protocolo")
public class ProtocoloController {

    @Autowired
    private ProtocoloService protocoloService;
    @Autowired
    private FinanceiroService financeiroService;
    @Autowired
    private ConfiguracaoService configuracaoService;
    @Autowired
    private DocumentoUtil documentoUtil;
    @Autowired
    private PDFService pdfService;
    @Autowired
    private RegistroService registroService;

    //    https://ankushs92.github.io/tutorial/2016/05/03/pagination-with-spring-boot.html

    @GetMapping(value = "/")

    @RequestMapping(method = RequestMethod.GET, value = "/{idTipo}/{nrProtocolo}/checklistintimacao")
    @ResponseBody
    public ResponseEntity<org.springframework.core.io.Resource> gerarChecklistIntimacao(@PathVariable Integer idTipo, @PathVariable Long nrProtocolo) throws Exception {

        String checklist = configuracaoService.findConfiguracao().getDsIntimacaoChecklist();
        protocoloService.findByNumeroAndTipoProtocolo(nrProtocolo, TipoProtocolo.fromCode(idTipo));
        Protocolo protocolo = protocoloService.findByNumeroAndTipoProtocolo(nrProtocolo, TipoProtocolo.fromCode(idTipo));
        if(protocolo == null)
            throw new Exception("Protocolo não encontrado");
        Registro registro = registroService.findByProtocoloIdAndEspecialidade(protocolo.getId(), TipoProtocolo.recuperaEspecialidade(protocolo.getTipo()));

        JtwigModel jtwigModel = documentoUtil.iniciarVariaveisModelo(registro,
                protocolo,
                null,
                null,
                null,
                protocolo.getPartesProtocolo(),
                null,
                null,
                null
                );
        JtwigTemplate jtwigTemplate =JtwigTemplate.inlineTemplate(checklist);
        String textoHtml = "<html><body>"+ tidyHtml(jtwigTemplate.render(jtwigModel))+"</body></html>";
        File temp = File.createTempFile(nrProtocolo+"_"+idTipo,".PDF");
        pdfService.htmlToPdf(textoHtml, temp.getPath());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + temp.getName() + "\"")
                .body(new InputStreamResource(new FileInputStream(temp)));

    }

    @RequestMapping(method = RequestMethod.GET, value = "/{idTipo}/{nrProtocolo}/comparecimento")
    @ResponseBody
    public ResponseEntity<InputStreamResource> gerarComparecimentoIntimacao(@PathVariable Integer idTipo, @PathVariable Long nrProtocolo) throws Exception {
        String checklist = configuracaoService.findConfiguracao().getDsIntimacaoComparecimento();
        protocoloService.findByNumeroAndTipoProtocolo(nrProtocolo, TipoProtocolo.fromCode(idTipo));
        Protocolo protocolo = protocoloService.findByNumeroAndTipoProtocolo(nrProtocolo, TipoProtocolo.fromCode(idTipo));
        if(protocolo == null)
            throw new Exception("Protocolo não encontrado");
        Registro registro = registroService.findByProtocoloIdAndEspecialidade(protocolo.getId(), TipoProtocolo.recuperaEspecialidade(protocolo.getTipo()));

        JtwigModel jtwigModel = documentoUtil.iniciarVariaveisModelo(registro,
                protocolo,
                null,
                null,
                null,
                protocolo.getPartesProtocolo(),
                null,
                null,
                null
        );
        JtwigTemplate jtwigTemplate =JtwigTemplate.inlineTemplate(checklist);
        String textoHtml = "<html><body>"+ tidyHtml(jtwigTemplate.render(jtwigModel))+"</body></html>";
        File temp = File.createTempFile(nrProtocolo+"_"+idTipo,".PDF");
        pdfService.htmlToPdf(textoHtml, temp.getPath());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + temp.getName() + "\"")
                .body(new InputStreamResource(new FileInputStream(temp)));
    }


    @RequestMapping(method = RequestMethod.GET, value = "/{idTipo}/{nrProtocolo}/imprimir")
    public String imprimir(Model model, @PathVariable Integer idTipo, @PathVariable Long nrProtocolo){
        Protocolo protocolo = protocoloService.findByNumeroAndTipoProtocolo(nrProtocolo, TipoProtocolo.fromCode(idTipo));
        preencherAtributosPeticao(model, protocolo);
        return "protocolorecepcao";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{idTipo}/{nrProtocolo}/devolucao/{idStatus}")
    public String imprimirDevolucao(Model model,@PathVariable Integer idTipo, @PathVariable Long nrProtocolo, @PathVariable Long idStatus) throws IOException {
        Protocolo protocolo = protocoloService.findByNumeroAndTipoProtocolo(nrProtocolo, TipoProtocolo.fromCode(idTipo));
        StatusProtocolo devolucao = null;
        for(StatusProtocolo status : protocolo.getStatusProtocolo()){
            if(status.getId().equals(idStatus)){
                devolucao = status;
            }
        }
        if(devolucao != null) {
            model.addAttribute("notaDevolucao",devolucao.getConteudo().getTexto() );
        }
        return "notadevolucao";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{idTipo}/{nrProtocolo}/apto")
    public String imprimirApto(Model model, @PathVariable Integer idTipo,@PathVariable Long nrProtocolo) throws IOException {
        Protocolo protocolo = protocoloService.findByNumeroAndTipoProtocolo(nrProtocolo, TipoProtocolo.fromCode(idTipo));
        StatusProtocolo apto = null;
        for(StatusProtocolo status : protocolo.getStatusProtocolo()){
            if(status.getStatus().getNome().equalsIgnoreCase("APTO PARA REGISTRO")){
                apto = status;
            }
        }
        if(apto != null) {
            model.addAttribute("notaDevolucao",apto.getConteudo().getTexto() );
        }
        return "aptoregistro";
    }

    private Model preencherAtributosPeticao(Model model, Protocolo protocolo){
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
        model.addAttribute("vlDeposito", protocolo.getVlDepositoPrevio());
//        model.addAttribute("saldo", protocolo.getSaldo().getCustaFormatted());
        model.addAttribute("saldo",totalProtocolo);

        return model;
    }

    public static String tidyHtml(String htmlString) {
        Tidy tidy = new Tidy();
        tidy.setDocType("omit");
        tidy.setXmlOut(true);
        tidy.setQuiet(true);
        tidy.setTidyMark(false);
        tidy.setWraplen(100_000);
        StringWriter swError = new StringWriter();
        tidy.setErrout(new PrintWriter(swError));
        StringWriter sw = new StringWriter();
        tidy.parse(new StringReader(htmlString), sw);
        String tidyHtml = sw.getBuffer().toString();
        tidyHtml = tidyHtml.replaceAll("(\r\n|\n|\r)+\\s*<", "<");
        return tidyHtml;
    }
}
