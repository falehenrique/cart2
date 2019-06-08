package br.com.exmart.rtdpjlite.service;

import br.com.exmart.rtdpjlite.BaseTest;
import br.com.exmart.rtdpjlite.util.RetornoSelo;
import br.com.exmart.util.Disposicao;
import br.com.exmart.util.PdfHash;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * Created by Heryk on 09/11/17.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PDFServiceTest extends BaseTest {

    @Autowired
    private PDFService pdfService; // = new PDFService();

    @Value("${URL_PDF_TESTE}PDF_SizeA4.pdf")
    private Resource _pdf;

    @Value("${URL_PDF_TESTE}PDF_A.pdf")
    private Resource _pdfa;
    @Value("${URL_PDF_TESTE}PDF_Carimbado.pdf")
    private Resource _pdfCarimbado;

    @Value("${URL_PDF_TESTE}Certidão_do_registro_317324_teste.pdf")
    private Resource _pdfErroGetPaginas;

    @Value("${URL_PDF_TESTE}PDF_PARA_ASSINATURA.pdf")
    private Resource _pdfparaassinatura;

    @Value("${URL_PDF_TESTE}ITAU_teste.pdf.p7s")
    private Resource _p7s;


    @Test
    public void A_when_getPaginasPDF() throws IOException {
        Integer retorno = pdfService.getPaginas(_pdf.getFile());

        //logger.info("Quantidade de paginas para PDF - ESPERADO: 2 - Retorno: " + retorno);
        assertThat(retorno == 2).isTrue();
    }


    @Test
    public void B_when_getPaginasPDFA() throws IOException {
        Integer retorno = pdfService.getPaginas(_pdfa.getFile());

        //logger.info("Quantidade de paginas para PDF/A - ESPERADO: 3 - Retorno: " + retorno);
        assertThat(retorno == 3).isTrue();
    }


//    @Test
//    public void C_when_deletePag() throws IOException {
//        byte[] aOrig = Files.readAllBytes(_pdf.getFile().toPath());
//        byte[] retorno = pdfService.excluirPagina(aOrig, 2);
//
//        FileOutputStream aDest = new FileOutputStream(_pdf.getFile().getParentFile() + File.separator + "PDF_A4_PG_EXCLUIDA.pdf");
//        aDest.write(retorno);
//        aDest.close();
//
//        assertThat(pdfService.getPaginas(_pdf.getFile()) == 2 && pdfService.getPaginas(retorno) == 1 ).isTrue();
//    }


    @Test
    public void D_when_carimbar_sem_link() throws IOException, NoSuchProviderException, NoSuchAlgorithmException {
        //byte[] aOrig = Files.readAllBytes(new File(this.getClass().getClassLoader().getResource( "util" + File.separator + "PDF" + File.separator + "pdf.pdf").getPath()).toPath());

        RetornoSelo retSelo = new RetornoSelo(
            "urlTribunalJustica",
                "1137614TICD000004440EA183",
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

        PdfHash retornoA = pdfService.carimbar(_pdf.getFile(),
                "CERTIDÃO DE REGISTRO: Certifico que foi apresentado este documento,<br> com {{PAGINAS}} páginas, " +
                        "registrado sob o número [[NUMERO_REGISTRO]] em {{DATA_REGISTRO}} neste 2º Oficial de Registro, " +
                        "Títulos e Documentos e Civil de Pessoa Jurídica de Osasco, possui o mesmo valor probante do original " +
                        "para todos os fins de Direito, seja em Juízo ou fora dele, nos termos dos artigos 161" +
                        " da Lei n. 6.015/73 e 217 da Lei 10.406/02 e foi extraída sob forma de documento eletrônico devendo" +
                        " para validade ser conservada em meio eletrônico, bem como comprovada a autoria e integridade. " +
                        "Osasco, {{DATA_EXTENSO}}. {{NOME_CARTORIO}}, CNPJ {{CNPJ_CARTORIO}}. Certifico ainda, que a assinatura" +
                        " digital constante neste documento eletrônico está em conformidade com os padrões da ICP-Brasil, " +
                        "nos termos da Lei 11.977 de 07 de julho de 2009.",

                "Certidão eletrônica, com valor de original, do documento registrado sob o " +
                        "número 323.404 em 10/11/2017, assinada digitalmente pelo 2º Oficial de Registro " +
                        "de Títulos e Documentos e Civil de Pessoa Jurídica de Osasco",


                "Documento assinado digitalmente em Conformidade do Padrão Brasileiro de Assinatura Digital, padrão " +
                        "ICP-Brasil. Validação do atributo de assinatura digital http://rtdpjportal.exmart.com.br//documento/validar/e843ac8f. " +
                        "Este é um documento público eletrônico, emitido nos " +
                        "termos da Medida Provisória de nº 2200-2, de 24/08/2001, só tendo validade em formato digital. Vedada a " +
                        "sua reprodução.",

//                "Documento assinado digitalmente em Conformidade do Padrão Brasileiro de Assinatura Digital, " +
//                        "padrão ICP-Brasil. Validação do atributo de assinatura digital %%%www.lumera.com.br/rtdpj/validao%%%. HASH: <HASH>.  " +
//                        "Este é um documento público eletrônico, emitido nos termos da Medida Provisória de nº 2200-2, de 24/08/2001, só tendo validade em formato digital. Vedada a sua reprodução. Testando o numero de linhas para que o carimbo seja igual ao em produção",
                "http://www.lumera.com.br/",
                pdfService.gerarHash(),
                Disposicao.DIREITA_SUPERIOR,
                PDFService.DisposicaoPagina.ULTIMA_PAGINA,
                null,
                _pdf.getFile().getParentFile() + File.separator + "PDF_Carimbado.pdf",
                true,
                retSelo);


        assertThat(retornoA.getHash().length() > 0).isTrue();
    }

    @Test
    public void D_when_carimbar_sem_link_sem_selo() throws IOException, NoSuchProviderException, NoSuchAlgorithmException {
        //byte[] aOrig = Files.readAllBytes(new File(this.getClass().getClassLoader().getResource( "util" + File.separator + "PDF" + File.separator + "pdf.pdf").getPath()).toPath());

        PdfHash retornoA = pdfService.carimbar(_pdf.getFile(),
                "CERTIDÃO DE REGISTRO: Certifico que foi apresentado este documento,<br> com {{PAGINAS}} páginas, " +
                        "registrado sob o número [[NUMERO_REGISTRO]] em {{DATA_REGISTRO}} neste 2º Oficial de Registro, " +
                        "Títulos e Documentos e Civil de Pessoa Jurídica de Osasco, possui o mesmo valor probante do original " +
                        "para todos os fins de Direito, seja em Juízo ou fora dele, nos termos dos artigos 161" +
                        " da Lei n. 6.015/73 e 217 da Lei 10.406/02 e foi extraída sob forma de documento eletrônico devendo" +
                        " para validade ser conservada em meio eletrônico, bem como comprovada a autoria e integridade. " +
                        "Osasco, {{DATA_EXTENSO}}. {{NOME_CARTORIO}}, CNPJ {{CNPJ_CARTORIO}}. Certifico ainda, que a assinatura" +
                        " digital constante neste documento eletrônico está em conformidade com os padrões da ICP-Brasil, " +
                        "nos termos da Lei 11.977 de 07 de julho de 2009.",

                "Certidão eletrônica, com valor de original, do documento registrado sob o " +
                        "número 323.404 em 10/11/2017, assinada digitalmente pelo 2º Oficial de Registro " +
                        "de Títulos e Documentos e Civil de Pessoa Jurídica de Osasco",


                "Documento assinado digitalmente em Conformidade do Padrão Brasileiro de Assinatura Digital, padrão " +
                        "ICP-Brasil. Validação do atributo de assinatura digital http://rtdpjportal.exmart.com.br//documento/validar/e843ac8f. " +
                        "Este é um documento público eletrônico, emitido nos " +
                        "termos da Medida Provisória de nº 2200-2, de 24/08/2001, só tendo validade em formato digital. Vedada a " +
                        "sua reprodução.",

//                "Documento assinado digitalmente em Conformidade do Padrão Brasileiro de Assinatura Digital, " +
//                        "padrão ICP-Brasil. Validação do atributo de assinatura digital %%%www.lumera.com.br/rtdpj/validao%%%. HASH: <HASH>.  " +
//                        "Este é um documento público eletrônico, emitido nos termos da Medida Provisória de nº 2200-2, de 24/08/2001, só tendo validade em formato digital. Vedada a sua reprodução. Testando o numero de linhas para que o carimbo seja igual ao em produção",
                "http://www.lumera.com.br/",
                pdfService.gerarHash(),
                Disposicao.DIREITA_SUPERIOR,
                PDFService.DisposicaoPagina.ULTIMA_PAGINA,
                null,
                _pdf.getFile().getParentFile() + File.separator + "PDF_Carimbado_sem_selo_sp.pdf",
                true,
                null);


        assertThat(retornoA.getHash().length() > 0).isTrue();
    }

    @Test
    public void D_when_carimbar_sem_link_fisico() throws IOException, NoSuchProviderException, NoSuchAlgorithmException {
        //byte[] aOrig = Files.readAllBytes(new File(this.getClass().getClassLoader().getResource( "util" + File.separator + "PDF" + File.separator + "pdf.pdf").getPath()).toPath());

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


        PdfHash retornoA = pdfService.carimbar(_pdf.getFile(),
                "REGISTRO: Certifico que foi apresentado este documento, com 66 páginas, e que os Termos de Abertura e" +
                        "Encerramento do Livro foram registrados sob nº 344246 em 20/02/2019 neste 2º Oficial de Registro de Títulos" +
                        "e Documentos e Civil de Pessoa Jurídica de Osasco, possui o mesmo valor probante do original para todos os" +
                        "fins de Direito, seja em juízo ou fera dele, nos termos dos artigos 161 da Lei n. 6.015/73 e 217 da Lei 10.404/02" +
                        "e foi extraída sob forma de documento eletrônico devendo para validade ser conservado em meio eletrônico," +
                        "bem como, comprovada autoria e integridade. Osasco, 20 de Fevereiro de 2019. 2º Oficial de Registro de" +
                        "Imóveis, Títulos e Documentos e Civil de Pessoa Jurídica de Osasco, CNPJ 51.241.396/0001-08. Certifico ainda," +
                        "que a assinatura digital constante neste documento eletrônico está em conformidade com os padrões da ICPBrasil," +
                        "nos termos da Lei 11.977 de 07 de julho de 2009. [Cartorio R$: 54,43, Estado R$: 15,48, Secretaria da" +
                        "Fazenda R$: 10,59, Sinoreg R$: 2,86, Trib.Justiça R$: 3,73, MP R$: 2,61, ISS R$: 1,08, Outros R$: 0,00] - Total R$: 2344342" ,

                "Certidão eletrônica, com valor de original, do documento registrado sob o " +
                        "número 323.404 em 10/11/2017, assinada digitalmente pelo 2º Oficial de Registro " +
                        "de Títulos e Documentos e Civil de Pessoa Jurídica de Osasco",


                "Documento assinado digitalmente em Conformidade do Padrão Brasileiro de Assinatura Digital, padrão " +
                        "ICP-Brasil. Validação do atributo de assinatura digital http://rtdpjportal.exmart.com.br//documento/validar/e843ac8f. " +
                        "Este é um documento público eletrônico, emitido nos " +
                        "termos da Medida Provisória de nº 2200-2, de 24/08/2001, só tendo validade em formato digital. Vedada a " +
                        "sua reprodução.",

//                "Documento assinado digitalmente em Conformidade do Padrão Brasileiro de Assinatura Digital, " +
//                        "padrão ICP-Brasil. Validação do atributo de assinatura digital %%%www.lumera.com.br/rtdpj/validao%%%. HASH: <HASH>.  " +
//                        "Este é um documento público eletrônico, emitido nos termos da Medida Provisória de nº 2200-2, de 24/08/2001, só tendo validade em formato digital. Vedada a sua reprodução. Testando o numero de linhas para que o carimbo seja igual ao em produção",
                "http://www.lumera.com.br/",
                pdfService.gerarHash(),
                Disposicao.DIREITA_SUPERIOR,
                PDFService.DisposicaoPagina.ULTIMA_PAGINA,
                null,
                _pdf.getFile().getParentFile() + File.separator + "PDF_Carimbado_FISICO.pdf",
                false,
                retSelo);


        assertThat(retornoA.getHash().length() > 0).isTrue();
    }


    @Test
    public void D_when_carimbar_com_link() throws IOException, NoSuchProviderException, NoSuchAlgorithmException {
        //byte[] aOrig = Files.readAllBytes(new File(this.getClass().getClassLoader().getResource( "util" + File.separator + "PDF" + File.separator + "pdf.pdf").getPath()).toPath());

        RetornoSelo retSelo = new RetornoSelo(
                "urlTribunalJustica",
                "Selo Digital 1137614TICD000004440EA183",
                "156.700,12",
                "923,12",
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

        PdfHash retornoA = pdfService.carimbar(_pdf.getFile(),
                "CERTIDÃO DE REGISTRO: Certifico que foi apresentado este documento,<br> com {{PAGINAS}} páginas, " +
                        "registrado sob o número [[NUMERO_REGISTRO]] em {{DATA_REGISTRO}} neste 2º Oficial de Registro, " +
                        "Títulos e Documentos e Civil de Pessoa Jurídica de Osasco, possui o mesmo valor probante do original " +
                        "para todos os fins de Direito, seja em Juízo ou fora dele, nos termos dos artigos 161" +
                        " da Lei n. 6.015/73 e 217 da Lei 10.406/02 e foi extraída sob forma de documento eletrônico devendo" +
                        " para validade ser conservada em meio eletrônico, bem como comprovada a autoria e integridade. " +
                        "Osasco, {{DATA_EXTENSO}}. {{NOME_CARTORIO}}, CNPJ {{CNPJ_CARTORIO}}. Certifico ainda, que a assinatura" +
                        " digital constante neste documento eletrônico está em conformidade com os padrões da ICP-Brasil, " +
                        "nos termos da Lei 11.977 de 07 de julho de 2009.",

                "Certidão eletrônica, com valor de original, do documento registrado sob o " +
                        "número 323.404 em 10/11/2017, assinada digitalmente pelo 2º Oficial de Registro " +
                        "de Títulos e Documentos e Civil de Pessoa Jurídica de Osasco",

                "Documento assinado digitalmente em Conformidade do Padrão Brasileiro de Assinatura Digital, padrão " +
                        "ICP-Brasil. Validação do atributo de assinatura digital %%%http://rtdpjportal.exmart.com.br//documento/validar/e843ac8f%%%. " +
                        "Este é um documento público eletrônico, emitido nos " +
                        "termos da Medida Provisória de nº 2200-2, de 24/08/2001, só tendo validade em formato digital. Vedada a " +
                        "sua reprodução.",


//                "Documento assinado digitalmente em Conformidade do Padrão Brasileiro de Assinatura Digital, " +
//                        "padrão ICP-Brasil. Validação do atributo de assinatura digital www.lumera.com.br/rtdpj/validao. HASH: <HASH>.  " +
//                        "Este é um documento público eletrônico, emitido nos termos da Medida Provisória de nº 2200-2, de 24/08/2001, só tendo validade em formato digital. Vedada a sua reprodução.",

                "http://www.lumera.com.br/",
                pdfService.gerarHash(),
                Disposicao.DIREITA_SUPERIOR,
                PDFService.DisposicaoPagina.ULTIMA_PAGINA,
                null,
                _pdf.getFile().getParentFile() + File.separator + "PDF_Carimbado_link.pdf",
                true,
                retSelo);



        assertThat(retornoA.getHash().length() > 0).isTrue();
    }

    @Test
    public void D_when_carimbar_com_link_p7s_anexo() throws IOException, NoSuchProviderException, NoSuchAlgorithmException {
        //byte[] aOrig = Files.readAllBytes(new File(this.getClass().getClassLoader().getResource( "util" + File.separator + "PDF" + File.separator + "pdf.pdf").getPath()).toPath());

        RetornoSelo retSelo = new RetornoSelo(
                "urlTribunalJustica",
                "Selo Digital 1137614TICD000004440EA183",
                "156.700,12",
                "923,12",
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


        PdfHash retornoA = pdfService.carimbar(_pdf.getFile(),
                "CERTIDÃO DE REGISTRO: Certifico que foi apresentado este documento,<br> com {{PAGINAS}} páginas, " +
                        "registrado sob o número [[NUMERO_REGISTRO]] em {{DATA_REGISTRO}} neste 2º Oficial de Registro, " +
                        "Títulos e Documentos e Civil de Pessoa Jurídica de Osasco, possui o mesmo valor probante do original " +
                        "para todos os fins de Direito, seja em Juízo ou fora dele, nos termos dos artigos 161" +
                        " da Lei n. 6.015/73 e 217 da Lei 10.406/02 e foi extraída sob forma de documento eletrônico devendo" +
                        " para validade ser conservada em meio eletrônico, bem como comprovada a autoria e integridade. " +
                        "Osasco, {{DATA_EXTENSO}}. {{NOME_CARTORIO}}, CNPJ {{CNPJ_CARTORIO}}. Certifico ainda, que a assinatura" +
                        " digital constante neste documento eletrônico está em conformidade com os padrões da ICP-Brasil, " +
                        "nos termos da Lei 11.977 de 07 de julho de 2009.",

                "Certidão eletrônica, com valor de original, do documento registrado sob o " +
                        "número 323.404 em 10/11/2017, assinada digitalmente pelo 2º Oficial de Registro " +
                        "de Títulos e Documentos e Civil de Pessoa Jurídica de Osasco",

                "Documento assinado digitalmente em Conformidade do Padrão Brasileiro de Assinatura Digital, padrão " +
                        "ICP-Brasil. Validação do atributo de assinatura digital %%%http://rtdpjportal.exmart.com.br//documento/validar/e843ac8f%%%. " +
                        "Este é um documento público eletrônico, emitido nos " +
                        "termos da Medida Provisória de nº 2200-2, de 24/08/2001, só tendo validade em formato digital. Vedada a " +
                        "sua reprodução.",


//                "Documento assinado digitalmente em Conformidade do Padrão Brasileiro de Assinatura Digital, " +
//                        "padrão ICP-Brasil. Validação do atributo de assinatura digital www.lumera.com.br/rtdpj/validao. HASH: <HASH>.  " +
//                        "Este é um documento público eletrônico, emitido nos termos da Medida Provisória de nº 2200-2, de 24/08/2001, só tendo validade em formato digital. Vedada a sua reprodução.",

                "http://www.lumera.com.br/",
                pdfService.gerarHash(),
                Disposicao.DIREITA_SUPERIOR,
                PDFService.DisposicaoPagina.ULTIMA_PAGINA,
                _p7s.getFile(),
                _pdf.getFile().getParentFile() + File.separator + "PDF_Carimbado_link_p7s_anexo.pdf",
                true ,
                retSelo);



        assertThat(retornoA.getHash().length() > 0).isTrue();
    }







    @Test
    public void E_when_gerar_hash() throws NoSuchProviderException, NoSuchAlgorithmException, IOException{
        String hs = pdfService.gerarHash();
        System.out.println(hs);
        assertThat( hs.length() == 8).isTrue();
    }


    //@Test
    public void F_when_gerar_hash_duplicado() {
        String hs = null;
        String hs2 = null;

        try {
            hs = pdfService.gerarHash();
            hs2 = pdfService.gerarHash();
        } catch (NoSuchProviderException e) {
            fail(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        }

        System.out.println(hs + " - " + hs2);
        assertThat( hs.equals(hs2) ).isFalse();
    }


    @Test
    public void H_testaSepararPagina() throws IOException {
        File retorno = pdfService.exportarPagina(this._pdfCarimbado.getFile(), PDFService.DisposicaoPagina.ULTIMA_PAGINA);
        Integer totalPaginas = pdfService.getPaginas(retorno);
        System.out.println("Total de Paginas " + totalPaginas);
        Assert.assertTrue(totalPaginas == 1);
    }

    @Test
    public void G_when_html_pdf() {

        try {
            File arquivoDestino =
            pdfService.htmlToPdf(

                    "<html>CERTIDÃO DE <b>REGISTRO</b>: Certifico que foi apresentado este documento, com 1 páginas, registrado sob o número [[NUMERO_REGISTRO]] em 22/02/2018 neste 2º Oficial de Registro, Títulos e Documentos e Civil de Pessoa Jurídica de Osasco, possui o mesmo valor<div><div style=\"text-align: center;\">probante do original para todos os fins de Direito, s</div><div>eja em Juízo ou fora dele, nos termos dos artigos 161 da Lei n. 6.015/73 e 217 da Lei 10.406/02 e foi extraída sob forma de documento eletrônico devendo para validade ser conservada em meio eletrônico, bem como comprovada a autoria e <u>integridade</u>. Osasco,  22 de</div><div><ol><li>Fevereiro de 2018. 2º Oficial de Registro de Imóveis, Títulos e</li><li>Documentos e Civil de Pessoa Jurídica de Osasco, CNPJ 51.241.396/0001-08.</li><li>Certifico ainda, que a assinatura digital constante neste documento eletrônico está em conformidade com os padrões da ICP-</li></ol></div><div><br></br></div><div>Brasil, nos termos da Lei 11.977 de 07 de julho de 2009. [Cartorio R$: 0,00, Estad</div><div><br></br></div></div><blockquote style=\"margin: 0 0 0 40px; border: none; padding: 0px;\"><blockquote style=\"margin: 0 0 0 40px; border: none; padding: 0px;\"><div><div><br></br></div><div>o R$: 0,00, Ipesp R$: 0,00, Sinoreg R$: 0,00, Trib.Justiça R$: 0,00, MP R$: 0,00, ISS R$: 0,00]</div></div><div>2323</div><div>23</div><div>23</div><div><br></br></div></blockquote></blockquote></html>",
                    _pdf.getFile().getParentFile() + File.separator + "PDF_HTML.pdf"
            );
            assertThat( arquivoDestino.exists() );

        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

//    //@Test
//    public void H_when_merge_all_pdf() {
//
//        try {
//            ArrayList<String> arquivos = new ArrayList<String>();
//            //Ja testado com 1000 arquivos de 3 paginas
//            for (int i = 1; i <= 10; i++) {
//                arquivos.add(_pdf.getFile().getParentFile() + File.separator + "PDF_Carimbado.pdf");
//            }
//
//            File arqRetorno =
//            pdfService.pdfMergeAllgs(
//                    arquivos,
//                    _pdf.getFile().getParentFile() + File.separator + "PDF_INTEIRO_TEOR.pdf"
//            );
//
//            assertThat( arqRetorno.exists() );
//
//        } catch(IOException e) {
//            fail(e.getMessage());
//        } catch (Exception e) {
//            fail(e.getMessage());
//        }
//
//
//    }

/*
    //@Test
    public void H_when_merge_pdf() {

        try {
            ArrayList<String> arquivos = new ArrayList<String>();
            for (int i = 1; i <= 3; i++) {
                arquivos.add(_pdf.getFile().getParentFile() + File.separator + "PDF_Carimbado.pdf");
            }

            File arqRetorno =
                    pdfService.pdfMerge(
                            _pdf.getFile().getParentFile() + File.separator + "PDF_Carimbado.pdf",
                            _pdf.getFile().getParentFile() + File.separator + "PDF_Carimbado.pdf",
                            _pdf.getFile().getParentFile() + File.separator + "PDF_Merge_Unico.pdf"
                    );

            assertThat( arqRetorno.exists() );

        } catch(IOException e) {
            fail(e.getMessage());
        } catch (Exception e) {
            fail(e.getMessage());
        }


    }
*/

//
//            @Test
//            public void A_when_carimbar_producao() throws IOException {
//                //byte[] aOrig = Files.readAllBytes(new File(this.getClass().getClassLoader().getResource( "util" + File.separator + "PDF" + File.separator + "pdf.pdf").getPath()).toPath());
//                String protocolo = "327891";
//
//                try {
//                    File arqOrigConv = pdfService.converte("/Users/Heryk/Desktop/Lumera/Ruy/" + protocolo + ".PDF", "/Users/Heryk/Desktop/Lumera/Ruy/" + protocolo + "_C.pdf");
//                    //byte[] aOrig = Files.readAllBytes(arqOrigConv.toPath());
//
//
//                    PdfHash retornoA = pdfService.carimbar(arqOrigConv,
//                            "CERTIDÃO DE REGISTRO: Certifico que a presente certidão é constituída de 15 páginas e foi extraída do documento registrado sob número 327.891 em 09/03/2018 neste 2° Oficial de Registro de Títulos e Documentos e Civil de Pessoa Jurídica de Osasco. Certifico, ainda, que a presente certidão possui o mesmo valor probante do documento original para todos os fins de Direito, seja em Juízo ou fora dele, nos termos dos artigos 161 da Lei n. 6.015/73 e 217 da Lei 10.406/02, tendo sido extraída sob forma de documento eletrônico. Certifico, ainda, que a assinatura digital constante neste documento eletrônico é do 2° Oficial de Registro de Títulos e Documentos e Civil de Pessoa Jurídica de Osasco e está em conformidade com os padrões da ICP-Brasil, nos termos da Medida Provisória nº. 2.200-2, de 24 de agosto de 2001. Osasco, 12 de abril de 2018. 2° Oficial de Registro de Títulos e Documentos e Civil de Pessoa Jurídica de Osasco, CNPJ 51.241.396/0001-08.",
//                            "Certidão eletrônica, com valor de original, do documento registrado sob o número 327.891 em 09/03/2018, assinada digitalmente pelo 2º Oficial de Registro de Títulos e Documentos e Civil de Pessoa Jurídica de Osasco",
//                            //null,
//                            "Para verificar o documento registrado acesse o site www.2osasco.com.br e digite: Conta: compapel Login: compapel e Senha: cpp123 e em Pesquisa de documento digite o número 327891.",
//                            "www.2osasco.com.br",
//                            "",
//                            Disposicao.DIREITA_INFERIOR,
//                            PDFService.DisposicaoPagina.ULTIMA_PAGINA);
//                    byte[] retorno = retornoA.getOrig();
//
//                    FileOutputStream aDest = new FileOutputStream( "/Users/Heryk/Desktop/Lumera/Ruy/PDF_"+protocolo+"_B.pdf");
//                    aDest.write(retorno);
//                    aDest.close();
//                    assertThat(true).isTrue();
//
//                } catch (Exception e) {
//                    fail(e.getMessage());
//                }
//            }
//
//




}
