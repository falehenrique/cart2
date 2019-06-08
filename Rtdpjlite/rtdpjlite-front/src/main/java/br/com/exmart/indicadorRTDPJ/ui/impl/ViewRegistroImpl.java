package br.com.exmart.indicadorRTDPJ.ui.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import com.google.common.base.Strings;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Window;

import br.com.exmart.indicadorRTDPJ.ui.ViewRegistro;
import br.com.exmart.indicadorRTDPJ.ui.navigator.NavigationManager;
import br.com.exmart.indicadorRTDPJ.ui.util.GerenciarJanela;
import br.com.exmart.indicadorRTDPJ.ui.util.Utils;
import br.com.exmart.rtdpjlite.model.CardTimeline;
import br.com.exmart.rtdpjlite.model.IndicadorPessoal;
import br.com.exmart.rtdpjlite.model.Objeto;
import br.com.exmart.rtdpjlite.model.Registro;
import br.com.exmart.rtdpjlite.model.TipoProtocolo;
import br.com.exmart.rtdpjlite.service.ArquivoService;
import br.com.exmart.rtdpjlite.service.ObjetoService;
import br.com.exmart.rtdpjlite.service.PKCS7Signer;
import br.com.exmart.rtdpjlite.service.ProtocoloService;
import br.com.exmart.rtdpjlite.service.SigningService;
import br.com.exmart.rtdpjlite.vo.ObjetoProtocoloVO;
import br.com.exmart.util.BeanLocator;
import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.MessageBox;
import pl.pdfviewer.PdfViewer;

public class ViewRegistroImpl extends ViewRegistro {
 
	private static final long serialVersionUID = 1L;
	private final ObjetoService objetoService;
    private Registro registro;
    private ProtocoloService protocoloService;
    private SigningService signingService;
    private ArquivoService arquivoService;
    private NavigationManager navigationManager;
    private Window window;
    private PKCS7Signer pkcs7Signer;



    public ViewRegistroImpl(Registro registro) {
        this.navigationManager = BeanLocator.find(NavigationManager.class);
        this.protocoloService = BeanLocator.find(ProtocoloService.class);
        this.signingService = BeanLocator.find(SigningService.class);
        this.arquivoService = BeanLocator.find(ArquivoService.class);
        this.objetoService = BeanLocator.find(ObjetoService.class);
        this.pkcs7Signer = BeanLocator.find(PKCS7Signer.class);
        this.registro = registro;
        if(registro.getEspecialidade().equalsIgnoreCase("TD")){
            labelTituloProtocolo.setValue("REGISTRO - RTD");
        }else{
            labelTituloProtocolo.setValue("REGISTRO - PJ");
        }
        labelNumeroProtocolo.setValue(registro.getNumeroRegistro().toString());
        labelDataProtocolo.setValue(Utils.formatarDataComHora(registro.getDataRegistro()));
        if(!Strings.isNullOrEmpty(registro.getSituacaoAtual())) {
            txtSituacaoAtual.setValue(registro.getSituacaoAtual());
        }
        if(!Strings.isNullOrEmpty(registro.getObservacao())) {
            txtObservacao.setValue(registro.getObservacao());
        }

        registroLabel.setValue(registro.getRegistro());
        naturezaLabel.setValue(registro.getNatureza().getNome());
        if(registro.getSubNatureza() != null)
        subnaturezaLabel.setValue(registro.getSubNatureza().getNome());

        if(registro.getProtocolo() != null) {
            Long numeroProtocolo = protocoloService.findNumeroById(registro.getProtocolo());
            if(numeroProtocolo != null)
                protocoloLabel.setValue(numeroProtocolo.toString());
        }

        if(registro.getNumeroRegistroReferencia()!=null)
        registroAnteriorLabel.setValue(registro.getNumeroRegistroReferencia().toString());

        //TODO deve arrumar quando for criado as tabelas
        tipoAtoLabel.setValue("-");
        subTipoAtoLabel.setValue("-");
        gridAssinaturas.getColumn("signatario").setCaption("Assinado por");
        gridAssinaturas.getColumn("dtEmissao").setCaption("Assinado em");
        try {
            gridAssinaturas.setItems(signingService.getAssinaturas(arquivoService.recuperarDocumentoProtocoloRegistroFile(this.registro.getRegistro(), registro.getNumeroRegistro(), registro.getEspecialidade(), null).getPath()));
        } catch (Exception e) {
            assinaturasList.setVisible(false);
        }

        for(IndicadorPessoal parte : registro.getIndicadorPessoal()){
            panelPartes.addComponent(new CardParteImpl(parte));
        }
        if(panelPartes.getComponentCount() <= 0 ){
            partesList.setVisible(false);
        }

        btnEmitirCertidao.addClickListener(evt->btnEmitirCertidaoListener());
        btnEditarRetroativo.addClickListener(evt->btnEditarRetroativoClickListener());
        btnEmitirCertidao.addClickListener(evt-> btnEmitirCertidaoClickListener());
        btnNovaAverbacao.addClickListener(evt->btnNovaAverbacaoClickListener());


        gerarTimeline();
        mostrarPDF();
        mostrarObjetos();
        
        
        tabSheetDetalhesRegistro.setSelectedTab(pdfViewerPanel);
        
        StreamResource myResource = createResource();
        FileDownloader fileDownloader = new FileDownloader(myResource);
        fileDownloader.extend(btnDownloadP7s);
    }

    @SuppressWarnings("serial")
	private StreamResource createResource() {
        return new StreamResource(new StreamResource.StreamSource(){

            @Override
            public InputStream getStream() {
                try {
                    File retorno = pkcs7Signer.signPkcs7(arquivoService.recuperarDocumentoProtocoloRegistroFile(registro.getRegistro(), registro.getNumeroRegistro(), registro.getEspecialidade(), null));

                    return new FileInputStream(retorno);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }, registro.getRegistro()+ ".p7s");
    }



    private void btnNovaAverbacaoClickListener() {
        MessageBox
                .createQuestion()
                .withCaption("Atenção")
                .withMessage("Deseja protocolocar uma averbação para esse registro?")
                .withYesButton(() -> {
                    this.window.close();
                    navigationManager.navegarPara(TipoProtocolo.recuperaTipoPrenotacaoByEspecialidade(registro.getEspecialidade()),null, Arrays.asList(new NavigationManager.Parametro("registroReferencia",registro.getRegistro())));


                }, ButtonOption.caption("SIM"))
                .withNoButton(ButtonOption.caption("NÃO"))
                .open();
    }

    private void btnEmitirCertidaoClickListener() {
        MessageBox
                .createQuestion()
                .withCaption("Atenção")
                .withMessage("Deseja pedir uma certidão para esse registro?.")
                .withYesButton(() -> {
//                    this.window.close();
                    navigationManager.navegarPara(TipoProtocolo.recuperaTipoCertidaoByEspecialidade(registro.getEspecialidade()),null, Arrays.asList(new NavigationManager.Parametro("registroReferencia",registro.getRegistro())));
                }, ButtonOption.caption("SIM"))
                .withNoButton(ButtonOption.caption("NÃO"))
                .open();
    }

    private void btnEditarRetroativoClickListener() {
        GerenciarJanela.abrirJanela("CADASTRO RETROATIVO", 90, 80, new ModalRetroativoImpl(this.registro, null), null, true, true, true, true);
    }

    @SuppressWarnings("unused")
	private void btnVerProtocoloListener() {
        MessageBox
            .createQuestion()
            .withCaption("Atenção")
            .withMessage("Deseja abrir outro protocolo? Caso esteja editando outro protocolo no momento as alterações não salvas serão perdidas.")
            .withYesButton(() -> {
//FIXME                navigationManager.navegarPara(registro.getTipo(), protocolo.getNumero());
//                this.window.close();
            }, ButtonOption.caption("SIM"))
            .withNoButton(ButtonOption.caption("NÃO"))
            .open();
    }

    private void mostrarObjetos() {

        List<Objeto> objetos = objetoService.findAll();

        if(this.registro.getObjetos() != null){
            for(ObjetoProtocoloVO objeto : this.registro.getObjetos()){
                Objeto selecionado = null;
                for(Objeto obj : objetos){
                    if(obj.getNome().equalsIgnoreCase( objeto.getNome())){
                        selecionado = obj;
                    }
                }
                CardObjetoImpl card = new CardObjetoImpl(registro.getIndicadorPessoal(), selecionado, objeto,null);
                card.habilitaDesabilita(false, false);
                panelObjetos.addComponent(card);
            }
            if(objetos.size() <=0){
                objetosListPanel.setVisible(false);
            }
        }else{
            objetosListPanel.setVisible(false);
        }

    }

    private void btnEmitirCertidaoListener() {
        //TODO deve chamar passando o numero do registro para o pedido de certidao
//        navigationManager.navigateTo(ViewProtocoloCertidaoPj.class, this.protocolo.getNumeroRegistro());
    }

//    private void mostrarDetalhes() {
//        panelDetalheRegistro.setVisible(!panelDetalheRegistro.isVisible());
//    }

    private void gerarTimeline() {

        for(CardTimeline card : this.protocoloService.getTimeLine(this.registro.getRegistro(), this.registro.getEspecialidade())){
            if(card != null) {
                panelTimeline.addComponent(new CardLinhaTempoImpl(card, this.registro.getNumeroRegistro()));
            }
        }
//        if(panelTimeline.getComponentCount() <=0){
//            timelinePanel.setVisible(false);
//        }else{
//            timelinePanel.setVisible(true);
//        }
    }

    private void mostrarPDF() {
        pdfViewerPanel.removeAllComponents();
        PdfViewer pdff = new PdfViewer();
        pdff.setPreviousPageCaption("ANTERIOR");
        pdff.setNextPageCaption("PRÓXIMO");
        pdff.setPageCaption("PÁGINA ");
        pdff.setToPageCaption("DE ");
        pdff.setDownloadBtnVisible(true);

        try {
            pdff.setFile(arquivoService.recuperarDocumentoProtocoloRegistroFile(this.registro.getRegistro(), this.registro.getNumeroRegistro(), this.registro.getEspecialidade(), null));
            pdfViewerPanel.addComponentsAndExpand(pdff);
            Notification.show("Atenção","Carregando documento...", Notification.Type.TRAY_NOTIFICATION);
        } catch (Exception e) {
            Notification.show("Arquivo", "Arquivo do protocolo não encontrado", Notification.Type.ERROR_MESSAGE);
        }



    }

}
