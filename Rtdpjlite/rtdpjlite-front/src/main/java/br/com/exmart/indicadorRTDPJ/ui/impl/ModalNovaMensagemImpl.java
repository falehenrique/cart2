package br.com.exmart.indicadorRTDPJ.ui.impl;

import br.com.exmart.indicadorRTDPJ.ui.ModalNovaMensagem;
import br.com.exmart.rtdpjlite.service.ArquivoService;
import br.com.exmart.rtdpjlite.service.rest.balcaoonline.PedidoService;
import br.com.exmart.rtdpjlite.vo.balcaoonline.PedidoMensagemTipo;
import br.com.exmart.util.BeanLocator;
import com.google.common.io.ByteStreams;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Window;
import com.wcs.wcslib.vaadin.widget.multifileupload.ui.MultiFileUpload;
import com.wcs.wcslib.vaadin.widget.multifileupload.ui.UploadFinishedHandler;
import com.wcs.wcslib.vaadin.widget.multifileupload.ui.UploadStateWindow;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Calendar;

public class ModalNovaMensagemImpl extends ModalNovaMensagem {
    private Window window;
    private Long idPedido;

    private MultiFileUpload singleUpload;
    private File arquivoUpload = null;
    public ModalNovaMensagemImpl(Long idPedido) {
        this.idPedido = idPedido;
        tipoMensagem.setItemCaptionGenerator(PedidoMensagemTipo::getNome);
        tipoMensagem.setItems(PedidoMensagemTipo.listar());
        tipoMensagem.setEmptySelectionAllowed(false);
        btnCancelar.addClickListener(evt->{this.window.close();});
        btnEnviar.addClickListener(evt->btnEnviarListener(evt));

        UploadFinishedHandler handler = new UploadFinishedHandler() {
            @Override
            public void handleFile(InputStream inputStream, String s, String s1, long l, int i) {
                try {
                    arquivoUpload =  BeanLocator.find(ArquivoService.class).criarArquivoTemporario(Long.toString(Calendar.getInstance().getTimeInMillis()), ByteStreams.toByteArray(inputStream));
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        UploadStateWindow uploadWindow = new UploadStateWindow();

        singleUpload = new MultiFileUpload(handler, uploadWindow, false);

        singleUpload.setAcceptedMimeTypes(Arrays.asList("application/pdf"));
        singleUpload.getSmartUpload().setUploadButtonCaptions("Anexar Documento", "");
        singleUpload.setMimeTypeErrorMsgPattern("Os documentos devem ser enviados somente com a extensão .pdf");
        panelMensagem.addComponent(singleUpload);
//        panelMensagem.setExpandRatio(singleUpload, 1);
        panelMensagem.setComponentAlignment(singleUpload, Alignment.TOP_LEFT);

    }

    private void btnEnviarListener(Button.ClickEvent evt) {

        try {
            //FIXME corrigir
//            BeanLocator.find(PedidoService.class).enviarMensagem(this.idPedido,tipoMensagem.getValue(), mensagem.getValue(), this.arquivoUpload);
            this.window.close();
        } catch (Exception e) {
            Notification.show("Erro","Erro ao enviar a mensagem", Notification.Type.ERROR_MESSAGE);
        }
    }
}
