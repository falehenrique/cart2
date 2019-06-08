package br.com.exmart.rtdpjlite.service;

import br.com.exmart.util.Disposicao;
import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceCmyk;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * Created by Heryk on 10/11/17.
 */
public class CarimboEventHandler implements IEventHandler {
    protected String texto;
    protected ImageData imgRubrica;
    protected Resource FONT;
    protected Disposicao DISP;
    protected String hash;

    public CarimboEventHandler(String texto, ImageData imgRubrica, Resource FONT, Disposicao disp, String hash) {
        this.texto = texto;
        this.imgRubrica = imgRubrica;
        this.FONT = FONT;
        this.DISP = disp;
        this.hash = hash;
    }
    @Override
    public void handleEvent(Event event) {
        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfDocument pdfDoc = docEvent.getDocument();
        PdfPage lPage = docEvent.getPage();


        PdfPage moldPage = pdfDoc.getLastPage();
        PageSize pSize = new PageSize(moldPage.getPageSize());
        PdfCanvas cPage = new PdfCanvas(lPage);

        Float xDisp = new Float(0.000);
        Float yDisp = new Float(0.000);



        PageSize ps = pdfDoc.getDefaultPageSize();


        //Sombra
        cPage.setFillColor(Color.LIGHT_GRAY);
        cPage.rectangle(
                (pSize.getWidth()-(pSize.getWidth()*0.0200)),  //0.653
                (pSize.getHeight()*0),
                (pSize.getWidth()*0.0200),
                (pSize.getHeight()));
        cPage.fill();



        //TEXTO DO CARIMBO
        PdfFont font = null;
        try {
            font = PdfFontFactory.createFont(FONT.getFile().getPath(), PdfEncodings.WINANSI, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        cPage.setFillColor(Color.BLACK);


        PDFService pdfService = new PDFService();

        Paragraph p = null;
        p = new Paragraph( String.format("%-10s", ((docEvent.getDocument().getPageNumber(lPage))+".  ") ) + String.format("%-270s",texto) + (((hash==null) || (hash.length()==0))?"": "Cód: ") + hash )
                .setFont(font)
                //.setMargins(10, 5,10,10)
                .setFontSize(6)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setTextAlignment(TextAlignment.LEFT)
                .setFixedLeading(7)
                .setRotationAngle(1.5707963268);

        //Centralizando proporcionalmente o carimbo
        //Conta: tamanho que se quer do carimbo (30% = *0.30)
        //       centralizar (100% - 30% = 70% / 2 = 35% = *0.35)
        Rectangle rect = new Rectangle(
                (pSize.getWidth()-(pSize.getWidth()*0.0225f)),  // X - Alinhamento do paragrafo (esquerda / direita)
                (pSize.getHeight()*0f),
                (pSize.getWidth()*0.0185f), //Alinhamento do paragrafo (-abaixo / +acima)
                (pSize.getHeight()*1f));


        cPage.fillStroke();


        new Canvas(cPage, pdfDoc, rect)
                .add(p).setWordSpacing(100);


        //-----------------------------------------------
        //Rubrica
        //-----------------------------------------------
        //Referencias X e Y para posicionamento da imagem
        Double cX = (pSize.getWidth()-(pSize.getWidth()*0.0180)); //655
        Double cY = (pSize.getHeight()-(pSize.getHeight()*0.0200));
        imgRubrica.setRotation(90f);

        cPage.addImage(imgRubrica,
                cX.floatValue(),
                cY.floatValue(),
                10f,
                true);
        cPage.fill();

    }

}