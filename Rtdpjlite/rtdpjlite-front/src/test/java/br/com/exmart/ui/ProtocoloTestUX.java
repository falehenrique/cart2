package br.com.exmart.ui;

import br.com.exmart.ui.tools.DataRand;
import com.vaadin.testbench.elements.*;
import org.junit.*;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.awt.*;
import java.awt.datatransfer.StringSelection;

/**
 * Created by Heryk on 03/07/2018.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProtocoloTestUX extends TestBase {

    //@FindBy(id = "Adicionar")
    //private ButtonElement btnAdicionar;
    private DataRand dataRand = new DataRand();
    private int numProtocolo;
    private String[][] naturezaSubnaturezaRegistro =
            {

                    {"CONTRATO DE GARANTIA","ALIENAÇÃO FIDUCIÁRIA"},
                    {"CONTRATO DE GARANTIA","CESSÃO DE DIREITOS"},
                    {"CONTRATO DE GARANTIA","CONTRATO - OUTROS"},
                    {"CÉDULA DE CRÉDITO","CÉDULA DE CRÉDITO À EXPORTAÇÃO"},
                    {"CÉDULA DE CRÉDITO","CÉDULA DE CRÉDITO BANCÁRIO"},
                    {"CÉDULA DE CRÉDITO","CÉDULA DE CRÉDITO COMERCIAL"},
                    {"CÉDULA DE CRÉDITO","CÉDULA DE CRÉDITO IMOBILIÁRIO"},
                    {"CÉDULA DE CRÉDITO","CÉDULA DE CRÉDITO INDUSTRIAL"},
                    {"CÉDULA DE CRÉDITO","CÉDULA DE CRÉDITO RURAL"},
                    {"CÉDULA DE CRÉDITO","CÉDULA DE CRÉDITO RURAL HIPOTECÁRIA"},
                    {"CÉDULA DE CRÉDITO","CÉDULA DE CRÉDITO RURAL HIPOTECÁRIA E PIGNORATÍCIA"},
                    {"CÉDULA DE CRÉDITO","CÉDULA DE CRÉDITO RURAL PIGNORATÍCIA"},
                    {"CÉDULA DE CRÉDITO","CÉDULA DE PRODUTO RURAL"},
                    {"OUTROS","ANUÊNCIA"},
                    {"OUTROS","ARRENDAMENTO MERCANTIL (LEASING)"},
                    {"OUTROS","ATA-REGISTRO"},
                    {"OUTROS","BALANÇO"},
                    {"OUTROS","CARTA"},
                    {"OUTROS","CERTIFICADO DIGITAL"},
                    {"OUTROS","CESSÃO DE DIREITOS EM GARANTIA"},
                    {"OUTROS","CNH"},
                    {"OUTROS","COMODATO"},
                    {"OUTROS","COMPRA E VENDA"},
                    {"OUTROS","COMPROMISSO DE CESSÃO DE DIREITOS"},
                    {"OUTROS","COMPROMISSO DE COMPRA E VENDA"},
                    {"OUTROS","COMPROMISSO DE PERMUTA"},
                    {"OUTROS","CONFISSÃO DE DÍVIDA"},
                    {"OUTROS","CONSÓRCIO"},
                    {"OUTROS","OUTROS"},
                    {"OUTROS","CONTRATO SOCIAL"},
                    {"OUTROS","CONTRATO-PADRÃO"},
                    {"OUTROS","CTPS"},
                    {"OUTROS","DECLARAÇÃO"},
                    {"OUTROS","DIN"},
                    {"OUTROS","DIREITO REAL DE USO"},
                    {"OUTROS","DOAÇÃO"},
                    {"OUTROS","DOCUMENTO ESTUDANTIL"},
                    {"OUTROS","DOCUMENTO NACIONAL EM IDIOMA ESTRANGEIRO"},
                    {"OUTROS","DUPLICATA"},
                    {"OUTROS","LAUDO/VISTORIA"},
                    {"OUTROS","LETRA DE CRÉDITO IMOBILIÁRIO"},
                    {"OUTROS","LIVRO"},
                    {"OUTROS","LOCAÇÃO"},
                    {"OUTROS","MICROFILME - AUTENTICAÇÃO"},
                    {"OUTROS","MÚTUO"},
                    {"OUTROS","NOTA DE CRÉDITO RURAL"},
                    {"OUTROS","NOTA FISCAL"},
                    {"OUTROS","NOTA PROMISSÓRIA"},
                    {"OUTROS","PARCERIA"},
                    {"OUTROS","PASSAPORTE"},
                    {"OUTROS","PENHOR"},
                    {"OUTROS","PERMUTA"},
                    {"OUTROS","POSSE"},
                    {"OUTROS","PRESTAÇÃO DE SERVIÇOS"},
                    {"OUTROS","RH"},
                    {"OUTROS","SEGURO"},
                    {"OUTROS","SOCIEDADE EM CONTA DE PARTICIPAÇÃO"},
                    {"OUTROS","TESTAMENTO"},
                    {"OUTROS","TRANSAÇÃO"},
                    {"OUTROS","USUFRUTO"},
                    {"NOTIFICAÇÃO","OUTROS"},
                    {"CÉDULA DE CRÉDITO","Outros"},
                    {"CONTÁBIL, FISCAL OU ADMINISTRATIVO","Outros"},
                    {"CONTRATO DE SERVIÇO PÚBLICO","Outros"},
                    {"DIREITO AUTORAL/INVENÇÃO","Outros"},
                    {"DOCUMENTO - OUTROS","Outros"},
                    {"DOCUMENTO DE IDENTIDADE/PESSOAL","Outros"},
                    {"MANDATO","DOCUMENTO - OUTROS"},
                    {"CONTRATO DE GARANTIA","FIANÇA/AVAL"},
                    {"CONTRATO DE GARANTIA","OUTRAS GARANTIAS"},
                    {"MANDATO","PROCURAÇÃO"},
                    {"MANDATO","SUBSTABELECIMENTO - REGISTRO"},
                    {"NOTIFICAÇÃO","OUTROS"},
                    {"DOCUMENTO OU TÍTULO DE CRÉDITO - OUTROS","Outros"},
                    {"MATRÍCULA DE ANIMAL DE ESTIMAÇÃO","Outros"},
                    {"MATRÍCULA DE CLUBE DE INVESTIMENTO","Outros"},
                    {"MATRÍCULA DE CONDOMÍNIO EDILÍCIO","Outros"},
                    {"MATRÍCULA DE FUNDO DE INVESTIMENTO","Outros"},
                    {"MATRÍCULA DE ITEM FERROVIÁRIO","Outros"},
                    {"MATRÍCULA DE MÁQUINA OU EQUIPAMENTO","Outros"},
                    {"MATRÍCULA DE PRECATÓRIO JUDICIAL","Outros"},
                    {"MATRÍCULA DE VEÍCULO","Outros"},
                    {"TRANSMISSÃO DA PROPRIEDADE OU DE DIREITOS","Outros"},
                    {"UNIÃO ESTÁVEL","Outros"},
                    {"FUNDO","Outros"},
                    {"INDISPONIBILIDADE","REGISTRO"},
                    {"INDISPONIBILIDADE","CANCELAMENTO"}
            };

    private String[][] naturezaSubnaturezaCertidao =
            {
                    {"BREVE RELATO", "Outros"},
                    {"CERTIDAO", "Outros"},
                    {"CERTIDAO ELETRÔNICA", "Outros"},
                    {"COPIA", "Outros"},
                    {"INFORMACAO PRESTADA", "Outros"},
                    {"INTEIRO TEOR", "Outros"},
                    {"BUSCA", "Outros"}
            };


    @Before
    public void Login() {
        String login;
        String senha;

        login = (ambiente.equals("prod")?"katiane":"email1@email.com");
        senha = "1234";

        //Login
        driver.findElement(By.id("login")).sendKeys(login);
        driver.findElement(By.id("password")).sendKeys(senha);
        driver.findElement(By.id("button-submit")).submit();
    }


    private void consultarProtocolo(String tipo, String protocolo) {
        //Clicar pesquisa geral
        $(ButtonElement.class).caption("").first().click();

        $(ComboBoxElement.class).first().selectByText("PROTOCOLOS");

        //Tipo de protocolo
        String opcaoProtocolo = "";
        if (tipo.equals("RTD")) {
            opcaoProtocolo = "PROTOCOLOS DE REGISTRO - TD";
        }
        else if (tipo.equals("ETD")) {
            opcaoProtocolo = "PROTOCOLOS DE EXAME E CÁLCULO - TD";
        }
        else if (tipo.equals("RTD")) {
            opcaoProtocolo = "PROTOCOLOS DE REGISTRO - PJ";
        }
        else if (tipo.equals("ETD")) {
            opcaoProtocolo = "PROTOCOLOS DE EXAME E CÁLCULO - PJ";
        }

        $(VerticalLayoutElement.class).$$(HorizontalLayoutElement.class).$$(ComboBoxElement.class).first().selectByText(opcaoProtocolo);

        if (protocolo != null) {
            //Número do protocolo
            $(TextFieldElement.class).first().setValue(protocolo);
        }

        //Clicar no primeiro item encontrado
        $(GridElement.class).first().getRow(0).click();
    }


    private int protocolar(String natureza, String subnatureza, String servico) {
        $(ComboBoxElement.class).caption("Natureza").first().selectByText(natureza); //CONTRATO DE GARANTIA
        $(ComboBoxElement.class).caption("Sub Natureza").first().selectByText(subnatureza); //OUTRAS GARANTIAS
        $(TextFieldElement.class).caption("Valor Documento").first().setValue("150000");
        $(TextFieldElement.class).id("nrDocParte").setValue(dataRand.getDocumento());
        $(TextFieldElement.class).caption("Parte").first().setValue(dataRand.getNome());
        $(ComboBoxElement.class).caption("Forma de entrega").first().selectByText("Digital");
        $(ComboBoxElement.class).caption("Cliente (Faturado)").first().selectByText("ITAÚ UNIBANCO SA");
        $(TextFieldElement.class).caption("Responsável").first().setValue(dataRand.getNome());
        $(TextFieldElement.class).caption("Identidade").first().setValue(dataRand.getIdentidade());
        $(TextFieldElement.class).caption("Telefone").first().setValue(dataRand.getTelefone());
        $(TextFieldElement.class).caption("Email").first().setValue(dataRand.getEmail());
        $(TextAreaElement.class).caption("Observações").first().setValue(dataRand.getLoremIpsum());
        //Selecionar Aba Serviço
        $(TabSheetElement.class).first().openTab("SERVIÇOS");
        //actionTools.selTab("SERVIÇOS");

        //Serviços
        //localhost
        //$(ComboBoxElement.class).caption("Serviço").first().selectByText("01- Reg. ou Av. c/ cont. financeiro");
        //2osasco
        $(ComboBoxElement.class).caption("Serviço").first().selectByText(servico); //"01- Reg. ou Av. c/ cont. financeiro"

        $(TextFieldElement.class).caption("Valor Base").first().setValue(dataRand.getValor());

        $(ButtonElement.class).caption("Adicionar").first().click();

        //Protocolar
        $(ButtonElement.class).caption("Salvar").first().click();
        System.out.println( $(PanelElement.class).$(LabelElement.class).get(2).getText() );

        int protocolo = 0;
        protocolo = Integer.parseInt( $(PanelElement.class).$(LabelElement.class).get(2).getText() );

        return protocolo;
    }


    private int protocolar_certidao(String protocolo, String natureza, String servico) {
        $(ComboBoxElement.class).caption("Natureza").first().selectByText(natureza);
        $(TextFieldElement.class).id("nrDocParte").setValue(dataRand.getDocumento());
        $(TextFieldElement.class).caption("Parte").first().setValue(dataRand.getNome());
        $(TextFieldElement.class).caption("Nº do Registro").first().setValue(protocolo);
        $(ComboBoxElement.class).caption("Forma de entrega").first().selectByText("Digital");
        //$(ButtonElement.class).caption("NÃO").first();
        $(ComboBoxElement.class).caption("Cliente (Faturado)").first().selectByText("ITAÚ UNIBANCO SA");
        $(TextFieldElement.class).caption("Responsável").first().setValue(dataRand.getNome());
        $(TextFieldElement.class).caption("Identidade").first().setValue(dataRand.getIdentidade());
        $(TextFieldElement.class).caption("Telefone").first().setValue(dataRand.getTelefone());
        $(TextFieldElement.class).caption("Email").first().setValue(dataRand.getEmail());
        $(TextAreaElement.class).caption("Observações").first().setValue(dataRand.getLoremIpsum());
        //Selecionar Aba Serviço
        $(TabSheetElement.class).first().openTab("SERVIÇOS");
        //actionTools.selTab("SERVIÇOS");

        $(ComboBoxElement.class).caption("Serviço").first().selectByText(servico);

        $(TextFieldElement.class).caption("Valor Base").first().setValue("0");

        $(ButtonElement.class).caption("Adicionar").first().click();

        //Protocolar
        $(ButtonElement.class).caption("Salvar").first().click();

        int protocoloCertidao = 0;
        protocoloCertidao = Integer.parseInt( $(PanelElement.class).$(LabelElement.class).get(2).getText() );

        return protocoloCertidao;

    }


    private void emitir_certidao() {
        //Editar
        $(ButtonElement.class).caption("Editar").first().click();

        //Selecionar Aba Título
        //actionTools.selTab("TÍTULO");
        $(TabSheetElement.class).first().openTab("TÍTULO");

//        WebElement menu = findElement(By.className("v-upload"));
//        menu.click();

        //Selecionar Aba Partes
        $(TabSheetElement.class).first().openTab("CERTIDÃO");
        //Finalizar Certidão
        $(ButtonElement.class).caption("FINALIZAR").first().click();

    }


    private void examinar() {
        //Editar
        $(ButtonElement.class).caption("Editar").first().click();

        //Selecionar Aba Título
        //actionTools.selTab("TÍTULO");
        $(TabSheetElement.class).first().openTab("TÍTULO");


/*
        UploadElement upload = $(UploadElement.class).first();
        WebElement submitButton = upload.findElement(By.className("v-upload"));
        submitButton.sendKeys("/Users/Heryk/Downloads/FluxoGeral.pdf");
        submitButton.click();
        //UploadElement menu =
                findElement(By.className("v-upload")).click();

*/



        //com.wcs.wcslib.vaadin.widget.multifileupload.component.CustomUploadElement comwcswcslibvaadinwidgetmultifileuploadcomponentCustomUpload1 = $(com.wcs.wcslib.vaadin.widget.multifileupload.component.CustomUploadElement.class).first();

        WebElement menu = findElement(By.className("v-upload")); //.cssSelector("input[type='file']"));
        menu.click();
        //driver.switchTo().frame(0);
        //menu.sendKeys("/Users/Heryk/Downloads/FluxoGeral.pdf");
        //menu.click();
        //driver.switchTo().defaultContent();


        StringSelection localArq = new StringSelection("/Users/Heryk/Downloads/FluxoGeral.pdf");
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(localArq, null);

        Robot robot = null;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }

        robot.delay(1000);
        robot.keyPress(java.awt.event.KeyEvent.VK_CONTROL);
        robot.keyPress(java.awt.event.KeyEvent.VK_V);
        robot.keyRelease(java.awt.event.KeyEvent.VK_V);
        robot.keyRelease(java.awt.event.KeyEvent.VK_CONTROL);
        robot.keyPress(java.awt.event.KeyEvent.VK_ENTER);
        robot.keyRelease(java.awt.event.KeyEvent.VK_ENTER);
        robot.delay(1000);


        //Selecionar Aba Partes
        $(TabSheetElement.class).first().openTab("PARTES");
        //Editar
        $(ButtonElement.class).caption("Editar").first().click();
        //Cadastra os dados da parte
        $(TextFieldElement.class).id("cpfCnpjParte").setValue(dataRand.getDocumento());
        $(TextFieldElement.class).caption("Nome").first().setValue(dataRand.getNome());
        $(ComboBoxElement.class).caption("Qualidade").first().selectByText("ANUENTE");
        $(ComboBoxElement.class).caption("Nacionalidade").first().selectByText("Brasileiro");
        $(ComboBoxElement.class).caption("Profissão").first().selectByText("ANALISTA");
        //Adicionar parte
        $(ButtonElement.class).caption("ADICIONAR PARTE").first().click();

        //Selecionar aba Análise
        $(TabSheetElement.class).first().openTab("ANÁLISE");

        $(TextAreaElement.class).caption("SITUAÇÃO ATUAL").first().setValue("Apto");
        $(ButtonElement.class).caption("Apto para registro").first().click();
        $(ButtonElement.class).caption("REGISTRAR").first().click();
        $(ButtonElement.class).caption("Fechar").first().click();

        $(VerticalSplitPanelElement.class).$(LabelElement.class).get(2);
        String oi = $(VerticalSplitPanelElement.class).$(LabelElement.class).first().getText();
        String oi3 = $(VerticalSplitPanelElement.class).$(LabelElement.class).first().getText();
        //Assert.assertTrue(lbProtocolo.getCaption().equals("3085"));
    }


    public int protocolarTodasNaturezasRegistro(String tipoProtocolo, String servico) {
        int erros = 0;
        numProtocolo = 0;
        for (String item[]: naturezaSubnaturezaRegistro) {
            getDriver().get(baseUrl);
            //Seleciona RegistroTD no menu principal
            $(ButtonElement.class).caption(tipoProtocolo).first().click();
            numProtocolo = protocolar(item[0], item[1], servico);
            System.out.println(item[0] + " - " + item[1]);
            if (numProtocolo == 0) {
                System.out.println("Erro ao protocolar: " + item[0] + " - " + item[1]);
                erros ++;
            } else {
                System.out.println("Protocolado: " + item[0] + " - " + item[1]);
            }
        }
        return erros;
    }


    public int protocolarTodasNaturezasCertidao(String tipoProtocolo, String registro) {
        int erros = 0;
        numProtocolo = 0;
        for (String item[]: naturezaSubnaturezaCertidao) {
            getDriver().get(baseUrl);
            System.out.println("Protocolando: " + item[0] + " - " + item[1]);
            //Seleciona RegistroTD no menu principal
            $(ButtonElement.class).caption(tipoProtocolo).first().click();
            numProtocolo = protocolar_certidao(registro, item[0], "08- Certidão - Registro sob forma eletrônica");
            System.out.println(item[0] + " - " + item[1]);
            if (numProtocolo == 0) {
                System.out.println("Erro ao protocolar: " + item[0] + " - " + item[1]);
                erros ++;
            } else {
                System.out.println("Protocolado: " + item[0] + " - " + item[1]);
            }
        }
        return erros;
    }


    //@Test
    public void AA_ProtocolarTodasNaturezasRegistroTD() {
        int erro = protocolarTodasNaturezasRegistro("REGISTRO TD", "01- Reg. ou Av. c/ cont. financeiro");
        Assert.assertTrue(erro <= 0);
    }

    //@Test
    public void AA_ProtocolarTodasNaturezasRegistroPJ() {
        int erro = protocolarTodasNaturezasRegistro("REGISTRO PJ", "06- Reg. PJ c/ ou s/ fins lucrativos");
        Assert.assertTrue(erro <= 0);
    }

    //@Test
    public void AA_ProtocolarTodasNaturezasCertidaoTD() {
        int erro = protocolarTodasNaturezasCertidao("CERTIDÃO TD", "335821");
        Assert.assertTrue(erro <= 0);
    }

    //@Test
    public void AA_ProtocolarTodasNaturezasCertidaoPJ() {
        int erro = protocolarTodasNaturezasCertidao("CERTIDÃO PJ", "180300");
        Assert.assertTrue(erro <= 0);
    }

    @Test
    public void A_ProtocolarTD() {
        //Seleciona RegistroTD no menu principal
        $(ButtonElement.class).caption("REGISTRO TD").first().click();
        numProtocolo = protocolar("CONTRATO DE GARANTIA", "OUTRAS GARANTIAS", "01- Reg. ou Av. c/ cont. financeiro");
        Assert.assertTrue(numProtocolo > 0);
    }


    @Test
    public void B_ProtocolarPJ() {
        //Seleciona RegistroTD no menu principal
        $(ButtonElement.class).caption("REGISTRO PJ").first().click();
        numProtocolo = protocolar("CONTRATO DE GARANTIA", "OUTRAS GARANTIAS", "06- Reg. PJ c/ ou s/ fins lucrativos");
        Assert.assertTrue(numProtocolo > 0);
    }


    @Test
    public void C_ProtocolarExameTD() {
        //Seleciona RegistroTD no menu principal
        $(ButtonElement.class).caption("EXAME E CÁLCULO TD").first().click();
        numProtocolo = protocolar("CONTRATO DE GARANTIA", "OUTRAS GARANTIAS", "01- Reg. ou Av. c/ cont. financeiro");
        Assert.assertTrue(numProtocolo > 0);
    }


    @Test
    public void D_ProtocolarExamePJ() {
        //Seleciona RegistroTD no menu principal
        $(ButtonElement.class).caption("EXAME E CÁLCULO PJ").first().click();
        numProtocolo = protocolar("CONTRATO DE GARANTIA", "OUTRAS GARANTIAS", "06- Reg. PJ c/ ou s/ fins lucrativos");
        Assert.assertTrue(numProtocolo > 0);
    }


    @Test
    public void E_ProtocolarCertidaoTD() {
        //Seleciona CertidaoTD no menu principal
        $(ButtonElement.class).caption("CERTIDÃO TD").first().click();
        protocolar_certidao("335821", naturezaSubnaturezaCertidao[0][0],"08- Certidão - Registro sob forma eletrônica");
    }


    @Test
    public void F_ProtocolarCertidaoPJ() {
        //Seleciona CertidaoPJ no menu principal
        $(ButtonElement.class).caption("CERTIDÃO PJ").first().click();
        protocolar_certidao("180300", naturezaSubnaturezaCertidao[0][0], "08- Certidão - Registro sob forma eletrônica");
    }









/*

    @Test
    public void G_ProtocolarEmitirCertidaoTD() {
        //Seleciona RegistroTD no menu principal
        $(ButtonElement.class).caption("CERTIDÃO TD").first().click();
        protocolar_certidao("335821", "08 - Certidão - Registro sob forma eletrônica");
        emitir_certidao();
    }


    @Test
    public void H_ProtocolocarExaminarRegistroTD() {
        $(ButtonElement.class).caption("REGISTRO TD").first().click();
        numProtocolo = protocolar("NOTIFICAÇÃO", "OUTROS", "01- Reg. ou Av. c/ cont. financeiro");
        //consultarProtocolo("RTD", Integer.toString(numProtocolo) );
        examinar();
        Assert.assertTrue(numProtocolo > 0);
    }


    @Test
    public void I_ProtocolarExaminarRegistroPJ() {
        $(ButtonElement.class).caption("REGISTRO PJ").first().click();
        numProtocolo = protocolar("CONTRATO DE GARANTIA", "OUTRAS GARANTIAS", "06- Reg. PJ c/ ou s/ fins lucrativos");
        //consultarProtocolo("RTD", Integer.toString(numProtocolo) );
        examinar();
        Assert.assertTrue(numProtocolo > 0);
    }

*/


    /*

    @Test
    public void H_Examinar_Registro_TD_Realizado_Portal() {
        $(TabSheetElement.class).first().openTab("BALCÃO ONLINE");
        $(ComboBoxElement.class).first().selectByText("PROTOCOLADO");
        String protocolo = $(GridElement.class).first().getCell(0, 7).getText();

        consultarProtocolo("RTD", protocolo);
        examinar();
    }


    @Test
    public void I_Gerar_Certidao_Registro_TD_Realizado_Portal() {
        $(TabSheetElement.class).first().openTab("BALCÃO ONLINE");
        $(ComboBoxElement.class).first().selectByText("REGISTRADO");
        String protocolo = $(GridElement.class).first().getCell(0, 7).getText();
        $(GridElement.class).first().getCell(0, 11).$(ButtonElement.class).first().click();
        //$(GridElement.class).first().getRow(0).$(ButtonElement.class).get(2).click();
        $(ButtonElement.class).caption("SIM").first();
        consultarProtocolo("RTD", "331414");
        examinar();
    }




*/










    //***
    //TENTATIVAS DE SELECAO AUTOMATICA DE ARQUIVO
    //***
    //
    //     TestBenchElement btUpload = $(TestBenchElement.class).caption("Anexar Documento").first();
    //     $(UploadElement.class).first().findElement(By.className("CustomUpload")).sendKeys("/tmp/test.xlsx");
    //$(UploadElement.class).first().findElement(By.className("v-button")).click();
    //     CustomComponentElement upload = $(com.wcs.wcslib.vaadin.widget.multifileupload.component.CustomUpload.class).first();


    //WebElement upArquivo = findElement(By.className("v-upload"));
    //System.out.println(upArquivo.getText());
    //upArquivo.click();
    //upArquivo.submit();
    //upArquivo.sendKeys("/Users");


    //findElement(By.id("btUpload")).sendKeys("/Users/Heryk/Downloads/TESTE.PDF");

    //        $(UploadElement.class).first().findElement(By.className("v-upload")).sendKeys("/Users/Heryk/Downloads/TESTE.pdf");

    //***Referencias
    //https://muthutechno.wordpress.com/2014/07/09/uploading-files-in-selenium-webdriver/

/*        LocalFileDetector detector = new LocalFileDetector();
        File file = new File("/Users/Heryk/Downloads/TESTE.PDF");
        file.toURI();

        WebElement menu = findElement(By.className("v-upload"));
        RemoteWebElement element1=(RemoteWebElement) ((RemoteWebDriver)driver).findElement(By.className("/Users/Heryk/Downloads/TESTE.PDF"));
        element1.click();
        ( (RemoteWebElement) element1) .setFileDetector( detector );
        element1.sendKeys(file.getAbsolutePath());


        LocalFileDetector detector = new LocalFileDetector();
        File file = new File("/Users/Heryk/Downloads/TESTE.PDF");
        file.toURI();
*/


    //findElement(By.className("v-upload")).sendKeys("TESTE.PDF");
    //menu.sendKeys("TESTE.PDF");


    //        ((RemoteWebElement) menu ).setFileDetector(new LocalFileDetector());

//        Actions actions = new Actions(driver);
//        actions.moveToElement(menu);
//        actions.click();
//        actions.sendKeys("/Users/Heryk/Downloads/TESTE.PDF");
//        actions.build().perform();

    //UploadElement eu = (UploadElement) menu;
    //eu.sendKeys("/Users/Heryk/Downloads/TESTE.PDF");
    //menu.sendKeys("/Users/Heryk/Downloads/TESTE.PDF");
//        menu.click();
    //menu.sendKeys("/Users/Heryk/Downloads/TESTE.pdf");



}

