package br.com.exmart.indicadorRTDPJ.ui.impl;

import br.com.exmart.indicadorRTDPJ.MyUI;
import br.com.exmart.indicadorRTDPJ.ui.ModalRegistro;
import br.com.exmart.indicadorRTDPJ.ui.custom.event.ProtocoloEvent;
import br.com.exmart.rtdpjlite.model.*;
import br.com.exmart.rtdpjlite.service.*;
import br.com.exmart.rtdpjlite.service.enums.Diretorio;
import br.com.exmart.rtdpjlite.util.DocumentoUtil;
import br.com.exmart.rtdpjlite.vo.ObjetoProtocoloVO;
import br.com.exmart.rtdpjlite.vo.StatusProtocoloJson;
import br.com.exmart.rtdpjlite.vo.financeiro.CustasCartorio;
import br.com.exmart.util.BeanLocator;
import br.com.exmart.util.Disposicao;
import br.com.exmart.util.TemplateChave;
import com.google.common.base.Strings;
import com.vaadin.data.HasValue;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.MessageBox;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.vaadin.spring.events.EventBus;
import org.w3c.tidy.Tidy;
import pl.pdfviewer.PdfViewer;

import java.io.*;
import java.lang.reflect.Method;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.List;

public class ModalRegistroImpl extends ModalRegistro {
    private Window window;
    private EventBus.ViewEventBus eventBus;
    private ModeloService modeloService;
    private Modelo modeloInicial= null;
    private ArquivoService arquivoService;
    private JtwigModel jtwigModel;
    private Protocolo protocolo;
    private ConfiguracaoService configuracaoService;
    private PDFService pdfService;
    private RegistroService registroService;


    private ProtocoloService protocoloService;
    private Configuracao configuracao;
    private List<CustasCartorio> custas;
    PDFService.DisposicaoPagina disposicaoPagina;
    Disposicao disposicaoCarimbo;
    private String txtCarimbo = null;
    File arquivoOriginal = null;
    TipoDocumento tipoDocumento = null;
    String statusNotificacao;
    Boolean icPositiva = false;
    boolean icCarimboInicial = false;


    public ModalRegistroImpl(Protocolo protocolo, List<Modelo> modelosCarimbo, List<CustasCartorio> custas, TipoDocumento tipoDocumento, File arquivoOriginal, List<Modelo> modelosTexto, String statusNotificacao, Boolean icPositiva) {
        this.statusNotificacao = statusNotificacao;
        this.icPositiva = icPositiva;
        this.tipoDocumento = tipoDocumento;
        this.protocolo = protocolo;
        this.eventBus = BeanLocator.find(EventBus.ViewEventBus.class);
        this.modeloService = BeanLocator.find(ModeloService.class);
        this.arquivoService = BeanLocator.find(ArquivoService.class);
        this.configuracaoService = BeanLocator.find(ConfiguracaoService.class);
        this.pdfService = BeanLocator.find(PDFService.class);
        this.protocoloService = BeanLocator.find(ProtocoloService.class);
        this.registroService =  BeanLocator.find(RegistroService.class);
        configuracao = configuracaoService.findConfiguracao();
        this.custas = custas;
        this.arquivoOriginal = arquivoOriginal;


        if(modelosCarimbo.size() <= 0 ){
            Notification.show("Erro","Não foi encontrado modelo para essa Natureza / Subnatureza", Notification.Type.ERROR_MESSAGE);
        }
        modeloInicial = modelosCarimbo.get(0);
        comboEtiquetas.setItems(modelosCarimbo);

        comboTexto.setItemCaptionGenerator(Modelo::getNome);
        comboTexto.setItems(modelosTexto);


        comboEtiquetas.setEmptySelectionAllowed(false);


        btnEditarCarimbo.addClickListener(evt -> abrirPanelEtiqueta());
        btnCancelarEtiqueta.addClickListener(evt->cancelarPanelEtiqueta());
        btnAtualizarEtiqueta.addClickListener(evt->atualizarDocumentoListener(evt));


        btnEditarTexto.addClickListener(evt -> abrirPanelTexto());
        btnCancelarTexto.addClickListener(evt -> cancelarPanelTexto());

        btnGerarTexto.addClickListener(evt -> gerarTextoListener()); //TODO Arrumar a ação do botão para que gere o pdf e atualize o documento


        this.disposicaoCarimbo = Disposicao.DIREITA_SUPERIOR;
        if(tipoDocumento.equals(TipoDocumento.REGISTRO)) {
            this.disposicaoPagina = PDFService.DisposicaoPagina.ULTIMA_PAGINA;
            btnEditarTexto.setVisible(false);
            verificarArquivo();
        }else{
            try {
                if (this.protocolo.getNatureza().getTipoEmissaoCertidao().equals(TipoEmissaoCertidao.TEXTO)) {
                    btnEditarTexto.setVisible(true);
                } else {
                    btnEditarTexto.setVisible(false);
                }
            }catch (NullPointerException e){
                btnEditarTexto.setVisible(false);
            }
            this.txtCarimbo = protocolo.getNatureza().getCarimbo().getCarimbo();
            Registro registro = null;
            if(tipoDocumento.equals(TipoDocumento.CERTIDAO_NOTIFICACAO)){
                registro = this.registroService.findByProtocolo(protocolo.getId());
            }else {
                registro = this.registroService.findByRegistroAndEspecialidade(protocolo.getRegistroReferencia(), protocolo.getTipo());
            }
            if(registro == null){
                //FIXME deve colocar algo
                this.txtCarimbo = configuracaoService.findConfiguracao().getTxtCarimboCertidaoSemRegistro();
                this.txtCarimbo = txtCarimbo.replace("[[NOME_CARTORIO]]", configuracao.getNomeCartorio());
            }else {
                txtCarimbo = txtCarimbo.replace("[[NUMERO_REGISTRO]]", registro.getNumeroRegistro().toString());
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                txtCarimbo = txtCarimbo.replace("[[DATA_REGISTRO]]", br.com.exmart.rtdpjlite.util.Utils.formatarData(registro.getDataRegistro().toLocalDate()));
            }
            this.disposicaoPagina = PDFService.DisposicaoPagina.PRIMEIRA_PAGINA;
            verificarArquivo();
        }

        btnConfirmarRegistro.addClickListener(evt->confirmarRegistroListener(evt));

        comboEtiquetas.setValue(modeloInicial);
        if(modelosTexto != null){
            if(modelosTexto.size() > 0){
                comboTexto.setValue(modelosTexto.get(0));
            }
        }
        if(!Strings.isNullOrEmpty(this.protocolo.getTextoCarimbo())) {
            comboEtiquetas.setValue(null);
            editorEtiquetas.setValue(this.protocolo.getTextoCarimbo());
        }
        if(!Strings.isNullOrEmpty(this.protocolo.getTextoCertidao())) {
            comboEtiquetas.setValue(null);
            editoTexto.setValue(this.protocolo.getTextoCertidao());
        }
        if(protocolo.getTipo().equals(TipoProtocolo.PRENOTACAO_TD) || protocolo.getTipo().equals(TipoProtocolo.PRENOTACAO_PJ)){
            btnConfirmarRegistro.setCaption("REGISTRAR");
        }
        if(this.tipoDocumento.equals(TipoDocumento.CERTIDAO_NOTIFICACAO)){
            editoTexto.setEnabled(true);
            btnConfirmarRegistro.setCaption("FINALIZAR CERTIDÃO");
        }

//        if(modeloInicial != null) {
//            aplicarModelo(modeloInicial, true);
//        }
        if(modelosTexto != null){
            if(modelosTexto.size() > 0){
                aplicarModelo(modelosTexto.get(0), false);
//                this.txtCarimbo = editorEtiquetas.getValue();
                atualizarCarimbo(editorEtiquetas.getValue());
            }
        }
        comboEtiquetas.setValue(modeloInicial);
        aplicarModelo(modeloInicial, true);
//        this.txtCarimbo = editorEtiquetas.getValue();
        if(this.icCarimboInicial){
            atualizarCarimbo(editorEtiquetas.getValue());
        }
        comboTexto.addValueChangeListener(valueChangeEvent -> aplicarModeloListener(valueChangeEvent));
        comboEtiquetas.addValueChangeListener(valueChangeEvent -> aplicarModeloListener(valueChangeEvent));
    }

    private void aplicarModeloListener(HasValue.ValueChangeEvent<Modelo> valueChangeEvent) {
        aplicarModelo(valueChangeEvent.getValue(), valueChangeEvent.getComponent() == comboEtiquetas);
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


    private void gerarTextoListener() {
        String texto  =editoTexto.getValue();
        if(!Strings.isNullOrEmpty(texto)){
            try {
                String textoHtml = "<html><body>"+ tidyHtml(texto)+"</body></html>";
                Diretorio subDiretorioGerarArquivo = null;
                if(tipoDocumento.equals(TipoDocumento.CERTIDAO_NOTIFICACAO)){
                    subDiretorioGerarArquivo = Diretorio.CERTIDAO_NOTIFICACAO;
                }
                this.arquivoOriginal = pdfService.htmlToPdf(textoHtml, arquivoService.recuperarPasta(this.protocolo.getNumero(), ((MyUI)UI.getCurrent()).getEspecialidadeAtual().getEspecialidade(),Diretorio.PROTOCOLO,subDiretorioGerarArquivo,protocolo.getTipo())+protocolo.getNumero()+".PDF");
                this.protocolo.setTextoCarimbo(texto);
                protocoloService.atualizarTextoCertidao(this.protocolo.getId(), texto);
                atualizarCarimbo(editorEtiquetas.getValue());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            Notification.show("Atenção","O preenchimento do texto é obrigatório", Notification.Type.ERROR_MESSAGE);
        }
    }

    private void verificarArquivo() {
        this.icCarimboInicial = false;
        File arquivo = null;
//        if (!tipoDocumento.equals(TipoDocumento.CERTIDAO_NOTIFICACAO)){
            arquivo = arquivoService.existeArquivoProtocoloCarimbado(this.protocolo.getNumero(), ((MyUI) UI.getCurrent()).getEspecialidadeAtual().getEspecialidade(), Diretorio.ASSINADO, protocolo.getTipo());
            if (arquivo == null)
                arquivo = arquivoService.existeArquivoProtocoloCarimbado(this.protocolo.getNumero(), ((MyUI) UI.getCurrent()).getEspecialidadeAtual().getEspecialidade(), Diretorio.CARIMBADO, protocolo.getTipo());
//        }
        if (arquivo != null) {
            mostrarArquivo(arquivo);
        } else {
            comboEtiquetas.setValue(modeloInicial);
            if((this.tipoDocumento.equals(TipoDocumento.CERTIDAO) | this.tipoDocumento.equals(TipoDocumento.CERTIDAO_NOTIFICACAO)) & protocolo.getNatureza().getTipoEmissaoCertidao() ==null){
                Notification.show("Atenção", "Forma de emissão não identificada", Notification.Type.ERROR_MESSAGE);
            }else{

                if(this.tipoDocumento.equals(TipoDocumento.REGISTRO)  || protocolo.getNatureza().getTipoEmissaoCertidao().equals(TipoEmissaoCertidao.IMAGEM) || protocolo.getNatureza().getTipoEmissaoCertidao().equals(TipoEmissaoCertidao.IMAGEM_INTEIRO_TEOR)){
                    btnEditarTexto.setVisible(false);
                    this.icCarimboInicial = true;
                }else{
                    btnEditarTexto.setVisible(true);
                    btnEditarTexto.click();
                }
            }
        }
    }


    private void atualizarDocumentoListener(Button.ClickEvent evt) {
        MessageBox
            .createQuestion()
            .withCaption("Alterar Carimbo")
            .withMessage("Deseja continuar?")
            .withYesButton(() -> {
                atualizarCarimbo(editorEtiquetas.getValue());
            }, ButtonOption.caption("SIM"), ButtonOption.focus())
            .withNoButton(ButtonOption.caption("NÃO"))
            .open();
    }

    private void confirmarRegistroListener(Button.ClickEvent evt) {
        StatusProtocoloJson json = new StatusProtocoloJson();
        json.setTexto(editorEtiquetas.getValue());
        if(this.tipoDocumento.equals(TipoDocumento.CERTIDAO_NOTIFICACAO)){
            this.eventBus.publish(this, new ProtocoloEvent.FinalizarCertidaoIntimacao(this.protocolo, json));
            if (this.window != null) {
                this.window.close();
            }
        }else {
            if (arquivoService.isDocumentoProtocoloExists(this.protocolo.getNumero(), ((MyUI) UI.getCurrent()).getEspecialidadeAtual().getEspecialidade(), protocolo.getTipo())) {
                if (this.protocolo.getTipo().equals(TipoProtocolo.CERTIDAO_PJ) || this.protocolo.getTipo().equals(TipoProtocolo.CERTIDAO_TD)) {
                    this.eventBus.publish(this, new ProtocoloEvent.FinalizarProtocoloCertidao(null));
                } else {
                    this.eventBus.publish(this, new ProtocoloEvent.RegistrarProtocolo(this.protocolo, json));
                    if (this.window != null) {
                        this.window.close();
                    }
                }
            } else {
                Notification.show("Atenção", "O arquivo do registro de nº: " + this.protocolo.getRegistroReferencia() + " não foi encontrado", Notification.Type.WARNING_MESSAGE);
            }
        }
    }

    private void abrirPanelEtiqueta() {
        if(!Strings.isNullOrEmpty(protocolo.getTextoCarimbo())){
            editorEtiquetas.setValue(protocolo.getTextoCarimbo());
        }
        panelEtiqueta.setVisible(true);
        panelImagem.setVisible(false);
        panelTexto.setVisible(false);

    }
    private void cancelarPanelEtiqueta(){
        panelEtiqueta.setVisible(false);
        panelTexto.setVisible(false);
        panelImagem.setVisible(true);
        btnEditarCarimbo.setEnabled(true);
    }

    private void abrirPanelTexto() {
        panelTexto.setVisible(true);
        panelImagem.setVisible(false);
        panelEtiqueta.setVisible(false);
    }
    private void cancelarPanelTexto() {
        panelTexto.setVisible(false);
        panelImagem.setVisible(true);
        panelEtiqueta.setVisible(false);
    }




    private JtwigModel criarModel(Object object){
        Class<? extends Object> classe = object.getClass();
        for (Method atributo : classe.getDeclaredMethods()) {
            if(atributo.isAnnotationPresent(TemplateChave.class)) {
                System.out.println(atributo.getAnnotation(TemplateChave.class).nomeChave());
                try {
                    atributo.setAccessible(true);
                    System.out.println(atributo.invoke(object));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    public void focus(){
        comboEtiquetas.setValue(modeloInicial);
    }
    private void aplicarModelo(Modelo carimbo, boolean icEtiqueta) {
        if(carimbo != null){
            Registro registroReferencia  = null;
            if(!Strings.isNullOrEmpty(protocolo.getRegistroReferencia())) {
                registroReferencia = registroService.findByRegistroAndEspecialidade(protocolo.getRegistroReferencia(), TipoProtocolo.recuperaEspecialidade(protocolo.getTipo()));
            }
            List<ObjetoProtocoloVO> objetos = protocolo.getObjetos();
            if(registroReferencia != null){
                objetos = registroReferencia.getObjetos();
            }
            if(registroReferencia == null){
                if(this.protocolo.getDtRegistro() != null){
                    registroReferencia = registroService.findByProtocolo(this.protocolo.getId());
                }
            }
            jtwigModel = BeanLocator.find(DocumentoUtil.class).iniciarVariaveisModelo(registroReferencia, this.protocolo, new ArrayList<>(), ((MyUI) UI.getCurrent()).getCustasCartorio(), this.statusNotificacao, this.protocolo.getPartesProtocolo(), objetos, protocolo.getIntimacaosProtocolo(), null);
            if(icEtiqueta) {
                if (carimbo != null) {
                    JtwigTemplate jtwigTemplate = JtwigTemplate.inlineTemplate(carimbo.getModelo());
                    String msg = jtwigTemplate.render(jtwigModel);
                    editorEtiquetas.setValue(msg);
//                    atualizarCarimbo(msg);
                }

                editorEtiquetas.focus();
            }else{
                JtwigTemplate jtwigTemplate = JtwigTemplate.inlineTemplate(carimbo.getModelo());
                String msg = jtwigTemplate.render(jtwigModel);
                editoTexto.setValue(msg);
//                atualizarCarimbo(msg);
                comboTexto.focus();
            }
        }
    }

    private void atualizarCarimbo(String texto){
        try {
            if(arquivoOriginal!= null){
                String hashArquivo = protocoloService.recuperarHashArquivo(this.protocolo.getId(), null);
                File arquivo = pdfService.converteAssinaPdf(arquivoOriginal,
                        arquivoService.recuperarPasta(this.protocolo.getNumero(), ((MyUI)UI.getCurrent()).getEspecialidadeAtual().getEspecialidade(), Diretorio.PROTOCOLO, Diretorio.CARIMBADO, protocolo.getTipo()),
                        texto,
                        this.txtCarimbo,
                        configuracaoService.findConfiguracao().getTxtExplicativoAssinatura().replace("<<URL_VALIDACAO>>", "%%%"+configuracaoService.findConfiguracao().getUrlQrcode()+"/documento/"+hashArquivo+"%%%"),
                        configuracaoService.findConfiguracao().getUrlQrcode()+"/documento/",
                        hashArquivo,
                        this.disposicaoPagina,
                        this.disposicaoCarimbo, null,
                        protocolo.getNatureza().isIcCertidaoDigital(), null);

                this.protocolo.setTextoCarimbo(texto);
                protocoloService.atualizarTextoCarimbo(this.protocolo.getId(), texto);
                if (arquivo != null) {
                    mostrarArquivo(arquivo);
                }
                btnEditarCarimbo.setEnabled(true);
            }else{
//                Notification.show("Carimbar Documento","Arquivo do Título não encontrado, favor cadastrar Retroativo para o mesmo OU gerar o documento", Notification.Type.ERROR_MESSAGE);
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Notification.show("Erro","Erro ao gerar o hash do arquivo favor entrar em contato com o Administrador", Notification.Type.ERROR_MESSAGE);
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
            Notification.show("Erro","Erro ao gerar o hash do arquivo favor entrar em contato com o Administrador", Notification.Type.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mostrarArquivo(File arquivo) {
        panelTexto.setVisible(false);
        panelEtiqueta.setVisible(false);
        panelImagem.setVisible(true);
        pdfViewerPanel.removeAllComponents();
        PdfViewer pdff = new PdfViewer();
        pdff.setPreviousPageCaption("ANTERIOR");
        pdff.setNextPageCaption("PRÓXIMO");
        pdff.setPageCaption("PÁGINA ");
        pdff.setToPageCaption("DE ");
        pdff.setDownloadBtnVisible(true);
        pdfViewerPanel.addComponentsAndExpand(pdff);
        pdff.setFile(arquivo);
        Notification.show("Atenção","Carregando documento...", Notification.Type.TRAY_NOTIFICATION);
    }

    public void habilitar(boolean habilitar) {
        btnEditarCarimbo.setEnabled(habilitar);
        btnEditarTexto.setEnabled(habilitar);
        btnConfirmarRegistro.setEnabled(habilitar);

        btnCancelarTexto.setEnabled(habilitar);
        btnAtualizarEtiqueta.setEnabled(habilitar);
        btnCancelarEtiqueta.setEnabled(habilitar);
        editoTexto.setEnabled(habilitar);
        btnGerarTexto.setEnabled(habilitar);
        comboTexto.setEnabled(habilitar);
        comboEtiquetas.setEnabled(habilitar);
    }

    public enum TipoDocumento {
        CERTIDAO,
        REGISTRO,
        CERTIDAO_NOTIFICACAO
    }


}
