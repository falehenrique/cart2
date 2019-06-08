package br.com.exmart.indicadorRTDPJ.ui.impl;

import br.com.exmart.indicadorRTDPJ.ui.ModalRetroativo;
import br.com.exmart.indicadorRTDPJ.ui.custom.MyStringToDateTimeConverter;
import br.com.exmart.indicadorRTDPJ.ui.custom.event.ProtocoloEvent;
import br.com.exmart.rtdpjlite.model.*;
import br.com.exmart.rtdpjlite.service.*;
import br.com.exmart.rtdpjlite.service.rest.fonetica.FoneticaService;
import br.com.exmart.util.BeanLocator;
import com.google.common.base.Strings;
import com.google.common.io.ByteStreams;
import com.vaadin.data.*;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.converter.StringToLongConverter;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Window;
import com.wcs.wcslib.vaadin.widget.multifileupload.ui.MultiFileUpload;
import com.wcs.wcslib.vaadin.widget.multifileupload.ui.UploadFinishedHandler;
import com.wcs.wcslib.vaadin.widget.multifileupload.ui.UploadStateWindow;
import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.MessageBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.vaadin.spring.events.EventBus;
import org.vaadin.textfieldformatter.CustomStringBlockFormatter;
import pl.pdfviewer.PdfViewer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class ModalRetroativoImpl extends ModalRetroativo{


    private RegistroService registroService;
    private Registro registro = new Registro();
    private BeanValidationBinder<Registro> binderRegistro = new BeanValidationBinder<>(Registro.class);
    private BeanValidationBinder<IndicadorPessoal> binderIndicadorPessoal= new BeanValidationBinder<>(IndicadorPessoal.class);
    private NaturezaService naturezaService;
    private SubNaturezaService subNaturezaService;
    private MultiFileUpload singleUpload;
    private Window window;
    private ArquivoService arquivoService;
    private File arquivoTemporario = null;
    private EventBus.ViewEventBus eventBus;
    private IndicadorPessoal indicadorSelecionado = null;

    public ModalRetroativoImpl(Registro registro, EventBus.ViewEventBus eventBus) {
        this.registro = registro;
        init(eventBus);
    }

    private void init(EventBus.ViewEventBus eventBus){
        this.registroService = BeanLocator.find(RegistroService.class);
        this.naturezaService = BeanLocator.find(NaturezaService.class);
        this.subNaturezaService = BeanLocator.find(SubNaturezaService.class);
        this.arquivoService = BeanLocator.find(ArquivoService.class);

        qualidade.setItems(BeanLocator.find(QualidadeService.class).findAll());
        nacionalidade.setItems(BeanLocator.find(NacionalidadeService.class).findAll());
        estadoCivil.setItems(BeanLocator.find(EstadoCivilService.class).findAll());
        profissao.setItems(BeanLocator.find(ProfissaoService.class).findAll());
        estado.setItems(BeanLocator.find(EstadoService.class).findAll());
        regimeBens.setItems(BeanLocator.find(RegimeBensService.class).findAll());
        tipoDocumento.setItems(BeanLocator.find(TipoDocumentoService.class).findAll());

        estado.addValueChangeListener(evt-> {
            if(evt != null)
                cidade.setItems(evt.getValue().getCidades());

        });

        this.eventBus = eventBus;

        natureza.setItemCaptionGenerator(Natureza::getNome);
        natureza.setItems(naturezaService.findAll());
        subnatureza.setItems(subNaturezaService.findAll());
        subnatureza.setItemCaptionGenerator(SubNatureza::getNome);


        natureza.addValueChangeListener(evt->naturezaListener(evt));

        new CustomStringBlockFormatter(dataReg, new int[]{2,2,4}, new String[]{"/","/"}, CustomStringBlockFormatter.ForceCase.NONE);


        binderRegistro.bind(natureza, "natureza");
        binderRegistro.forField(subnatureza).asRequired("Favor selecionar a Sub Natureza").bind("subNatureza");
        binderRegistro.bind(regAnterior,"registroReferencia");
        binderRegistro.forField(numReg).withNullRepresentation("").withConverter(new StringToLongConverter(null,"Deve conter apenas números")).bind("numeroRegistro");
        binderRegistro.bind(especialidade,"especialidade");
        binderRegistro.forField(dataReg).withConverter(new MyStringToDateTimeConverter()).bind("dataRegistro");

        binderRegistro.setBean(this.registro);

        regAnterior.addBlurListener(evt->regAnteriorListener());

        btnSalvar.addClickListener(evt->btnSalvarListener());
        btnSalvarParte.addClickListener(evt->btnSalvarIndicadorPessoalListener());
        btnNovaParte.addClickListener(evt->{
            this.indicadorSelecionado = new IndicadorPessoal();
            this.binderIndicadorPessoal.readBean(this.indicadorSelecionado);
            panelPartesForm.setEnabled(true);
        });


        UploadFinishedHandler handler = new UploadFinishedHandler() {
            @Override
            public void handleFile(InputStream inputStream, String s, String s1, long l, int i) {
                try {

//					Notification.show("Documento enviado com sucesso");


                    mostrarArquivoPdf(arquivoService.criarArquivoTemporario(Long.toString(Calendar.getInstance().getTimeInMillis()), ByteStreams.toByteArray(inputStream)));
                    inputStream.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        UploadStateWindow uploadWindow = new UploadStateWindow();

        singleUpload = new MultiFileUpload(handler, uploadWindow, false);

        singleUpload.setAcceptedMimeTypes(Arrays.asList("application/pdf"));
        singleUpload.getSmartUpload().setUploadButtonCaptions("Alterar documento", "");
        singleUpload.setMimeTypeErrorMsgPattern("Os documentos devem ser enviados somente com a extensão .pdf");
        panelBotaoUpload.addComponent(singleUpload);
        panelBotaoUpload.setExpandRatio(singleUpload, 1);
        panelBotaoUpload.setComponentAlignment(singleUpload, Alignment.TOP_LEFT);

        especialidade.focus();
        btnCancelar.addClickListener(evt->btnCancelarClickListener());

        if(!Strings.isNullOrEmpty(registro.getEspecialidade())){
            File arquivo = arquivoService.recuperarDocumentoProtocoloRegistroFile(registro.getRegistro(), registro.getNumeroRegistro(), registro.getEspecialidade(), TipoProtocolo.recuperaTipoPrenotacaoByEspecialidade(registro.getEspecialidade()));
            if(arquivo != null){
                mostrarArquivoPdf(arquivo);
            }
        }
        if(registro.getIndicadorPessoal() != null)
            this.gridIndicadorPessoal.setItems(registro.getIndicadorPessoal());

        this.gridIndicadorPessoal.addItemClickListener(item -> {
            try {
                this.indicadorSelecionado= item.getItem();
                binderIndicadorPessoal.readBean(this.indicadorSelecionado);

            }catch (NullPointerException e){
            }
            panelPartesForm.setEnabled(true);
        });

        binderIndicadorPessoal.bind(nome,"nome");
        binderIndicadorPessoal.bind(documento,"cpfCnpj");
        binderIndicadorPessoal.bind(rg,"nrDocumento");
        binderIndicadorPessoal.bind(telefone,"telefone");
        binderIndicadorPessoal.bind(qualidade,"qualidade");
        binderIndicadorPessoal.bind(email,"email");
        binderIndicadorPessoal.bind(nacionalidade,"nacionalidade");
        binderIndicadorPessoal.bind(estadoCivil,"estadoCivil");
        binderIndicadorPessoal.bind(profissao,"profissao");

        binderIndicadorPessoal.bind(orgaoExpedidor,"nmOrgaoExpedidorDocumento");

        binderIndicadorPessoal.bind(orgaoExpedidor,"nmOrgaoExpedidorDocumento");
        binderIndicadorPessoal.bind(regimeBens,"regimeBens");
        binderIndicadorPessoal.bind(tipoDocumento,"tipoDocumento");

//        binderIndicadorPessoal.bind(vlParticipacao,"vlParticipacao");

        binderIndicadorPessoal.forField(cotas).withConverter(new StringToIntegerConverter(null, "Não pode ser nulo")).bind("qtdCotas");


        binderIndicadorPessoal.bind(cep,"cep");
        binderIndicadorPessoal.bind(endereco,"endereco");
        binderIndicadorPessoal.bind(nrEndereco,"numero");
        binderIndicadorPessoal.bind(complemento,"complemento");
        binderIndicadorPessoal.bind(bairro,"bairro");
        binderIndicadorPessoal.bind(cidade,"cidade");
        binderIndicadorPessoal.bind(estado,"estado");

        arrumarGridPartes();
    }
    private void arrumarGridPartes(){
        this.gridIndicadorPessoal.removeAllColumns();
        this.gridIndicadorPessoal.addColumn("nome").setCaption("Nome");
        this.gridIndicadorPessoal.addColumn("cpfCnpj").setCaption("Documento");
        this.gridIndicadorPessoal.addColumn("tipoDocumento").setCaption("Tipo Doc.");
        this.gridIndicadorPessoal.addColumn("nrDocumento").setCaption("Identidade");
        this.gridIndicadorPessoal.addColumn("telefone").setCaption("Telefone");
        this.gridIndicadorPessoal.addColumn("qualidade").setCaption("Qualidade");
        this.gridIndicadorPessoal.addColumn("email").setCaption("Email");
        this.gridIndicadorPessoal.addColumn("nacionalidade").setCaption("Nacionalidade");
        this.gridIndicadorPessoal.addColumn("estadoCivil").setCaption("Estado Civil");
        this.gridIndicadorPessoal.addColumn("regimeBens").setCaption("Regime Bens");
        this.gridIndicadorPessoal.addColumn("profissao").setCaption("Profissao");
        this.gridIndicadorPessoal.addColumn("qtdCotas").setCaption("Cotas");
        this.gridIndicadorPessoal.addColumn("cep").setCaption("Cep");
        this.gridIndicadorPessoal.addColumn("endereco").setCaption("Endereco");
        this.gridIndicadorPessoal.addColumn("numero").setCaption("Numero");
        this.gridIndicadorPessoal.addColumn("complemento").setCaption("Compl.");
        this.gridIndicadorPessoal.addColumn("bairro").setCaption("Bairro");
        this.gridIndicadorPessoal.addColumn("cidade").setCaption("Cidade");
        this.gridIndicadorPessoal.addColumn("estado").setCaption("Estado");
    }


    private void btnCancelarClickListener() {
        MessageBox
                .createQuestion()
                .withCaption("Atenção")
                .withMessage("Deseja sair do cadastro retroativo? As alterações que não foram salvas serão perdidas.")
                .withYesButton(() -> {
                    this.window.close();
                }, ButtonOption.caption("SIM"), ButtonOption.focus())
                .withNoButton(ButtonOption.caption("NÃO"))
                .open();
    }

    private void mostrarArquivoPdf(File arquivo) {
        this.arquivoTemporario = arquivo;
        panelMostrarTitulo.removeAllComponents();
        PdfViewer pdf = new PdfViewer();
        pdf.setPreviousPageCaption("ANTERIOR");
        pdf.setNextPageCaption("PRÓXIMO");
        pdf.setPageCaption("PÁGINA ");
        pdf.setDownloadBtnVisible(true);
        pdf.setToPageCaption("DE ");
        pdf.setStyleName("pdfRTDPJ");
        pdf.setFile(arquivo);
        panelMostrarTitulo.addComponentsAndExpand(pdf);
    }

    private void btnSalvarIndicadorPessoalListener(){
        BinderValidationStatus<IndicadorPessoal> validacao = this.binderIndicadorPessoal.validate();
        if(validacao.hasErrors()){
            Notification.show("Atencao", "Ops ;) Acho que faltou preencher algum campo corretamente", Notification.Type.ERROR_MESSAGE);
        }else{
            try {
                this.binderIndicadorPessoal.writeBean(this.indicadorSelecionado);
                this.indicadorSelecionado.setNomeFonetico(BeanLocator.find(FoneticaService.class).foneticar(this.indicadorSelecionado.getNome()));
                if(this.indicadorSelecionado.getId() == null)
                    this.registro.getIndicadorPessoal().add(this.indicadorSelecionado);
                this.gridIndicadorPessoal.getDataProvider().refreshAll();
                panelPartesForm.setEnabled(false);

            } catch (ValidationException e) {
                e.printStackTrace();
            }catch (HttpClientErrorException ht){
                ht.printStackTrace();
                Notification.show("Fonética", "Erro: " + ht.getMessage(), Notification.Type.ERROR_MESSAGE);
            }catch (HttpServerErrorException ht ){
                ht.printStackTrace();
                Notification.show("Fonética", "Erro: " + ht.getMessage(), Notification.Type.ERROR_MESSAGE);
            }
        }

    }
    private void btnSalvarListener() {

        BinderValidationStatus<Registro> validacao = this.binderRegistro.validate();
        if(validacao.hasErrors()){
            for(ValidationResult validacaoError : validacao.getValidationErrors()){
                System.out.println(validacaoError.getErrorMessage());
            }
            Notification.show("Atenção", "Ops ;) Acho que faltou preencher algum campo corretamente", Notification.Type.ERROR_MESSAGE);
        }else{
            MessageBox
                .createQuestion()
                .withCaption("Atenção")
                .withMessage("Deseja salvar as alterações neste registro retroativo?")
                .withYesButton(() -> {
                    salvarRetroativo();
                }, ButtonOption.caption("SIM"), ButtonOption.focus())
                .withNoButton(ButtonOption.caption("NÃO"))
                .open();
        }
    }

    private void salvarRetroativo() {
        registro.setRegistro(registroService.gerarRegistro(this.registro.getNumeroRegistro(), this.registro.getDataRegistro()));
        registroService.save(binderRegistro.getBean());
        if(arquivoTemporario != null){
            arquivoService.salvarArquivoRetroativo(binderRegistro.getBean().getRegistro(), this.arquivoTemporario, binderRegistro.getBean().getEspecialidade(), binderRegistro.getBean().getNumeroRegistro());
        }
        if(this.eventBus != null) {
            this.eventBus.publish(this, new ProtocoloEvent.RetroativoCadastrado(this.registro));
        }
        window.close();
    }

    private void naturezaListener(HasValue.ValueChangeEvent<Natureza> evt) {
        if(evt != null){
            subnatureza.setItems(evt.getValue().getSubNaturezas());
        }
    }

    private void regAnteriorListener() {
        if(!Strings.isNullOrEmpty(regAnterior.getValue()) & !Strings.isNullOrEmpty(especialidade.getValue())){
            List<Registro> protocoloReferenciaList = registroService.findByNumeroRegistroAndEspecialidade(new Long(regAnterior.getValue()), especialidade.getValue());
            if (protocoloReferenciaList.size() == 1) {
                Registro protocoloReferencia = protocoloReferenciaList.get(0);
                this.registro.setRegistroReferencia(protocoloReferencia.getRegistro());
            } else {
                Notification.show("Atenção", "Encontrado mais de um registro para o numero informado", Notification.Type.ERROR_MESSAGE);
            }
        }
    }

    public ModalRetroativoImpl(Long registroRetroativo, String especialidadeRetroativo, EventBus.ViewEventBus eventBus) {
        this.registroService = BeanLocator.find(RegistroService.class);
        List<Registro> retroativo = registroService.findByNumeroRegistroAndEspecialidade(registroRetroativo, especialidadeRetroativo);
        if(retroativo.size() > 0){
            this.registro = retroativo.get(0);
            init(eventBus);
        }else{
            init(eventBus);
            numReg.setValue(registroRetroativo.toString());
            especialidade.setValue(especialidadeRetroativo);
        }
            especialidade.setEnabled(Strings.isNullOrEmpty(especialidadeRetroativo));
        numReg.setEnabled(registroRetroativo == null);
        dataReg.focus();
    }
}
