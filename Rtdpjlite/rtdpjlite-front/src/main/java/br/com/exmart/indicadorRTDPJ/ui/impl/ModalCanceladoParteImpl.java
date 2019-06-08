package br.com.exmart.indicadorRTDPJ.ui.impl;

import br.com.exmart.indicadorRTDPJ.MyUI;
import br.com.exmart.indicadorRTDPJ.ui.ModalCanceladoParte;
import br.com.exmart.indicadorRTDPJ.ui.util.FormatarDocumento;
import br.com.exmart.rtdpjlite.model.Protocolo;
import br.com.exmart.rtdpjlite.model.Status;
import br.com.exmart.rtdpjlite.model.StatusProtocolo;
import br.com.exmart.rtdpjlite.service.ProtocoloService;
import br.com.exmart.rtdpjlite.vo.StatusProtocoloJson;
import br.com.exmart.util.BeanLocator;
import com.google.common.base.Strings;
import com.google.common.io.ByteStreams;
import com.vaadin.data.BeanValidationBinder;
import com.vaadin.event.FieldEvents;
import com.vaadin.ui.*;
import com.wcs.wcslib.vaadin.widget.multifileupload.ui.MultiFileUpload;
import com.wcs.wcslib.vaadin.widget.multifileupload.ui.UploadFinishedHandler;
import com.wcs.wcslib.vaadin.widget.multifileupload.ui.UploadStateWindow;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Arrays;

public class ModalCanceladoParteImpl extends ModalCanceladoParte {
    private Window window;
    private BeanValidationBinder<StatusProtocoloJson> binder = new BeanValidationBinder<>(StatusProtocoloJson.class);
    private StatusProtocoloJson statusProtocoloGson = new StatusProtocoloJson();
    private ProtocoloService protocoloService;
    private Protocolo protocolo;
    private Status status;
    private byte[] arquivo;

    public ModalCanceladoParteImpl(Protocolo protocolo, Status status) {
        this.protocolo = protocolo;
        this.status = status;
        this.protocoloService = BeanLocator.find(ProtocoloService.class);
        btnCancelar.addClickListener(evt->{window.close();});
        btnConfirmar.addClickListener(evt -> confirmarClickListener(evt));

        bindCampos();


        UploadFinishedHandler handler = new UploadFinishedHandler() {
            @Override
            public void handleFile(InputStream inputStream, String s, String s1, long l, int i) {
                try {
                    arquivo = ByteStreams.toByteArray(inputStream);
                    binder.getBean().setPossuiAnexo(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        UploadStateWindow uploadWindow = new UploadStateWindow();

        MultiFileUpload singleUpload = new MultiFileUpload(handler, uploadWindow, false);

        singleUpload.setAcceptedMimeTypes(Arrays.asList("application/pdf"));
        singleUpload.getSmartUpload().setUploadButtonCaptions("Anexar requerimento","");
        singleUpload.setMimeTypeErrorMsgPattern("Tipo de Arquivo não suportado, utilize apenas .pdf");
        panel.addComponent(singleUpload);
        panel.setExpandRatio(singleUpload, 1);
        panel.setComponentAlignment(singleUpload, Alignment.TOP_LEFT);
        documento.addBlurListener(evt->blurListener(evt));

        nome.focus();
    }

    private void blurListener(FieldEvents.BlurEvent evt) {
        if(!Strings.isNullOrEmpty(documento.getValue())){
            documento.setValue(FormatarDocumento.formataDocumento(documento.getValue()));
        }
    }

    private void confirmarClickListener(Button.ClickEvent evt) {
        if(binder.isValid()) {
            this.protocolo.getStatusProtocolo().add(new StatusProtocolo(this.status, LocalDateTime.now(), arquivo,binder.getBean(), ((MyUI) UI.getCurrent()).getUsuarioLogado()));

            //TODO deve salvar os dados refernte a esse status
            this.window.close();
        }else{
            Notification.show("Ops ;) Acho que faltou preencher algum campo corretamente", Notification.Type.ERROR_MESSAGE);
        }
    }
    private void bindCampos() {
        binder.bind(nome, "nomeParte");
        binder.bind(documento, "documentoParte");
//        binder.bind(requerimento, "requerimento");
        binder.setBean(this.statusProtocoloGson);
    }
}
