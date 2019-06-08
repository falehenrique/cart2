package br.com.exmart.indicadorRTDPJ.ui.impl;

import br.com.exmart.indicadorRTDPJ.MyUI;
import br.com.exmart.indicadorRTDPJ.ui.ModalCanceladoJud;
import br.com.exmart.rtdpjlite.model.Protocolo;
import br.com.exmart.rtdpjlite.model.Status;
import br.com.exmart.rtdpjlite.model.StatusProtocolo;
import br.com.exmart.rtdpjlite.service.ProtocoloService;
import br.com.exmart.rtdpjlite.vo.StatusProtocoloJson;
import br.com.exmart.util.BeanLocator;
import com.google.common.io.ByteStreams;
import com.vaadin.data.BeanValidationBinder;
import com.vaadin.ui.*;
import com.wcs.wcslib.vaadin.widget.multifileupload.ui.MultiFileUpload;
import com.wcs.wcslib.vaadin.widget.multifileupload.ui.UploadFinishedHandler;
import com.wcs.wcslib.vaadin.widget.multifileupload.ui.UploadStateWindow;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Arrays;

public class ModalCanceladoJudImpl extends ModalCanceladoJud{
    private Window window;
    private BeanValidationBinder<StatusProtocoloJson> binder = new BeanValidationBinder<>(StatusProtocoloJson.class);
    private StatusProtocoloJson statusProtocoloGson = new StatusProtocoloJson();
    private ProtocoloService protocoloService;
    private Protocolo protocolo;
    private Status status;
    private byte[] arquivo;

    public ModalCanceladoJudImpl(Protocolo protocolo, Status status) {
        this.protocolo = protocolo;
        this.status = status;
        this.protocoloService = BeanLocator.find(ProtocoloService.class);
        btnCancelar.addClickListener(evt->{window.close();});
        btnConfirmar.addClickListener(evt -> confirmarClickListener(evt));
        bindCampos();
        dtOrdem.setValue(LocalDateTime.now());

        UploadFinishedHandler handler = new UploadFinishedHandler() {
            @Override
            public void handleFile(InputStream inputStream, String s, String s1, long l, int i) {
                try {
                    arquivo = ByteStreams.toByteArray(inputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        UploadStateWindow uploadWindow = new UploadStateWindow();

        MultiFileUpload singleUpload = new MultiFileUpload(handler, uploadWindow, false);

        singleUpload.setAcceptedMimeTypes(Arrays.asList("application/pdf"));
        singleUpload.getSmartUpload().setUploadButtonCaptions("Anexar processo","");
        singleUpload.setMimeTypeErrorMsgPattern("Tipo de Arquivo não suportado, utilize apenas .PDF");
        panel.addComponent(singleUpload);
        panel.setExpandRatio(singleUpload, 1);
        panel.setComponentAlignment(singleUpload, Alignment.MIDDLE_RIGHT);

        numeroOrdem.focus();
    }

    private void bindCampos() {
        binder.bind(numeroOrdem, "nrOrdem");
        binder.bind(dtOrdem, "dataCancelamentoJudicial");

        binder.setBean(this.statusProtocoloGson);
    }

    private void confirmarClickListener(Button.ClickEvent evt) {
        if(binder.isValid()) {
            statusProtocoloGson.setPossuiAnexo(this.arquivo != null);
            this.protocolo.getStatusProtocolo().add(new StatusProtocolo(this.status, LocalDateTime.now(), this.arquivo,binder.getBean(), ((MyUI) UI.getCurrent()).getUsuarioLogado()));

            //TODO deve salvar os dados refernte a esse status
            this.window.close();
        }else{
            Notification.show("Ops ;) Acho que faltou preencher algum campo corretamente", Notification.Type.ERROR_MESSAGE);
        }
    }
}
