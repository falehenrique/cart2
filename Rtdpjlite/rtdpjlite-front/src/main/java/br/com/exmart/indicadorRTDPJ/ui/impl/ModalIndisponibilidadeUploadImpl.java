package br.com.exmart.indicadorRTDPJ.ui.impl;

import br.com.exmart.indicadorRTDPJ.MyUI;
import br.com.exmart.indicadorRTDPJ.ui.ModalIndisponibilidadeUpload;
import br.com.exmart.rtdpjlite.service.IndisponibilidadeService;
import br.com.exmart.util.BeanLocator;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.wcs.wcslib.vaadin.widget.multifileupload.ui.MultiFileUpload;
import com.wcs.wcslib.vaadin.widget.multifileupload.ui.UploadFinishedHandler;
import com.wcs.wcslib.vaadin.widget.multifileupload.ui.UploadStateWindow;
import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.MessageBox;
import net.sf.jasperreports.data.excel.ExcelFormatEnum;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Calendar;

public class ModalIndisponibilidadeUploadImpl extends ModalIndisponibilidadeUpload {
    private MultiFileUpload singleUpload;
    private File arquivoImportar = null;
    private Window window;
    private File arquivo;
    private IndisponibilidadeService indisponibilidadeService;

    public ModalIndisponibilidadeUploadImpl() {
        this.indisponibilidadeService = BeanLocator.find(IndisponibilidadeService.class);
        panelLoading.setVisible(false);
        btnProcessarArquivo.setEnabled(false);
        UploadFinishedHandler handler = new UploadFinishedHandler() {
            @Override
            public void handleFile(InputStream inputStream, String s, String s1, long l, int i) {
                try {
                    arquivo = File.createTempFile(String.valueOf(Calendar.getInstance().getTimeInMillis()), "xml");
                    try (FileOutputStream out = new FileOutputStream(arquivo)) {
                        IOUtils.copy(inputStream, out);
                    }

                    btnProcessarArquivo.setEnabled(true);
                }catch (Exception e ){

                }
            }
        };

        UploadStateWindow uploadWindow = new UploadStateWindow();

        singleUpload = new MultiFileUpload(handler, uploadWindow, false);

//        singleUpload.setAcceptedMimeTypes(Arrays.asList("application/xml"));
        singleUpload.getSmartUpload().setUploadButtonCaptions("Enviar Arquivo", "");
        singleUpload.setMimeTypeErrorMsgPattern("Os documentos devem ser enviados somente com a extensão .xml");
        singleUpload.setWidth("100%");
        singleUpload.setEnabled(true);
        panelUpload.addComponent(singleUpload);
//        setExpandRatio(singleUpload, 1);
        panelUpload.setComponentAlignment(singleUpload, Alignment.TOP_CENTER);

        btnCancelar.addClickListener(evt-> {this.window.close();});
        btnProcessarArquivo.addClickListener(clickEvent -> processarArquivo());
    }

    private void processarArquivo() {
        try {

            panelLoading.setVisible(true);
            indisponibilidadeService.enviarArquivoIndisponibilidade(this.arquivo, () -> {
                getUI().access(new Runnable() {
                    @Override
                    public void run() {
                        arquivo.deleteOnExit();
                        Notification.show("Sucesso","Indisponibilidade processada com sucesso" , Notification.Type.WARNING_MESSAGE);
                        panelLoading.setVisible(false);
                        btnProcessarArquivo.setEnabled(false);
                    }
                });

            });

        } catch (Exception e) {
            Notification.show("Erro", e.getMessage(), Notification.Type.ERROR_MESSAGE);
        }
    }
}
