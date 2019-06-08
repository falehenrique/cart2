package br.com.exmart.indicadorRTDPJ.ui.impl;

import br.com.exmart.indicadorRTDPJ.ui.ModalDocumento;
import pl.pdfviewer.PdfViewer;
import java.io.File;

public class ModalDocumentoImpl extends ModalDocumento {



    private void mostrarArquivoPdf(File arquivo) {
        panelPdf.removeAllComponents();
        PdfViewer pdf = new PdfViewer();
        pdf.setPreviousPageCaption("ANTERIOR");
        pdf.setNextPageCaption("PRÓXIMO");
        pdf.setPageCaption("PÁGINA ");
        pdf.setDownloadBtnVisible(true);
        pdf.setToPageCaption("DE ");
        pdf.setStyleName("pdfRTDPJ");
        pdf.setFile(arquivo);
        panelPdf.addComponentsAndExpand(pdf);
    }
}