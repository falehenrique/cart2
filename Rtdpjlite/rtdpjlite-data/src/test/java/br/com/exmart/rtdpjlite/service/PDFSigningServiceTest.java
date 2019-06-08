package br.com.exmart.rtdpjlite.service;

import br.com.exmart.rtdpjlite.BaseTest;
import br.com.exmart.rtdpjlite.util.RetornoSelo;
import br.com.exmart.util.Disposicao;
import br.com.exmart.util.PdfHash;
import org.apache.commons.io.FileUtils;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.util.encoders.Base64;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PDFSigningServiceTest extends BaseTest{


    @Value("${URL_PDF_TESTE}PDF_PARA_ASSINATURA.pdf")
    private Resource _pdfparaassinatura;

    @Value("${URL_PDF_TESTE}RTD05201803211530749.pdf")
    private Resource _pdfparaassinatura_f;


    public static String ASSINADO_DIG_F = "PDF_ASSINADO_DIG_F.pdf";
    public static String CARIMBADO_F = "RTD05201803211530749.pdf";

    public static String CONVERTIDO = "PDF_CONVERTIDO.pdf";
    public static String ASSINADO_DIG = "PDF_ASSINADO_DIG.pdf";
    public static String CARIMBADO = "PDF_CARIMBADO.pdf";

    public static String ORIGINAL = "src/test/resources/pdfs/selo.pdf";
    public static String COASSINADO = "src/test/resources/pdfs/hello_signed1.pdf";
    public static String ASSINADO = "assinado.pdf";
    public static String PATH = "src/test/resources/pdfs/";

    @Autowired 
	SigningService signing;
    @Autowired
    PKCS7Signer pkcs7Signer;

    @Autowired
    private PDFService pdfService; // = new PDFService();


    @Test
    public void A_when_Sign() throws IOException {
		try {
			Path path = Paths.get(COASSINADO);
			File retorno = signing.sign7(path.toFile(),PATH,ASSINADO,
                    "Motivo da assinatura",
                    "Local da assinatura",
                    LocalDateTime.now(),
                    "Certidão eletrônica, com valor de original, do documento registrado sob o número 326.084 em 15/01/2018, assinada digitalmente pelo 2º Oficial de Registro de Títulos e Documentos e Civil de Pessoa Jurídica de Osasco",
                    true,
                    PDFService.DisposicaoPagina.PRIMEIRA_PAGINA,
                    path.toFile());
			assertThat(retorno != null);
		} catch (Exception  e) {
			fail(e.getMessage());
		}
    }


    @Test
    public void A_when_converte() {
        try {
            System.out.println(_pdfparaassinatura.getFile().getAbsolutePath());

            pdfService.converte(
                    _pdfparaassinatura.getFile().getAbsolutePath(),
                    _pdfparaassinatura.getFile().getParent() ,   CONVERTIDO);
            assertThat(true).isTrue();
        } catch (Exception e) {
            fail(e.getMessage());
        }

    }


    @Test
    public void B_when_carimbar() throws IOException {
        PdfHash retornoA = null;
        try {

            //FIXME Fabio Ebner Alterar com o objeto do selo
            RetornoSelo retSelo = new RetornoSelo(
                    "urlTribunalJustica",
                    "Selo Digital 1137614TICD000004440EA183",
                    "total",
                    "iss",
                    "assinaturaDigital",
                    "https://selodigital.tjsp.jus.br/?r=1234561AB123456789012318I%7C1000%7C50%7CyYVHtIzr7snVc\n" +
                            "0dDAbLVEe9ko7L4o40m6wpRrJqOcWWDQyg8NrXTt%2BRlWpBCafAtnjaAKmf52kZq8PKt9m8hB6\n" +
                            "GveKA8WbCFQoX4YOUskthcwH1Ek63fdgME2Eiy1da1TqL4NH7G3PShvEwtPFhWLXI%2Bv%2B\n" +
                            "wJxbJPobIJ4tyM4FI6jfT0cEaycLAQ0TwZhSSk%2FP%2BAw40K%2FLl%2FB6hSjR9AokWDdRsSZ\n" +
                            "GAx%2BD16WlpF2Tj9JgAQi4lA5f6e9IWR3d4qHFPL1N628x8d5zQVt%2Fg%2F%2FqjSz4VEhYIoi\n" +
                            "UWJnRzh4zDYhMDX96zzayYCo5wDChSNBnOvuzTgFD%2B02KlKtJDy9g%3D%3D",
                    "Para verificar a autenticidade do documento, acesse o site da Corregedoria Geral da Justiça: https://selodigital.tjsp.jus.br.",
                    null
            );


            retornoA = pdfService.carimbar(_pdfparaassinatura.getFile(),
                    "CERTIDÃO DE REGISTRO: Certifico que foi apresentado este documento,<br> com {{PAGINAS}} páginas, " +
                            "registrado sob o número [[NUMERO_REGISTRO]] em {{DATA_REGISTRO}} neste 2º Oficial de Registro, " +
                            "Títulos e Documentos e Civil de Pessoa Jurídica de Osasco, possui o mesmo valor probante do original " +
                            "para todos os fins de Direito, seja em Juízo ou fora dele, nos termos dos artigos 161" +
                            " da Lei n. 6.015/73 e 217 da Lei 10.406/02 e foi extraída sob forma de documento eletrônico devendo" +
                            " para validade ser conservada em meio eletrônico, bem como comprovada a autoria e integridade. " +
                            "Osasco, {{DATA_EXTENSO}}. {{NOME_CARTORIO}}, CNPJ {{CNPJ_CARTORIO}}. Certifico ainda, que a assinatura" +
                            " digital constante neste documento eletrônico está em conformidade com os padrões da ICP-Brasil, " +
                            "nos termos da Lei 11.977 de 07 de julho de 2009.",

                    "Certidão eletrônica, com valor de original, do documento registrado sob o número 326.084 em 15/01/2018, assinada digitalmente pelo 2º Oficial de Registro de Títulos e Documentos e Civil de Pessoa Jurídica de Osasco",


                    "Documento assinado digitalmente em Conformidade do Padrão Brasileiro de Assinatura Digital, " +
                            "padrão ICP-Brasil. Validação do atributo de assinatura digital www.lumera.com.br/rtdpj/validao. HASH: <HASH>.  " +
                            "Este é um documento público eletrônico, emitido nos termos da Medida Provisória de nº 2200-2, de 24/08/2001, só tendo validade em formato digital. Vedada a sua reprodução.",
                    "http://www.lumera.com.br/",
                    pdfService.gerarHash(),
                    Disposicao.DIREITA_SUPERIOR,
                    PDFService.DisposicaoPagina.ULTIMA_PAGINA,
                    null,
                    _pdfparaassinatura.getFile().getParentFile() + File.separator + CARIMBADO,
                    true,
                    retSelo);
        } catch (NoSuchProviderException e) {
            fail(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            fail(e.getMessage());
        }


        assertThat(retornoA.getHash().length() > 0).isTrue();



    }


    @Test
    public void D_when_Sign() throws IOException {
        try {
            File arquivoOrig = new File(_pdfparaassinatura.getFile().getParentFile() + File.separator + CARIMBADO);


            File retorno = signing.sign7(arquivoOrig,_pdfparaassinatura.getFile().getParent(),
                    ASSINADO_DIG,
                    "Motivo da assinatura",
                    "Local da assinatura",
                    LocalDateTime.now(),
                    "Certidão eletrônica, com valor de original, do documento registrado sob o número 328.403 em 23/03/2018, assinada digitalmente pelo 2º Oficial de Registro de Títulos e Documentos e Civil de Pessoa Jurídica de Osasco",
                    true,
                    PDFService.DisposicaoPagina.PRIMEIRA_PAGINA,
                    arquivoOrig);

            assertThat(retorno != null);
        } catch (Exception  e) {
            e.printStackTrace();
            fail( e.getMessage() );
        }
    }


    @Test
    public void E_when_Sign_F() throws IOException {
        try {
            File arquivoOrig = new File(_pdfparaassinatura_f.getFile().getParentFile() + File.separator + CARIMBADO_F);


              File retorno = signing.sign7(arquivoOrig,_pdfparaassinatura_f.getFile().getParent(),
                    ASSINADO_DIG_F,
                    "Motivo da assinatura",
                    "Local da assinatura",
                    LocalDateTime.now(),
                    "Certidão eletrônica, com valor de original, do documento registrado sob o número 328.403 em 23/03/2018, assinada digitalmente pelo 2º Oficial de Registro de Títulos e Documentos e Civil de Pessoa Jurídica de Osasco",
                    true,
                    PDFService.DisposicaoPagina.ULTIMA_PAGINA,
                    arquivoOrig );

            assertThat(retorno != null);
        } catch (Exception  e) {
            e.printStackTrace();
            fail( e.getMessage() );
        }
    }

//    @Test
    public void testar(){

//        PKCS7Signer signer = new PKCS7Signer();
        try {
//            PKCS7Signer signer = new PKCS7Signer();
            System.out.println("binhoca 2");
            KeyStore keyStore = pkcs7Signer.loadKeyStore();
//            CMSSignedDataGenerator signatureGenerator = pkcs7Signer.setUpProvider(keyStore);

            String content = "some bytes to be signed";

            File arquivo = new File("/Users/fabioebner/Downloads/Registro_Teste.pdf");

//            byte[] signedBytes = pkcs7Signer.signPkcs7(content.getBytes("UTF-8"), signatureGenerator);
//            byte[] signedBytes = pkcs7Signer.signPkcs7(arquivo);
//            FileUtils.writeByteArrayToFile(new File("/Users/fabioebner/Downloads/Teste2.p7s"), signedBytes);
//            String signedString = new String(Base64.encode(signedBytes));
//            System.out.println("Signed Encoded Bytes: " + signedString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
