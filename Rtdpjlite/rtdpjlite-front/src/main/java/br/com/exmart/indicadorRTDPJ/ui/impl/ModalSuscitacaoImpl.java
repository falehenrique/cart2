package br.com.exmart.indicadorRTDPJ.ui.impl;

import br.com.exmart.indicadorRTDPJ.MyUI;
import br.com.exmart.indicadorRTDPJ.ui.ModalSuscitacao;
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

public class ModalSuscitacaoImpl extends ModalSuscitacao {
    private Window window;
    private ProtocoloService protocoloService;
    private Protocolo protocolo;
    private Status status;
    private BeanValidationBinder<StatusProtocoloJson> binder = new BeanValidationBinder<>(StatusProtocoloJson.class);
    private StatusProtocoloJson statusProtocoloGson = new StatusProtocoloJson();
    private byte[] arquivo;

    public ModalSuscitacaoImpl(Protocolo protocolo, Status status) {
        this.protocolo = protocolo;
        this.status = status;
        this.protocoloService = BeanLocator.find(ProtocoloService.class);
//		new CustomStringBlockFormatter(documento, new int[]{3, 3, 3, 2}, new String[]{".", ".", "-"}, CustomStringBlockFormatter.ForceCase.NONE);
//		documento.addBlurListener(evt -> validarDocumento(evt));
        btnCancelar.addClickListener(evt -> {window.close();});
        btnConfirmar.addClickListener(evt -> confirmarClickListener(evt));
        nrProcesso.focus();

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
        singleUpload.getSmartUpload().setUploadButtonCaptions("Anexar Processo","");
        singleUpload.setMimeTypeErrorMsgPattern("Tipo de Arquivo não suportado, utilize apenas .pdf");
        panel.addComponent(singleUpload,0);
        panel.setExpandRatio(singleUpload, Float.parseFloat("0.5"));
        panel.setComponentAlignment(singleUpload, Alignment.TOP_LEFT);
    }

    private void bindCampos() {
        binder.bind(nrProcesso, "nrProcessoDuvida");
        binder.bind(observacao, "observacao");
        binder.setBean(this.statusProtocoloGson);
    }

    private void confirmarClickListener(Button.ClickEvent evt) {
        if(binder.isValid()) {
            this.protocolo.getStatusProtocolo().add(new StatusProtocolo(this.status, LocalDateTime.now(),  arquivo,binder.getBean(), ((MyUI) UI.getCurrent()).getUsuarioLogado()));

            //TODO deve salvar os dados refernte a esse status
            this.window.close();
        }else{
            Notification.show("Ops ;) Acho que faltou preencher algum campo corretamente", Notification.Type.ERROR_MESSAGE);
        }
    }
}
