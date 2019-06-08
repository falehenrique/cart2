package br.com.exmart.indicadorRTDPJ.ui.impl;

import br.com.exmart.indicadorRTDPJ.MyUI;
import br.com.exmart.indicadorRTDPJ.ui.ModalProtocolarLote;
import br.com.exmart.rtdpjlite.model.CartorioParceiro;
import br.com.exmart.rtdpjlite.model.Cliente;
import br.com.exmart.rtdpjlite.model.Status;
import br.com.exmart.rtdpjlite.model.TipoProtocolo;
import br.com.exmart.rtdpjlite.service.*;
import br.com.exmart.rtdpjlite.vo.ArquivoCsvImportacao;
import br.com.exmart.rtdpjlite.vo.RetornoValidacaoLote;
import br.com.exmart.rtdpjlite.vo.financeiro.ServicoCalculado;
import br.com.exmart.util.BeanLocator;
import com.google.common.io.ByteStreams;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.wcs.wcslib.vaadin.widget.multifileupload.ui.MultiFileUpload;
import com.wcs.wcslib.vaadin.widget.multifileupload.ui.UploadFinishedHandler;
import com.wcs.wcslib.vaadin.widget.multifileupload.ui.UploadStateWindow;
import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.MessageBox;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class ModalProtocolarLoteImpl extends ModalProtocolarLote {
    private TipoProtocolo tipoProtocolo;
    private ConfiguracaoService configuracaoService;
    private ArquivoService arquivoService;
    private ClienteService clienteService;
    private ProtocoloService protocoloService;
    private MultiFileUpload singleUpload;
    private File arquivoImportar = null;
    RetornoValidacaoLote retornoValidacaoLote;
    LoteService loteService;
    List<ArquivoCsvImportacao> retornoProcessraArquivos;
    String diretorio;

    public ModalProtocolarLoteImpl(TipoProtocolo tipoProtocolo) {
        this.clienteService = BeanLocator.find(ClienteService.class);
        this.configuracaoService = BeanLocator.find(ConfiguracaoService.class);
        this.loteService = BeanLocator.find(LoteService.class);
        this.arquivoService = BeanLocator.find(ArquivoService.class);
        this.protocoloService = BeanLocator.find(ProtocoloService.class);
        this.tipoProtocolo = tipoProtocolo;
        txtTipoImportacao.addStyleName(TipoProtocolo.recuperaEspecialidade(tipoProtocolo).toLowerCase());
        txtTipoImportacao.setValue("Protocolar em lote para " + TipoProtocolo.recuperaEspecialidade(tipoProtocolo));
        comboCliente.setItemCaptionGenerator(Cliente::getNome);

        UploadFinishedHandler handler = new UploadFinishedHandler() {
            @Override
            public void handleFile(InputStream inputStream, String s, String s1, long l, int i) {

                try {
                    arquivoImportar = arquivoService.salvarArquivoLote(inputStream, comboCliente.getValue().getId()+"_"+s, comboCliente.getValue().getId());
                    diretorio = arquivoImportar.getParent();
//                    inputStream.close();
//                    System.out.println(arquivoImportar.getAbsoluteFile().toString());
                    try {
                        retornoValidacaoLote = protocoloService.importarProtocolos(arquivoImportar, comboCliente.getValue());
                        retornoProcessraArquivos = loteService.importarProtocolo(retornoValidacaoLote.getCliente(), retornoValidacaoLote.getDiretorio(), retornoValidacaoLote.getStatusRegistrado(), retornoValidacaoLote.getBeans(), retornoValidacaoLote.getServicosProtocolo(),((MyUI) UI.getCurrent()).getUsuarioLogado(), ((MyUI) UI.getCurrent()).getCustasCartorio());
                        arquivoImportar.delete();
                        MessageBox
                                .create()
                                .withCaption("Processamento de LoteItem")
                                .withMessage("Lote protocolado e registrado com sucesso, agora iremos processar os arquivos.")
                                    .withYesButton(() -> {
                                        btnProcessarArquivoListener();
                                    },ButtonOption.caption("Ok"), ButtonOption.focus())
                                    .open();
                    }catch (Exception e){
                        e.printStackTrace();
                        MessageBox
                            .create()
                            .withCaption("Problemas ao validar o lote")
                            .withMessage(e.getMessage().replace(",","\n").replace("[","").replace("]",""))
                            .withCloseButton(ButtonOption.caption("Fechar"))
                            .open();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        UploadStateWindow uploadWindow = new UploadStateWindow();

        singleUpload = new MultiFileUpload(handler, uploadWindow, false);

        singleUpload.setAcceptedMimeTypes(Arrays.asList("application/zip","application/octet-stream", "application/x-rar-compressed"));
        singleUpload.getSmartUpload().setUploadButtonCaptions("Enviar Arquivo", "");
        singleUpload.setMimeTypeErrorMsgPattern("Os documentos devem ser enviados somente com a extensão .zip");
        singleUpload.setWidth("100%");
        singleUpload.setEnabled(true);
        panelUpload.addComponent(singleUpload);
//        setExpandRatio(singleUpload, 1);
        panelUpload.setComponentAlignment(singleUpload, Alignment.TOP_CENTER);

        comboCliente.setItems((s, s1) -> {return s.toUpperCase().startsWith(s1.toUpperCase());},clienteService.findAll());

        btnProcessarArquivo.addClickListener(evt->btnProcessarArquivoListener());

    }

    private void btnProcessarArquivoListener() {
        loteService.processarArquivosLote();//this.retornoProcessraArquivos, diretorio,((MyUI) UI.getCurrent()).getUsuarioLogado());
    }

    private void retornoProcessarArquivo() {
        System.out.println("acabou");
//        UI.getCurrent().access(() -> {
//            ((MyUI)UI.getCurrent()).addNotification();
//        });
    }
}
