package br.com.exmart.rtdpjlite.service;

import br.com.exmart.rtdpjlite.util.RetornoSelo;
import br.com.exmart.util.Disposicao;
import br.com.exmart.util.ExecShell;
import br.com.exmart.util.PdfHash;
import com.google.common.base.Strings;
import com.google.common.io.Files;
import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.filespec.PdfFileSpec;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import com.itextpdf.kernel.utils.PdfMerger;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.pdfa.PdfADocument;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.MalformedURLException;
import java.security.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Heryk on 09/11/17.
 */
@Service
public class PDFService {

    @Value("${URL_PDF_FONT}")
    private Resource FONT;

    @Value("${URL_PDF_COLOR}")
    private Resource INTENT;

    @Value("${URL_PDF_ICP}")
    private Resource ICP;

    @Value("${URL_PDF_LOGO_CARIMBO}")
    private Resource LOGO_CARIMBO;

    @Value("${URL_PDF_LOGO_CARIMBO_OVAL}")
    private Resource LOGO_CARIMBO_OVAL;

    @Value("${comando_ghostscript}")
    private String comando;

    @Value("${comando_gs_thumb}")
    private String comandoThumb;

    @Value("${comando_converter_a4}")
    private String converterA4;


    @Value("${comando_gs_merge}")
    private String comandoMerge;

    private  boolean icGhostiscript;
    @Value("${versao_ghostscript}")
    private Float versaoGhostscript;

    public boolean isIcGhostiscript() {
        return icGhostiscript;
    }
    public String getMensagemErro(){
        return "Ghostscript não instaldo ou versão inferior a "+versaoGhostscript;
    }
    protected static Logger logger = LoggerFactory.getLogger(PDFService.class);

    @PostConstruct
    private void verificarGhostscript(){
        icGhostiscript = false;
        try {
            String target = new String("gs -version");
            // String target = new String("mkdir stackOver");
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(target);
            proc.waitFor();
            StringBuffer output = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = "";
            while ((line = reader.readLine())!= null) {
                output.append(line + "\n");
            }
            int index = output.indexOf("GPL Ghostscript");
            if(index >=0){
                String versao = output.substring(index+16,20);
                if(Strings.isNullOrEmpty(versao)){
                    throw new Exception(getMensagemErro());
                }else{
                    Float versaoF = new Float(versao);
                    if(versaoF<this.versaoGhostscript){
                        throw new Exception(getMensagemErro());
                    }else{
                        this.icGhostiscript = true;
                    }
                }
            }else{
                throw new Exception(getMensagemErro());
            }
//            System.out.println("### " + output);
        } catch (Throwable t) {
            t.printStackTrace();
//            throw new Exception("Ghostscript não instaldo ou versão inferior a 9.22");
        }
    }


    /***
     * Cria arquivo pdf de teste
     * @param dest Path com nome do arquivo pdf a ser gerado
     * @return True sempre
     */
    public Boolean criarPdf(String dest) {
        try {
            PdfWriter writer = new PdfWriter(dest);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            PdfFont font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
            document.add(new Paragraph("Pdf Criado"));
            document.close();
            pdf.close();
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /***
     * Retorna a quantidade de páginas
     * @param arquivo Arquivo pdf em byte array
     * @return Quantidade de páginas
     */

    public Integer getPaginas(File arquivo) {
        Integer retorno = 0;

        PdfReader pdfReader = null;
        try {
            pdfReader = new PdfReader(arquivo);
            PdfDocument pdfDoc = new PdfDocument(pdfReader);
            retorno = pdfDoc.getNumberOfPages();
            pdfDoc.close();
            pdfReader.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }

        return retorno;
    }


    /***
     * Carimba arquivo pdf em padrão pré-estabelecido para uso de cartório extrajudicial de TDPJ
     * @param arquivo Byte array do pdf a ser carimbado
     * @param txtIntegra Texto principal da página gerada com carimbos
     * @param txtCarimboCertidao Texto do carimbo pequeno(Todas as Páginas)
     * @param txtExplica Texto de explicação do QRCode
     * @param urlQRCode Url para conferencia por QRCode
     * @param hash Hash gerado para validação do arquivo pdf carimbado
     * @param dispCarimbo Disposição do carimbo txtCarimboCertidao (carimbo pequeno)
     * @param dispPagina Disposição da página criada (Primeira página ou Última página)
     *
     * @param arquivoSaida
     * @return PdfHash (Hash gerado e Byte array do pdf carimbado)
     */
    public PdfHash carimbar(File arquivo,
                            String txtIntegra,
                            String txtCarimboCertidao,
                            String txtExplica,
                            String urlQRCode,
                            String hash,
                            Disposicao dispCarimbo,
                            DisposicaoPagina dispPagina,
                            File arqP7SOriginal,
                            String arquivoSaida,
                            boolean icDigital,
                            RetornoSelo retornoSelo) {

        PdfHash pdfHash = new PdfHash();
        try {


//            ByteArrayInputStream bCarimboOrigem = new ByteArrayInputStream(arquivo);

            PdfDocument pdfDoc = new PdfDocument(new PdfReader(arquivo), new PdfWriter(arquivoSaida));


            if (!Strings.isNullOrEmpty(txtCarimboCertidao)) {
                ImageData imgLogoCarimboOval = ImageDataFactory.create(LOGO_CARIMBO_OVAL.getFile().getPath());

                CarimboEventHandler handler = new CarimboEventHandler(txtCarimboCertidao, imgLogoCarimboOval, FONT, dispCarimbo, hash);
                pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, handler);
            }

            //Gera uma nova página no inicio ou final do arquivo com o mesmo tamanho da última página do arquivo
            //PdfPage moldPage = pdfDoc.getLastPage();
            //PageSize pSize = new PageSize(moldPage.getPageSize());
            PageSize pSize = PageSize.A4;
            PdfPage lPage = null;
            if (dispPagina == DisposicaoPagina.PRIMEIRA_PAGINA) {
                pdfDoc.addNewPage(1, pSize);
                lPage = pdfDoc.getFirstPage();
            } else if (dispPagina == DisposicaoPagina.ULTIMA_PAGINA) {
                pdfDoc.addNewPage(pSize);
                lPage = pdfDoc.getLastPage();
            }


            PdfCanvas cPage = new PdfCanvas(lPage);
            PdfFont font = PdfFontFactory.createFont(FONT.getFile().getPath(), PdfEncodings.WINANSI, true);


            cPage.setFillColor(Color.BLACK);
            PageSize ps = pdfDoc.getDefaultPageSize();
            //TEXTO DO CARIMBO

            Paragraph p = new Paragraph(txtIntegra)
                    .setFont(font)
                    .setMargins(10, 10,10,10)
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.JUSTIFIED)
                    .setFixedLeading(15);

            //Centralizando proporcionalmente o carimbo
            //Conta: tamanho que se quer do carimbo (30% = *0.30)
            //       centralizar (100% - 30% = 70% / 2 = 35% = *0.35)
            Double fX = (pSize.getWidth()*0.05);
            Double fY = (pSize.getHeight()*0.38);
            Double fW = (pSize.getWidth()*0.90);

            Double fH = 0.0;


            Double linha = 0.0;
            linha = ( Math.ceil( ( txtIntegra.length()-1072 )/120.0) * 0.03 ) + 0.21;

            if (txtIntegra.length() > 1072) {
                fH = (pSize.getHeight()*linha);  //0.20
            } else {
                fH = (pSize.getHeight()*0.21);  //0.20
            }


            Rectangle rect = new Rectangle(
                    fX.floatValue(),
                    fY.floatValue(),
                    fW.floatValue(),
                    fH.floatValue());


            ImageData imgLogoCarimbo = ImageDataFactory.create(LOGO_CARIMBO.getFile().getPath());

            new Canvas(cPage, pdfDoc, rect)
                    .add(p).setWordSpacing(100);
            cPage.rectangle(rect).addImage(imgLogoCarimbo,
                    (float) ((pSize.getWidth()-imgLogoCarimbo.getWidth())/2),
                    (float) (pSize.getHeight()*0.72),
                    true);
            cPage.stroke();


            if (icDigital) {
                //Imagem ICP-BRASIL
                ImageData imgICP = ImageDataFactory.create(ICP.getFile().getPath());


                cPage.addImage(imgICP,
                        (float) (pSize.getWidth() * 0.05),
                        (float) ((pSize.getHeight() * 0.95) - ((pSize.getHeight() * 0.05))),
                        (float) (pSize.getWidth() * 0.05),
                        false);
            }


            //Hash para geração do QRCode
            /*String hash = gerarHash();

            Security.addProvider(new BouncyCastleProvider());
            MessageDigest digest = MessageDigest.getInstance("SHA256", "BC" );
            byte[] sha1Imagem = digest.digest();
            String hash = sha1Imagem.toString();
            */

            //-----------------------------------------------
            //Carimbo de referencia com texto informativo
            //Complemento para o carimbo QRCode
            //-----------------------------------------------

            if (icDigital) {

                cPage.setFillColor(Color.BLACK);

                Paragraph p2 = new Paragraph();

                if (txtExplica.indexOf("%%%") > 0) {

                    txtExplica.replace("<HASH>", hash);
                    String[] partes = txtExplica.split("%%%");


                    p2.add(partes[0])
                            .add(new Link(partes[1], PdfAction.createURI(partes[1])))
                            .add(partes[2])

                            .setFont(font)
                            .setMargins(10, (float) (pSize.getWidth() * 0.13), 10, 10)
                            .setFontSize(9)
                            .setTextAlignment(TextAlignment.JUSTIFIED)
                            .setFixedLeading(10)

                            .setFixedPosition(
                                    (float) (pSize.getWidth() * 0.065),
                                    (float) (pSize.getHeight() - (pSize.getHeight() * 0.72)),
                                    (float) (pSize.getWidth() * 0.75))
                            .setPageNumber(pdfDoc.getPageNumber(lPage));
                } else {

                    p2.add(txtExplica.replace("<HASH>", hash))
                            .setFont(font)
                            .setMargins(10, (float) (pSize.getWidth() * 0.13), 10, 10)
                            .setFontSize(9)
                            .setTextAlignment(TextAlignment.JUSTIFIED)
                            .setFixedLeading(10);

                }

                Rectangle rectCarimbo = new Rectangle(
                        (float) (pSize.getWidth() * 0.05),
                        (float) (pSize.getHeight() - (pSize.getHeight() * 0.75)),
                        (float) (pSize.getWidth() * 0.90),
                        (float) (pSize.getHeight() * 0.10));


                new Canvas(cPage, pdfDoc, rectCarimbo)
                        .add(p2);
                cPage.rectangle(rectCarimbo).setColor(Color.WHITE, true);
                cPage.stroke();

                cPage.setFillColor(Color.BLACK);


                //-----------------------------------------------
                //QRCode
                //-----------------------------------------------
                cPage.setFillColor(Color.GRAY);

                //Objeto QRCode
                BarcodeQRCode barcodeQRCode = new BarcodeQRCode(urlQRCode + hash);
                Image code128Image = new Image(barcodeQRCode.createFormXObject(pdfDoc)).setHeight(400);
                PdfFormXObject xObject = barcodeQRCode.createFormXObject(pdfDoc);

                //Referencias X e Y para posicionamento do QR Code no pdf
                Double cX = (pSize.getWidth() * 0.90) - xObject.getWidth() - 10;
                Double cY = pSize.getHeight() - (pSize.getHeight() * 0.70) - xObject.getHeight() + 2;

                cPage.addXObject(
                        xObject,
                        cX.floatValue(),
                        cY.floatValue(),
                        2f);
                cPage.fill();
            }



            if (retornoSelo != null) {
                //-----------------------------------------------
                //Carimbo QRCode Selo TJSP
                //-----------------------------------------------
                cPage.setFillColor(Color.BLACK);

                Paragraph p3 = new Paragraph();
                Paragraph p4 = new Paragraph();

                String txtSelo = retornoSelo.getMensagemCarimboSelo();
                String txtNumeroSelo = retornoSelo.getSeloDigital();

                p3.add(txtSelo)
                        .setFont(font)
                        .setMargins(10, (float) (pSize.getWidth() * 0.23), 10, 10)
                        .setFontSize(9)
                        .setTextAlignment(TextAlignment.JUSTIFIED)
                        .setFixedLeading(10);


                p4.add("Selo Digital " + txtNumeroSelo)
                        .setFont(font)
                        .setMargins(10, (float) (pSize.getWidth() * 0.28), 10, 10)
                        .setFontSize(11)
                        .setBold()
                        .setTextAlignment(TextAlignment.JUSTIFIED)
                        .setFixedLeading(10);



                Rectangle rectCarimbo2 = new Rectangle(
                        (float) (pSize.getWidth() * 0.05),
                        (float) (pSize.getHeight() - (pSize.getHeight() * 0.9325)),
                        (float) (pSize.getWidth() * 0.90),
                        (float) (pSize.getHeight() * 0.15));

                new Canvas(cPage, pdfDoc, rectCarimbo2)
                        .add(p3)
                        .add(p4);
                cPage.rectangle(rectCarimbo2).setColor(Color.WHITE, true);
                cPage.stroke();

                cPage.setFillColor(Color.BLACK);


                //-----------------------------------------------
                //QRCode Selo SP
                //-----------------------------------------------
                cPage.setFillColor(Color.GRAY);

                //Objeto QRCode
                BarcodeQRCode barcodeQRCode = new BarcodeQRCode(retornoSelo.getQrCode());
                Image code128Image = new Image(barcodeQRCode.createFormXObject(pdfDoc)).setHeight(400);
                PdfFormXObject xObject = barcodeQRCode.createFormXObject(pdfDoc);

                //Referencias X e Y para posicionamento do QR Code no pdf
                Double cX = (pSize.getWidth() * 0.89) - xObject.getWidth() - 10;
                Double cY = pSize.getHeight() - (pSize.getHeight() * 0.835) - xObject.getHeight() + 2;

                cPage.addXObject(
                        xObject,
                        cX.floatValue(),
                        cY.floatValue(),
                        1.5f);
                cPage.fill();


            }




            //if (arqP7SOriginal == null) {
            //    anexarPdfOriginal(pdfDoc, arquivo);
            //}
            //else {
            //    anexarPdfOriginal(pdfDoc, arqP7SOriginal);
            //}

            pdfDoc.close();

            pdfHash.setOrig(new File(arquivoSaida));
            pdfHash.setHash(hash);


            return pdfHash;
        } catch (IOException e) {
            e.printStackTrace();
            return pdfHash;
        }
    }

    /***
     * Anexa um PDF
     * @param pdfDoc Instancia do arquivo que receberá o anexo
     * @param orig Arquivo pdf original que será atachado
     * @return Instancia com pdf original anexado
     */
    public PdfDocument anexarPdfOriginal(PdfDocument pdfDoc, File orig) throws IOException {

        String arqOriginal = "DocumentoOriginal" + ( Files.getFileExtension(orig.getPath()).equals("p7s")?".p7s":".PDF");

        PdfFileSpec spec = PdfFileSpec.createEmbeddedFileSpec(pdfDoc, orig.getPath(), null, arqOriginal, null, null, true);
        pdfDoc.addFileAttachment("Documento original", spec);

        return pdfDoc;
    }


    /***
     * Excluir uma página de um arquivo PDF
     * @param orig Arquivo que será excluído uma página
     * @param pagina Número da página a ser excluída
     * @return Array de byte do pdf com a página excluída
     */
    public byte[] excluirPagina(byte[] orig, int pagina) {
        ByteArrayInputStream bOrigem = new ByteArrayInputStream(orig);
        ByteArrayOutputStream bDestino = new ByteArrayOutputStream();

        try {
            PdfDocument pdfDoc = new PdfDocument(new PdfReader(bOrigem), new PdfWriter(bDestino));
            pdfDoc.removePage(pagina);
            pdfDoc.close();
            bOrigem.close();

            return bDestino.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }


    /***
     * Gera um hash randomico com as 8 primeiro posições baseado em SHA256
     * @return Hash com 8 posições
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     */
    public String gerarHash() throws NoSuchProviderException, NoSuchAlgorithmException, IOException {
        //Hash para geração do QRCode baseada em uma cadeia randomica

        String sRand = "";
        for (int i=1; i <=20; i++) {
            sRand = sRand + Long.toHexString(Double.doubleToLongBits(Math.random()));
        }

        String hexHash = "";
        for (int i=1; i <=8; i++) {
            hexHash = hexHash + sRand.charAt( (int)Math.floor( Math.random() * Math.floor( (double)sRand.length()-1 ) ) );
        }

        return hexHash.substring(0,8);
    }


    /***
     * Gera um hash de arquivo com as 8 primeiro posições baseado em SHA256
     * @return Hash com 8 posições
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     */
    public String gerarHash(File file) throws NoSuchProviderException, NoSuchAlgorithmException, IOException {
        //Hash para geração do QRCode baseada no conteúdo do arquivo
        Security.addProvider(new BouncyCastleProvider());
        MessageDigest messageDigest = MessageDigest.getInstance("SHA256", "BC" );

        int bufferSize = 256 * 1024; //256K
        FileInputStream fis = null;
        DigestInputStream dis = null;
        fis = new FileInputStream(file);
        dis = new DigestInputStream(fis,messageDigest);
        byte[] buffer =new byte[bufferSize];
        while (dis.read(buffer) > 0);
        messageDigest= dis.getMessageDigest();
        byte[] sha256 = messageDigest.digest();
        dis.close();
        fis.close();

        String hash = sha256.toString();
        String hexHash = Hex.toHexString(hash.getBytes());
        return hexHash; //hexHash.substring(0,8);
    }


    public PdfCanvas escrever(PdfPage pPdf, String texto, Rectangle rect) {
        Paragraph p = new Paragraph(texto);

        //Centralizando proporcionalmente o carimbo
        //Conta: tamanho que se quer do carimbo (30% = *0.30)
        //       centralizar (100% - 30% = 70% / 2 = 35% = *0.35)

        PdfCanvas cPage = new PdfCanvas(pPdf);
        cPage.setFillColor(Color.BLACK);
        cPage.setFillColor(Color.BLACK);

        PdfCanvas retorno = new PdfCanvas(pPdf);

        return retorno;
    }





    public static enum DisposicaoPagina {
        PRIMEIRA_PAGINA(1),
        ULTIMA_PAGINA(2);

        private final int valor;
        DisposicaoPagina(int valorOpcao){
            valor = valorOpcao;
        }
        public int getValor(){
            return valor;
        }



    }



    public File htmlToPdf(String html, String arqDestinoPath) throws IOException {

        ByteArrayOutputStream bDestino = new ByteArrayOutputStream();
        PdfWriter pdfDoc = new PdfWriter(bDestino);

        File arqDestino = new File(arqDestinoPath);

        try {

            PdfRendererBuilder builder = new PdfRendererBuilder();

            //builder.withUri(url);
            builder.withHtmlContent(html, "");
            builder.toStream(bDestino);
            builder.run();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bDestino.close();
                pdfDoc.close();

            } catch (IOException e) {
                // swallow
            }
        }

        FileOutputStream aDest = new FileOutputStream(arqDestino);
        aDest.write(bDestino.toByteArray());
        aDest.close();

        return arqDestino;
    }


    public Boolean anexarFolha(String arquivo, String dest, String carimbo) throws IOException {

        Image img = new Image(ImageDataFactory.create(carimbo)).setFixedPosition(12, 300);
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(arquivo), new PdfWriter(dest));
        pdfDoc.addNewPage();
        pdfDoc.close();
        return true;
    }







    public Boolean atacharPdf(String origem, String pdfAdd) throws FileNotFoundException {
        PdfWriter pdfWriter = new PdfWriter(origem);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document;
        document = new Document(pdfDocument);

        PdfWriter pdfWriterP = new PdfWriter(pdfAdd);
        PdfDocument pdfDocumentP = new PdfDocument(pdfWriterP);

        Document docPdf;
        docPdf = new Document(pdfDocumentP);




        //document.add(docPdf);
        document.close();
        docPdf.close();

        try {
            pdfDocumentP.close();
            pdfWriterP.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }



    public File pdfMergeAllgs(List<String> arquivos, String arqDestinoPath) throws Exception {

        if (!this.icGhostiscript) {
            throw new Exception(getMensagemErro());
        }
        StringBuffer output = new StringBuffer();
        Process process = null;
        //Usa o Ghostscript para realizar o merge de arquivos


        String listaPdf = "";
        for (String arquivo: arquivos) {
            listaPdf = listaPdf + " " + arquivo;
        }

        String novoComando = comandoMerge.format(comandoMerge, arqDestinoPath, listaPdf);
        //    		System.out.println(comando);
        //System.exit(0);
        ExecShell execShell = new ExecShell();
        execShell.exec(novoComando);
//        try {
//            process = Runtime.getRuntime().exec(novoComando);
//            process.waitFor();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//
//            //Captura o retorno do shell
//            String retorno = "";
//            while ((retorno = reader.readLine()) != null) {
//                output.append(retorno + "\n");
//            }
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
        return new File(arqDestinoPath);
    }

    public File pdfMergeAll(ArrayList<String> arquivos, String arqDestinoPath) throws IOException {


        File arqDestino = new File(arqDestinoPath);
        PdfDocument pdf = new PdfDocument(new PdfWriter(arqDestinoPath));
        pdf.close();

        for (String arquivo: arquivos) {
            pdfMerge(arquivo, arqDestinoPath, arqDestinoPath);
        }

        return arqDestino;
    }



    public File pdfMerge(String leitura, String leitura2, String destino) throws IOException {

        File arqDestino = new File(destino);
        PdfDocument pdf = new PdfDocument(new PdfReader(destino));

        PdfMerger merger = new PdfMerger(pdf);

        PdfDocument pdfOrig = new PdfDocument(new PdfReader(leitura));
        merger.merge(pdfOrig, 1, pdfOrig.getNumberOfPages());
        pdfOrig.close();

        PdfDocument pdfOrig2 = new PdfDocument(new PdfReader(leitura2));
        merger.merge(pdfOrig2, 1, pdfOrig2.getNumberOfPages());
        pdfOrig2.close();

        merger.close();
        pdf.close();

        return arqDestino;
    }


    public void pdfMerge(String orig) throws IOException {
        PdfDocument pdf = null;
        PdfMerger merger = new PdfMerger(pdf);

        PdfDocument pdfOrig = new PdfDocument(new PdfReader(orig));

        merger.merge(pdfOrig, 1, 1);
        pdfOrig.close();
        pdf.close();

    }

    //Converte um PDF em PDF/A
    public File converte(String pdfOriginal, String diretorio, String pdfConvertido) throws Exception {
        logger.debug("Original {}", pdfOriginal);
        logger.debug("Diretorio {}", diretorio);
        logger.debug("Convertido {}", pdfConvertido);
        if(!this.icGhostiscript){
            throw  new Exception(getMensagemErro());
        }
        StringBuffer output = new StringBuffer();
        Process process = null;
        //Usa o Ghostscript para converter o arquivo
        //Exportar o comando para o application.properties
        //    		String comando = "/usr/local/bin/gs -dPDFA -dNOOUTERSAVE -sProcessColorModel=DeviceRGB -sDEVICE=pdfwrite -o %s -dPDFACompatibilityPolicy=1 %s";

        String novoComando = comando.format(comando, pdfOriginal, diretorio, pdfConvertido);
        //    		System.out.println(comando);
        //System.exit(0);
        logger.debug("Executando {}",novoComando);
        ExecShell execShell = new ExecShell();
        execShell.exec(novoComando);
//        try {
//            process = Runtime.getRuntime().exec(novoComando);
//            logger.debug("Antes de wait for");
//            int exitCode = process.waitFor();
//            logger.debug("Exit code {}", exitCode);
//   /*
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//
//            //Captura o retorno do shell
//            String retorno = "";
//            while ((retorno = reader.readLine())!= null) {
//                logger.debug(retorno);
////                output.append(retorno + "\n");
//            }
//            reader.close();
//            OutputStream outputStream = process.getOutputStream();
//            PrintStream printStream = new PrintStream(outputStream);
//            printStream.println();
//            printStream.flush();
//            printStream.close();
//*/
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
        File retorno = new File(diretorio+File.separator+pdfConvertido);
        logger.debug("arquivo para retorno: {}", retorno.getAbsoluteFile());
        logger.debug("existe arquivo {}", retorno.exists());
        return retorno;
    }

    /*
    	public static void main(String[] args) {
			PDFService service = new PDFService();
			service.converte("/Users/rseixas/Projects/Exmart/rtdpjlite/rtdpjlite-data/src/test/resources/pdfs/322873.pdf", "/Users/rseixas/Projects/Exmart/rtdpjlite/rtdpjlite-data/src/test/resources/pdfs/convertido.pdf");
		}
    */
    /*
    abrir.pdf
    salvar
    add(.pdf / .tiff)
    assinar
    assinar (stamp)
    assinatura.getAssinantes
    assinatura.isvalid
    atachar.documento
    atachar.xml
    */

    public File converteAssinaPdfCertidao(File arquivoOriginal, String motivo, String txtCarimboCertidao, String txtExplicacaoAssinatura, String urlQrcode, Long idServicoCalculado, String hashArquivo, DisposicaoPagina disposicaoPagina, Disposicao disposicaoCarimbo, boolean icDigital, RetornoSelo retornoSelo){
        File retorno = converteAssinaPdf(arquivoOriginal,motivo,txtCarimboCertidao,txtExplicacaoAssinatura,urlQrcode, hashArquivo, disposicaoPagina, disposicaoCarimbo, icDigital, retornoSelo);
        String arquivoTo = retorno.getParent()+File.separator;
        try {
            FileUtils.copyFile(retorno, new File(arquivoTo + idServicoCalculado.toString() + ".PDF"));
//            Files.copy(retorno.toPath(), new File(arquivoTo + idServicoCalculado.toString() + ".PDF").toPath(), StandardCopyOption.REPLACE_EXISTING);
            retorno.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new File(arquivoTo + idServicoCalculado.toString() + ".PDF");
    }
    public File converteAssinaPdf(File arquivoOriginal, String motivo, String txtCarimboCertidao, String txtExplicacaoAssinatura, String urlQrcode, String hashArquivo, DisposicaoPagina disposicaoPagina, Disposicao disposicaoCarimbo, boolean icDigital, RetornoSelo retornoSelo){
        String diretorioConvertido=arquivoOriginal.getParent()+File.separator+"PDFA";
        File diretorio = new File(diretorioConvertido);
        diretorio.mkdirs();
//        File diretorioConvertido = new File(arquivoOriginal.getParent()+File.separator+"pdfA");
//        diretorioConvertido.mkdirs();
        try {
            File convertido = this.converte(arquivoOriginal.getPath(), diretorioConvertido ,arquivoOriginal.getName());
            String arquivoConvertido = convertido.getAbsolutePath();

            PdfHash arqRetorno = carimbar(convertido, motivo,
                    txtCarimboCertidao,
                    txtExplicacaoAssinatura,
                    urlQrcode,
                    hashArquivo,
                    disposicaoCarimbo,
                    disposicaoPagina,
                    null, diretorioConvertido+File.separator + "_"+arquivoOriginal.getName(), icDigital, retornoSelo);


            convertido.delete();

            File retorno = new File(diretorioConvertido+File.separator + "_"+arquivoOriginal.getName());
            retorno.renameTo(new File(arquivoConvertido));
            return  new File(arquivoConvertido);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }


    @Async
    public void gerarThumbnailPdf(String diretorio, String arquivoOriginal) throws Exception {
        if(!this.icGhostiscript){
            throw  new Exception(getMensagemErro());
        }
        StringBuffer output = new StringBuffer();
        Process process = null;
        //Usa o Ghostscript para converter o arquivo
        //Exportar o comando para o application.properties
        //    		String comando = "/usr/local/bin/gs -dPDFA -dNOOUTERSAVE -sProcessColorModel=DeviceRGB -sDEVICE=pdfwrite -o %s -dPDFACompatibilityPolicy=1 %s";

        String novoComando = comandoThumb.format(comando, diretorio+"thumb_"+arquivoOriginal, diretorio+arquivoOriginal);
        //System.out.println("comando:  " + novoComando);
        //    		System.out.println(comando);
        //System.exit(0);
//        try {
//            process = Runtime.getRuntime().exec(novoComando);
//            process.waitFor();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//
//            //Captura o retorno do shell
//            String retorno = "";
//            while ((retorno = reader.readLine())!= null) {
//                output.append(retorno + "\n");
//            }
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
        ExecShell execShell = new ExecShell();
        execShell.exec(novoComando);
    }




























    public byte[] criarPdfA(byte[] orig, PdfAConformanceLevel pdfAConformanceLevel) {


        try {

            //Cria arquivo PDF/A (destino)
            ByteArrayOutputStream dest = new ByteArrayOutputStream();
            PdfADocument pdf = new PdfADocument(new PdfWriter(dest),
                    pdfAConformanceLevel,
                    new PdfOutputIntent(
                            "Custom",
                            "", "http://www.color.org",
                            "sRGB IEC61966-2.1",
                            new FileInputStream(INTENT.getFile().getPath())));

            //Seta tamanho da página
            pdf.setDefaultPageSize(PageSize.A4);
            Document document = new Document(pdf);






            //Seta parametros requeridos
            pdf.setTagged();

//            PdfFont font = PdfFontFactory.createFont(FONT, PdfEncodings.WINANSI, true);
//            Paragraph p = new Paragraph();
//            p.setFont(font);
//            p.add(new Text("The quick brown "));
//            document.add(p);

           // System.out.println(iOrig.read());


            /*
            pdf.addNewPage();
            PdfPage page2 = pdf.getLastPage();
            PdfCanvas canvas2 = new PdfCanvas(page2);
            ImageData img7 = ImageDataFactory.create("/Users/Heryk/Desktop/PDF_A_JAVA/img_pdf7.tiff");
            canvas2.addImage(img7, 0f, 0f, true);


 */
            //Instancia pdf de origem
            ByteArrayInputStream iOrig = new ByteArrayInputStream(orig);
            PdfDocument oDocument = new PdfDocument(new PdfReader(iOrig));

            PdfPage pOrig = oDocument.getPage(1);
            PdfFormXObject pageCopy = pOrig.copyAsFormXObject(pdf);

            Image imageHWS = new Image(pageCopy);

            PdfCanvas pdfCanvas = new PdfCanvas(pageCopy, pdf);



            PdfStream pStream = new PdfStream(orig);
            PdfImageXObject pimg = new PdfImageXObject(pStream);

            byte[] imageData = pimg.getImageBytes();


            //System.out.println(imageData);
            FileOutputStream opa = new FileOutputStream("/Users/Heryk/Desktop/imagemH.tiff");
            opa.write(imageData);
            opa.close();


            //pimg.copyTo(document);


            //document.add(imageHWS);


  //          pdf.addPage(pOrig);

//            PdfFormXObject pageCopy = pOrig.copyAsFormXObject(pdf);
//            pOrig.copyTo(pdf);


        //    PdfPage page29 = pdf.getLastPage();
        //    PdfCanvas canvas29 = new PdfCanvas(pageCopy, oDocument);
        //    canvas29.addXObject(pageCopy, 0, 0, 500, 500, 500,500);
            //canvas29.addImage(, 0f, 0f, true);




            pdf.addNewPage();
            PdfPage page3 = pdf.getLastPage();
            PdfCanvas canvas3 = new PdfCanvas(page3);
            ImageData img8 = ImageDataFactory.create("/Users/Heryk/Desktop/PDF_A_JAVA/img_pdf8.tiff");
            canvas3.addImage(img8, 0f, 0f, true);

            oDocument.close();
            pdf.close();
            document.close();
            //System.out.println(dest.toByteArray().length);
            return dest.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return orig;
        }
    }

    public Boolean criarPdfA(String orig, String dest, PdfAConformanceLevel pdfAConformanceLevel) {

        try {

            PdfADocument pdf = new PdfADocument(new PdfWriter(dest),
                    pdfAConformanceLevel,
                    new PdfOutputIntent(
                            "Custom",
                            "", "http://www.color.org",
                            "sRGB IEC61966-2.1",
                            new FileInputStream(INTENT.getFile().getPath())));


            PdfWriter pdfWriter = new PdfWriter(dest);


            pdf.setDefaultPageSize(PageSize.A4);
            Document document = new Document(pdf);

            //Setting some required parameters
            pdf.setTagged();
/*

            PdfFont font = PdfFontFactory.createFont(FONT, PdfEncodings.WINANSI, true);
            Paragraph p = new Paragraph();
            p.setFont(font);
            p.add(new Text("The quick brown "));
            document.add(p);

            pdf.addNewPage();

            PdfPage page2 = pdf.getLastPage();

            PdfCanvas canvas2 = new PdfCanvas(page2);
            ImageData img7 = ImageDataFactory.create("/Users/Heryk/Desktop/img_1234.jpg");
            canvas2.addImage(img7, 0f, 0f, true);
            */

   //         TIFFLZWDecoder tiffDec = new TIFFLZWDecoder();

            PdfFormXObject page;
            PdfCanvas canvas = new PdfCanvas(pdf.getFirstPage().newContentStreamBefore(),
                    pdf.getFirstPage().getResources(), pdf);






            PdfDocument srcDoc;
            srcDoc = new PdfDocument(new PdfReader(orig));
            page = srcDoc.getFirstPage().copyAsFormXObject(pdf);

            canvas.addXObject(page, 0, 0);
            srcDoc.close();

            pdf.close();

            //document.close();
            return true;

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
            return false;
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
            return false;
        } catch (IOException e) {
            return false;
        }


    }





    public void manipulatePdfH() throws IOException {
        String DEST = "/Users/Heryk/Desktop/extract_streams%s";
        String SRC = "/Users/Heryk/Desktop/323404.pdf";


        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC));
        PdfObject obj;
        List<Integer> streamLengths = new ArrayList<>();
        for (int i = 1; i <= pdfDoc.getNumberOfPdfObjects(); i++) {
            obj = pdfDoc.getPdfObject(i);
            if (obj != null && obj.isStream()) {
                byte[] b;
                try {
                    b = ((PdfStream) obj).getBytes();
                } catch (PdfException exc) {
                    b = ((PdfStream)obj).getBytes(false);
                }
                //System.out.println(b.length);
                FileOutputStream fos = new FileOutputStream(String.format(DEST, i));
                fos.write(b);

                streamLengths.add(b.length);
                fos.close();
            }
        }
        pdfDoc.close();
    }



    protected Boolean manipulatePdfA(String orig, String dest, PdfAConformanceLevel pdfAConformanceLevel) throws Exception {


        PdfADocument pdf = new PdfADocument(new PdfWriter(dest),
                PdfAConformanceLevel.PDF_A_1B,
                new PdfOutputIntent(
                        "Custom",
                        "", "http://www.color.org",
                        "sRGB IEC61966-2.1", new FileInputStream(INTENT.getFile().getPath())));
        //Setting some required parameters
        pdf.setTagged();
        pdf.getCatalog().setLang(new PdfString("en-US"));
        pdf.getCatalog().setViewerPreferences(
                new PdfViewerPreferences().setDisplayDocTitle(true));
        PdfDocumentInfo info = pdf.getDocumentInfo();
        info.setTitle("iText7 PDF/A-1a example");
        //Create PdfMerger instance
        PdfMerger merger = new PdfMerger(pdf);
        //Add pages from the first document
        PdfDocument firstSourcePdf = new PdfDocument(new PdfReader(orig));

        PdfPage firstP = firstSourcePdf.getPage(1);

//        firstSourcePdf.getPage(1).p

                merger.merge(firstSourcePdf, 1, firstSourcePdf.getNumberOfPages());
        //Add pages from the second pdf document
        PdfDocument secondSourcePdf = new PdfDocument(new PdfReader(orig));
        merger.merge(secondSourcePdf, 1, secondSourcePdf.getNumberOfPages());
        //Merge
//        merger.merge();
        //Close the documents
        firstSourcePdf.close();
        secondSourcePdf.close();
        pdf.close();


    return true;

      /*  PdfADocument pdfDoc = new PdfADocument(
                new PdfReader(orig),
                new PdfWriter(dest));



        PdfDocument srcDoc;
        PdfFormXObject page;
        PdfCanvas canvas = new PdfCanvas(pdfDoc.getFirstPage().newContentStreamBefore(),
                pdfDoc.getFirstPage().getResources(), pdfDoc);
        for (String path : extra) {
            srcDoc = new PdfDocument(new PdfReader(path));
            page = srcDoc.getFirstPage().copyAsFormXObject(pdfDoc);
            canvas.addXObject(page, 0, 0);
            srcDoc.close();
        }
        pdfDoc.close();*/
    }




    public static final String SRC =
            "/Users/Heryk/Desktop/PDF_Teste.pdf";


    protected void manipulatePdf(String dest, String[] extra) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
        PdfDocument srcDoc;
        PdfFormXObject page;
        PdfCanvas canvas = new PdfCanvas(pdfDoc.getFirstPage().newContentStreamBefore(),
                pdfDoc.getFirstPage().getResources(), pdfDoc);
        for (String path : extra) {
            srcDoc = new PdfDocument(new PdfReader(path));
            page = srcDoc.getFirstPage().copyAsFormXObject(pdfDoc);
            canvas.addXObject(page, 0, 0);
            srcDoc.close();
        }
        pdfDoc.close();
    }



    public Boolean criarPdfA_OK(String orig, String dest, PdfAConformanceLevel pdfAConformanceLevel)  {

        try {

            PdfADocument pdf = new PdfADocument(new PdfWriter(dest),
                    pdfAConformanceLevel,
                    new PdfOutputIntent(
                            "Custom",
                            "", "http://www.color.org",
                            "sRGB IEC61966-2.1",
                            new FileInputStream(INTENT.getFile().getPath())));


            pdf.setDefaultPageSize(PageSize.A4);
            Document document = new Document(pdf);

            //Setting some required parameters
            pdf.setTagged();

            PdfFont font = PdfFontFactory.createFont(FONT.getFile().getPath(), PdfEncodings.WINANSI, true);
            Paragraph p = new Paragraph();
            p.setFont(font);
            p.add(new Text("The quick brown "));
            document.add(p);

            PdfDocument pdfDoc2 = new PdfDocument(new PdfReader("/Users/Heryk/Desktop/323404.pdf"));
            Document doc2 = new Document(pdfDoc2);
            Integer npg = pdfDoc2.getNumberOfPages();


            pdf.addNewPage();

            PdfPage page2 = pdf.getLastPage();

            PdfCanvas canvas2 = new PdfCanvas(page2);
            ImageData img7 = ImageDataFactory.create("/Users/Heryk/Desktop/img_1234.jpg");
            canvas2.addImage(img7, 0f, 0f, true);

            pdfDoc2.close();
            pdf.close();

            document.close();
            return true;
        }
        catch (IOException e) {
            return false;
        }


    }



    public Boolean criarPdfA_OK_2(String orig, String dest, PdfAConformanceLevel pdfAConformanceLevel)  {

        try {

            PdfADocument pdf = new PdfADocument(new PdfWriter(dest),
                    pdfAConformanceLevel,
                    new PdfOutputIntent(
                            "Custom",
                            "", "http://www.color.org",
                            "sRGB IEC61966-2.1",
                            new FileInputStream(INTENT.getFile().getPath())));


            pdf.setDefaultPageSize(PageSize.A4);
            Document document = new Document(pdf);

            //Setting some required parameters
            pdf.setTagged();

            PdfFont font = PdfFontFactory.createFont(FONT.getFile().getPath(), PdfEncodings.WINANSI, true);
            Paragraph p = new Paragraph();
            p.setFont(font);
            p.add(new Text("The quick brown "));
            document.add(p);


         /*   PDDocument document = PDDocument.load(orig);
            List<PDPage> pages = document.getDocumentCatalog().getPages();
            for (int i = 0; i < pages.size(); i++) {
                PDPage page = pages.get(i);
                BufferedImage image = page.convertToImage(BufferedImage.TYPE_INT_RGB, 72);
                ImageIO.write(image, "jpg", new File(pdffile.getAbsolutePath() + "_" + i + ".jpg"));
            }
*/



            pdf.addNewPage();
            PdfPage page2 = pdf.getLastPage();
            PdfCanvas canvas2 = new PdfCanvas(page2);
//            ImageData img7 = ImageDataFactory.create("/Users/Heryk/Desktop/323404/323404-1.jpg");
            ImageData img7 = ImageDataFactory.create("/Users/Heryk/Desktop/PDF_A_JAVA/img_pdf7.tiff");
            canvas2.addImage(img7, 0f, 0f, true);

            pdf.addNewPage();
            PdfPage page3 = pdf.getLastPage();
            PdfCanvas canvas3 = new PdfCanvas(page3);
//            ImageData img8 = ImageDataFactory.create("/Users/Heryk/Desktop/323404/323404-2.jpg");
            ImageData img8 = ImageDataFactory.create("/Users/Heryk/Desktop/PDF_A_JAVA/img_pdf8.tiff");
            canvas3.addImage(img8, 0f, 0f, true);

            pdf.close();

            document.close();
            return true;
        }
        catch (IOException e) {
            return false;
        }


    }



    public Boolean carimbar_assd(String arquivo, String dest, String carimbo, String carimbo2) throws IOException {

        Image img = new Image(ImageDataFactory.create(carimbo));
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(arquivo), new PdfWriter(dest));
        //CarimboEventHandler handler = new CarimboEventHandler(img);
        CarimboEventHandler handler = new CarimboEventHandler((String) "texto", null, null, Disposicao.DIREITA_SUPERIOR, "");
        pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, handler);
        pdfDoc.addNewPage();

        int n = pdfDoc.getNumberOfPages();

        PdfPage page = pdfDoc.getPage(n);
        PdfCanvas canvas = new PdfCanvas(page);
        // add new content
        Image img2 = new Image(ImageDataFactory.create(carimbo2));
        ImageData img23 = ImageDataFactory.create(carimbo2);

        canvas.addImage(img23, 50.0f, 200.0f, true);


        pdfDoc.close();

        //PdfDocument pdfDoc =
        //        new PdfDocument(new PdfReader(arquivo), new PdfWriter(dest));
        //Document document = new Document(pdfDoc);
     /*   Image img = new Image(ImageDataFactory.create(carimbo)).setFixedPosition(12, 300);

        PdfWriter pdfWriter = new PdfWriter(arquivo);
        Document document;
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        document = new Document(pdfDocument);
      */  //Paragraph p = new Paragraph();
        //Image carimboImage = new Image(ImageDataFactory.create(carimbo));
        //Set alt text
        //carimboImage.getAccessibilityProperties().setAlternateDescription("Carimbo");
        //p.add(carimboImage);


/*        PageSize pagesize;
        PdfCanvas canvas;



        int n = pdfDocument.getNumberOfPages();
        for (int i = 1; i <= n; i++) {
            PdfPage page = pdfDocument.getPage(i);
            pagesize = (PageSize) page.getPageSize();
            canvas = new PdfCanvas(page);
            // add new content
            canvas.addImage(imgCarimbo, 3.5f, 2.1f, true);
        }
*/
        //document.add(p);



//        PDFService handler = new PDFService(img);
//        pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE, handler);

//        document.close();

        //pdfDoc.close();
        return true;
    }



    public Boolean carimbarTexto(String arquivo, String dest, String carimbo, String carimbo2) throws IOException {

        Image img = new Image(ImageDataFactory.create(carimbo)).setFixedPosition(480, 780);
        ImageData img3 = ImageDataFactory.create(carimbo2);
        ImageData img23 = ImageDataFactory.create(carimbo);


        PdfDocument pdfDoc = new PdfDocument(new PdfReader(arquivo), new PdfWriter(dest));


        int n = pdfDoc.getNumberOfPages();
        PdfPage page7 = pdfDoc.getPage(1);



        PageSize ps = new PageSize(page7.getPageSize());
        img.scaleToFit(ps.getHeight(), ps.getHeight());
        img.scaleToFit(ps.getHeight(), ps.getHeight());


        for (int i = 1; i <= n; i++) {
            PdfPage page = pdfDoc.getPage(i);

            PdfCanvas canvas = new PdfCanvas(page);

            canvas.addImage(img23, 1800.0f, 3320.0f, false);
        }



        ps.setHeight(ps.getHeight());
        ps.setWidth(ps.getWidth());

        pdfDoc.addNewPage(ps);


        PdfPage page2 = pdfDoc.getPage(n+1);
        PdfCanvas canvas2 = new PdfCanvas(page2);

        canvas2.addImage(img23, 1800.0f, 3320.0f, false);
        canvas2.addImage(img3, 500.0f, 2800.0f, true);

        pdfDoc.close();
        return true;
    }


    public Boolean pdf() {

        return true;
    }


    /*
    	public static void main(String[] args) {
			PDFService service = new PDFService();
			service.converte("/Users/rseixas/Projects/Exmart/rtdpjlite/rtdpjlite-data/src/test/resources/pdfs/322873.pdf", "/Users/rseixas/Projects/Exmart/rtdpjlite/rtdpjlite-data/src/test/resources/pdfs/convertido.pdf");
		}
    */
    /*
    abrir.pdf
    salvar
    add(.pdf / .tiff)
    assinar
    assinar (stamp)
    assinatura.getAssinantes
    assinatura.isvalid
    atachar.documento
    atachar.xml
    */

    public File converteAssinaPdfCertidao(File arquivoOriginal, String diretorioConvertido, String motivo, String txtCarimboCertidao, String txtExplicacaoAssinatura, String urlQrcode, Long idServicoCalculado, String hashArquivo, DisposicaoPagina disposicaoPagina, Disposicao disposicaoCarimbo, File arquivoAnexar, boolean icDigital, RetornoSelo retornoSelo){
        File retorno = converteAssinaPdf(arquivoOriginal,diretorioConvertido, motivo,txtCarimboCertidao,txtExplicacaoAssinatura,urlQrcode, hashArquivo, disposicaoPagina, disposicaoCarimbo, arquivoAnexar, icDigital, retornoSelo);
        String arquivoTo = retorno.getParent()+File.separator;
        try {
            FileUtils.copyFile(retorno, new File(arquivoTo + idServicoCalculado.toString() + ".PDF"));
//            Files.copy(retorno.toPath(), new File(arquivoTo + idServicoCalculado.toString() + ".PDF").toPath(), StandardCopyOption.REPLACE_EXISTING);
            retorno.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new File(arquivoTo + idServicoCalculado.toString() + ".PDF");
    }
    public File converteAssinaPdf(File arquivoOriginal, String diretorioConvertido, String motivo, String txtCarimboCertidao, String txtExplicacaoAssinatura, String urlQrcode, String hashArquivo, DisposicaoPagina disposicaoPagina, Disposicao disposicaoCarimbo, File arquivoAnexar, boolean icDigital, RetornoSelo retornoSelo){
        File diretorio = new File(diretorioConvertido);
        logger.debug("Diretorio Convertiod {}", diretorioConvertido);
        diretorio.mkdirs();

        try {
            logger.debug("comecou converter");
            File convertido = this.converte(arquivoOriginal.getPath(), diretorioConvertido , arquivoOriginal.getName());
            logger.debug("Arquivo Convertido {}", convertido.getAbsoluteFile());
            logger.debug("finalizou converter");
            String arquivoConvertido = convertido.getAbsolutePath();

            logger.debug("comecou carimbar");
            PdfHash arqRetorno = carimbar(convertido, motivo,
                    txtCarimboCertidao,
                    txtExplicacaoAssinatura,
                    urlQrcode,
                    hashArquivo,
                    disposicaoCarimbo,
                    disposicaoPagina,
                    arquivoAnexar, diretorioConvertido+File.separator + "_"+arquivoOriginal.getName(),
                    icDigital,
                    retornoSelo);

            logger.debug("finalizou carimbar");
            File arquivoCarimbado = arqRetorno.getOrig();

            convertido.delete();

            File retorno = new File(diretorioConvertido+File.separator + "_"+arquivoOriginal.getName());
            retorno.renameTo(new File(arquivoConvertido));
            return  new File(arquivoConvertido);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }


    public File exportarPagina(File arquivo, DisposicaoPagina disposicao) throws IOException {
        if(arquivo == null){
            throw new IOException("Arquivo não pode ser null");
        }
        PDDocument pdDoc = PDDocument.load(arquivo);

//Creates a new pdf document
        PDDocument document = null;

        try {

            int paginaExportar =0;
            if(disposicao.equals(DisposicaoPagina.ULTIMA_PAGINA)){
                paginaExportar = pdDoc.getNumberOfPages();
                paginaExportar = paginaExportar -1;
            }

            document = new PDDocument();
            document.addPage((PDPage) pdDoc.getDocumentCatalog().getPages().get(paginaExportar));
            File retorno = File.createTempFile(Calendar.getInstance().getTimeInMillis() + "_" + disposicao.getValor(), ".PDF");
            document.save(retorno);
            document.close();
            return retorno;
        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public File pdfMergeAllgsWithoutPdfA(List<String> arquivos, String arqDestinoPath) throws Exception {
        logger.info("comecou merge arquivos");

        if (!this.icGhostiscript) {
            throw new Exception(getMensagemErro());
        }
        StringBuffer output = new StringBuffer();
        Process process = null;
        //Usa o Ghostscript para realizar o merge de arquivos


        String listaPdf = "";
        for (String arquivo: arquivos) {
            listaPdf = listaPdf + " " + arquivo;
        }

        String novoComando = comandoMerge.format(comandoMerge, arqDestinoPath, listaPdf);
        //System.out.println(novoComando);
        //    		System.outSystem.out.println(comando);
        //System.exit(0);
//        try {
            ExecShell execShell = new ExecShell();
            execShell.exec(novoComando);
//            process = Runtime.getRuntime().exec(novoComando);
//            process.waitFor();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//
//            //Captura o retorno do shell
//            String retorno = "";
//            while ((retorno = reader.readLine()) != null) {
//                output.append(retorno + "\n");
//            }
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
        return new File(arqDestinoPath);
    }

    public File converteA4(File arquivoOriginal) throws Exception {
        logger.debug("converter A4", arquivoOriginal.getAbsoluteFile());
        String arquivoSaida = arquivoOriginal.getAbsolutePath()+".pdf";
        String arquivoEntrada = arquivoOriginal.getAbsolutePath();
        if(!this.icGhostiscript){
            throw  new Exception(getMensagemErro());
        }
        StringBuffer output = new StringBuffer();
        Process process = null;


        String novoComando = converterA4.format(converterA4,arquivoSaida, arquivoOriginal);
        logger.debug("Executando {}",novoComando);
        ExecShell execShell = new ExecShell();
        execShell.exec(novoComando);

        arquivoOriginal.delete();
        FileUtils.moveFile(new File(arquivoSaida),
                new File(arquivoEntrada)
                );

        File retorno = new File(arquivoEntrada);

        logger.debug("arquivo para retorno: {}", retorno.getAbsoluteFile());
        logger.debug("existe arquivo {}", retorno.exists());
        return retorno;
    }

}
