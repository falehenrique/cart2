package br.com.exmart.indicadorRTDPJ.ui.view;

import com.vaadin.ui.HorizontalLayout;
import pl.pdfviewer.PdfViewer;

import java.io.File;

public class ModalVisualizarDocumento extends HorizontalLayout {
    public ModalVisualizarDocumento(File arquivo) {
        setSpacing(true);
        setMargin(true);
        PdfViewer pdf = new PdfViewer();
        pdf.setPreviousPageCaption("ANTERIOR");
        pdf.setNextPageCaption("PRÓXIMO");
        pdf.setPageCaption("PÁGINA ");
        pdf.setDownloadBtnVisible(true);
        pdf.setToPageCaption("DE ");
        pdf.setStyleName("pdfRTDPJ");
        pdf.setFile(arquivo);
        addComponentsAndExpand(pdf);
    }
}
