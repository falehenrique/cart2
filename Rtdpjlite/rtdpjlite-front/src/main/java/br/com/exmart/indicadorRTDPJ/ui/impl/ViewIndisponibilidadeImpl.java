package br.com.exmart.indicadorRTDPJ.ui.impl;

import br.com.exmart.indicadorRTDPJ.MyUI;
import br.com.exmart.indicadorRTDPJ.ui.ViewIndisponibilidade;
import br.com.exmart.indicadorRTDPJ.ui.util.GerenciarJanela;
import br.com.exmart.indicadorRTDPJ.ui.util.Utils;
import br.com.exmart.rtdpjlite.model.*;
import br.com.exmart.rtdpjlite.service.*;
import br.com.exmart.rtdpjlite.vo.indisponibilidade.*;
import br.com.exmart.util.BeanLocator;
import com.github.appreciated.app.layout.annotations.MenuCaption;
import com.github.appreciated.app.layout.annotations.MenuIcon;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.wcs.wcslib.vaadin.widget.multifileupload.ui.MultiFileUpload;
import com.wcs.wcslib.vaadin.widget.multifileupload.ui.UploadFinishedHandler;
import com.wcs.wcslib.vaadin.widget.multifileupload.ui.UploadStateWindow;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@SpringView(name = "indisponibilidade")
@UIScope
@MenuCaption("INDISPONIBILIDADE")
@MenuIcon(VaadinIcons.GROUP)
public class ViewIndisponibilidadeImpl extends ViewIndisponibilidade implements View{
    protected static Logger logger = LoggerFactory.getLogger(ViewIndisponibilidadeImpl.class);
    @Value("${cdCartorioNaturezaPj}")
    private Integer cdCartorioNaturezaPj;
    private FormaEntrega formaEntrega;
    @Autowired
    private IndisponibilidadeService indisponibilidadeService;
    @Autowired
    private RegistroService registroService;
    @Autowired
    private NaturezaService naturezaService;
    @Autowired
    private ProtocoloService protocoloService;
    @Autowired
    private FormaEntregaService formaEntregaService;
    @Autowired
    private QualidadeService qualidadeService;
    private ParteIndisponivel parteSelecionado;
    private String tipoIndisponibilidade = "";
    private RetornoIndisponibilidade retorno = null;
    List<ParteIndisponivel> encontrados = new ArrayList<>();
    List<ParteIndisponivel> encontradosSemDocumento = new ArrayList<>();


    private MultiFileUpload singleUpload;
    private File arquivoImportar = null;
    private Window window;
    private File arquivo;


    public ViewIndisponibilidadeImpl() {

        arrumarGridIndisponibilidade();
        gridEncontrados.setSelectionMode(Grid.SelectionMode.SINGLE);
        gridRegistros.setSelectionMode(Grid.SelectionMode.SINGLE);
        gridValidar.setSelectionMode(Grid.SelectionMode.SINGLE);
        tabSheet.addSelectedTabChangeListener(evt -> tabSheetChangelistener(evt));
    }

    private void tabSheetChangelistener(TabSheet.SelectedTabChangeEvent evt) {
        gridRegistros.getColumns().get(gridRegistros.getColumns().size()-1).setHidden(!"gridValidar".equalsIgnoreCase(evt.getTabSheet().getSelectedTab().getId()));
        listarRegistros(null);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        this.verificarIndisponibilidade();
        gridRegistros.getColumns().get(gridRegistros.getColumns().size()-1).setHidden(!"gridValidar".equalsIgnoreCase(tabSheet.getSelectedTab().getId()));
    }
    public void verificarIndisponibilidade() {
        this.encontrados.clear();
        this.encontradosSemDocumento.clear();
        this.formaEntrega = formaEntregaService.findByNome("Digital");
        try {

            retorno = indisponibilidadeService.verificarIndisponibilidade();


            encontrados.addAll(retorno.getPartesEncontradasIndisponibilidade());
            encontrados.addAll(retorno.getPartesEncontradasCancelamento());

            gridEncontrados.setItems(encontrados);

            encontradosSemDocumento.addAll(retorno.getPartesEncontradasIndisponibilidadeSemDocumento());
            encontradosSemDocumento.addAll(retorno.getPartesEncontradasCancelamentoSemDocumento());
            gridValidar.setItems(encontradosSemDocumento);
            gridNaoEncontrados.setItems(retorno.getPartesNaoEncontradas());

            gridEncontrados.addItemClickListener(evt -> listarRegistros(evt.getItem()));
            gridValidar.addItemClickListener(evt -> listarRegistros(evt.getItem()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(encontrados.size() <= 0 && encontradosSemDocumento.size() <= 0){
            Notification.show("Indisponibilidade", "Nenhuma indisponibilidade encontrada", Notification.Type.WARNING_MESSAGE);
        }
    }


    private void arrumarGridIndisponibilidade(){
        gridEncontrados.removeAllColumns();
        gridEncontrados.addColumn(ParteIndisponivel::getTipo).setCaption("Tipo");
        gridEncontrados.addColumn(ParteIndisponivel::getNome).setCaption("Nome");
        gridEncontrados.addColumn(ParteIndisponivel::getDocumento).setCaption("Documento");
        gridEncontrados.addColumn(item -> {return Utils.formatarDataComHora(item.getDataPedido());}, new HtmlRenderer()).setCaption("Data") .setMinimumWidthFromContent(true);
        gridEncontrados.addColumn(ParteIndisponivel::getNumeroProcesso).setCaption("Nrº Processo");

        gridValidar.removeAllColumns();
        gridValidar.addColumn(ParteIndisponivel::getTipo).setCaption("Tipo");
        gridValidar.addColumn(ParteIndisponivel::getNome).setCaption("Nome");
        gridValidar.addColumn(ParteIndisponivel::getDocumento).setCaption("Documento");
        gridValidar.addColumn(item -> {return Utils.formatarDataComHora(item.getDataPedido());}, new HtmlRenderer()).setCaption("Data") .setMinimumWidthFromContent(true);
        gridValidar.addColumn(ParteIndisponivel::getNumeroProcesso).setCaption("Nrº Processo");


        gridNaoEncontrados.removeAllColumns();
        gridNaoEncontrados.addColumn(ParteIndisponivel::getTipo).setCaption("Tipo");
        gridNaoEncontrados.addColumn(ParteIndisponivel::getNome).setCaption("Nome");
        gridNaoEncontrados.addColumn(ParteIndisponivel::getDocumento).setCaption("Documento");
        gridNaoEncontrados.addColumn(item -> {return Utils.formatarDataComHora(item.getDataPedido());}, new HtmlRenderer()).setCaption("Data") .setMinimumWidthFromContent(true);
        gridNaoEncontrados.addColumn(ParteIndisponivel::getNumeroProcesso).setCaption("Nrº Processo");

        gridRegistros.removeAllColumns();
        gridRegistros.addColumn(ParteIndisponivelRegistro::getNumeroRegistro).setCaption("Registro");
        gridRegistros.addColumn(ParteIndisponivelRegistro::getNrPastaPj).setCaption("Nrº Pasta");
        gridRegistros.addComponentColumn(item -> {
            Button button = new Button(VaadinIcons.INFO);
            button.addStyleName("borderless");
            button.addClickListener(click ->
            {
                Registro registroSelecionado = registroService.findById(item.getId());

                GerenciarJanela.abrirJanela("", 100, 100, new ViewRegistroImpl(registroSelecionado));
            });
            return button;
        }).setCaption("Visualizar Registro");
        gridRegistros.addComponentColumn(item -> {
            Button button = new Button(VaadinIcons.FILE_ADD);
            button.addStyleName("borderless");
            button.addClickListener(click ->
            {
                Natureza natureza = naturezaService.findByNome("INDISPONIBILIDADE");

                if(parteSelecionado.getTipo().equalsIgnoreCase("INDISPONIVEL")){
                    tipoIndisponibilidade = "REGISTRO";
                }else{
                    tipoIndisponibilidade = "CANCELAMENTO";
                }

                SubNatureza subNatureza = natureza.getSubNaturezas().stream().filter(sub -> {return sub.getNome().equalsIgnoreCase(tipoIndisponibilidade);}).collect(Collectors.toList()).get(0);

                Registro registroSelecionado = registroService.findById(item.getId());
                Protocolo novoProtocolo = new Protocolo();
                novoProtocolo.setTipo(TipoProtocolo.PRENOTACAO_PJ);
                novoProtocolo.setNatureza(natureza);
                novoProtocolo.setSubNatureza(subNatureza);
                novoProtocolo.setProtocoloIndisponibilidade(this.parteSelecionado.getProtocoloIndisponibilidade());
                novoProtocolo.setProtocoloCancelamentoIndisponibilidade(this.parteSelecionado.getProtocoloCancelamentoIndisponibilidade());
                novoProtocolo.setApresentante(this.parteSelecionado.getNomeInstituicao());
                novoProtocolo.setEmail(this.parteSelecionado.getEmail());
                novoProtocolo.setData_doc(LocalDate.now());
                novoProtocolo.setPastaPJ(registroSelecionado.getNrPastaPj());
                novoProtocolo.setParteDocumento(this.parteSelecionado.getDocumento());
                novoProtocolo.setParte(this.parteSelecionado.getDocumento());
                novoProtocolo.setFormaEntrega(this.formaEntrega);
                novoProtocolo.setResponsavel(this.parteSelecionado.getUsuario());
                novoProtocolo.setVias(1);
                novoProtocolo.setRegistroReferencia(registroSelecionado.getRegistro());
                novoProtocolo.setNumeroRegistroReferencia(registroSelecionado.getNumeroRegistro());

                IndicadorPessoal indicador = null;
                if("gridEncontrados".equalsIgnoreCase(this.tabSheet.getSelectedTab().getId())){
                    indicador = registroSelecionado.getIndicadorPessoal().stream().filter(indicadorPessoal -> {
                        return indicadorPessoal.getCpfCnpj().equalsIgnoreCase(this.parteSelecionado.getDocumento());
                    }).collect(Collectors.toList()).get(0);
                }else{
                    indicador = registroSelecionado.getIndicadorPessoal().stream().filter(indicadorPessoal -> {
                        return indicadorPessoal.getNomeFonetico().equalsIgnoreCase(this.parteSelecionado.getNomeFonetico());
                    }).collect(Collectors.toList()).get(0);
                    indicador.setCpfCnpj(this.parteSelecionado.getDocumento());
                }

                Qualidade qualidade = qualidadeService.findByNome("SÓCIO");



                novoProtocolo.getPartesProtocolo().add(ParteProtocolo.fromIndicadorPessoal(indicador.toParte() ,qualidade,new Double("0"),0));

                try {
                    if(!"gridEncontrados".equalsIgnoreCase(this.tabSheet.getSelectedTab().getId())){
                        logger.debug("Atualizar documento da PARTE");
                    }
                    protocoloService.save(novoProtocolo, ProtocoloService.ACAO.SALVAR, new ArrayList<>(), true, false, null,((MyUI) UI.getCurrent()).getUsuarioLogado());
                    try {
                        IndisponibilidadePessoaProtocoloVO registroIndisponibilidade = new IndisponibilidadePessoaProtocoloVO(cdCartorioNaturezaPj, novoProtocolo.getId(), novoProtocolo.getNumero(), novoProtocolo.getDataProtocolo(), novoProtocolo.getRegistroReferencia());
                        indisponibilidadeService.informarProtocolado(this.parteSelecionado.getIdParte(), Arrays.asList(registroIndisponibilidade));

                    }catch (Exception e){
                        Notification.show("Indisponibilidade", "Erro ao verificar indisponibilidade: \n" +
                                "Oficio: " + this.parteSelecionado.getIdOficio() + "Parte: " + this.parteSelecionado.getIdParte() + ", Protocolo: " + novoProtocolo.getNumero(), Notification.Type.ERROR_MESSAGE);
                    }

                    ParteIndisponivel remover = null;
                    parteSelecionado.getRegistros().remove(item);
                    if("gridEncontrados".equalsIgnoreCase(this.tabSheet.getSelectedTab().getId())){
                        if(this.parteSelecionado.getRegistros().size() <= 1){
                            this.encontrados.remove(this.parteSelecionado);
                            gridEncontrados.deselectAll();
                        }

                        this.gridRegistros.getDataProvider().refreshAll();
                        this.gridEncontrados.getDataProvider().refreshAll();
                    }else{
                        if(this.parteSelecionado.getRegistros().size() <= 1){
                            this.encontradosSemDocumento.remove(this.parteSelecionado);
                            gridValidar.deselectAll();
                        }
                        this.gridRegistros.getDataProvider().refreshAll();
                        this.gridValidar.getDataProvider().refreshAll();

                    }
                    listarRegistros(parteSelecionado);

                    Notification.show(tipoIndisponibilidade + " de Indisponibilidade","Protocolo de nº "+ novoProtocolo.getNumero()+", gerado com sucesso", Notification.Type.ERROR_MESSAGE);
                    verificaMarcarComoFinalizado(parteSelecionado);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
            return button;
        }).setCaption("Protocolar");

        gridRegistros.addComponentColumn(item -> {
            Button button = new Button(VaadinIcons.FILE_REMOVE);
            button.addStyleName("borderless");
            button.addClickListener(click ->
            {
                IndisponibilidadePessoaRegistroNegativoVO negativo = new IndisponibilidadePessoaRegistroNegativoVO();
                negativo.setRegistroReferencia(item.getRegistro());
                negativo.setCdCartorioReferencia(cdCartorioNaturezaPj);
                indisponibilidadeService.informarRegistroNegativo(parteSelecionado.getIdParte(), Arrays.asList(negativo));

                ParteIndisponivel remover = null;
                parteSelecionado.getRegistros().remove(item);
                if(this.parteSelecionado.getRegistros().size() <= 1){
                    this.encontradosSemDocumento.remove(this.parteSelecionado);
                    gridValidar.deselectAll();
                }
                this.gridRegistros.getDataProvider().refreshAll();
                this.gridValidar.getDataProvider().refreshAll();
                listarRegistros(parteSelecionado);

                verificaMarcarComoFinalizado(parteSelecionado);
                //FIXME deve verificar se essa parte existe no encontrados se nao existir deve marcar como finalizado
            });
            return button;
        }).setCaption("Ignorar");

        this.indisponibilidadeService = BeanLocator.find(IndisponibilidadeService.class);
        panelLoading.setVisible(false);
        UploadFinishedHandler handler = new UploadFinishedHandler() {
            @Override
            public void handleFile(InputStream inputStream, String s, String s1, long l, int i) {
                try {
                    arquivo = File.createTempFile(String.valueOf(Calendar.getInstance().getTimeInMillis()), "xml");
                    try (FileOutputStream out = new FileOutputStream(arquivo)) {
                        IOUtils.copy(inputStream, out);
                    }

//                    btnProcessarArquivo.setEnabled(true);
                    processarArquivo();
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


    }

    private void verificaMarcarComoFinalizado(ParteIndisponivel parteSelecionado) {
        List<ParteIndisponivel> encontradosSemDocumentoResto = encontradosSemDocumento.stream()
                .filter(parte -> {
                    return parte.getIdParte().equals(parteSelecionado.getIdParte());
                })
                .collect(Collectors.toList());

        List<ParteIndisponivel> encontradosResto = encontrados.stream()
                .filter(parte -> { return parte.getIdParte().equals(parteSelecionado.getIdParte());})
                .collect(Collectors.toList());

        if(encontradosSemDocumentoResto.size() <= 0 && encontradosResto.size() <= 0){
            logger.debug("Marcando como finalizado: " + parteSelecionado.toString());
            indisponibilidadeService.marcarComoFinalizado(parteSelecionado.getIdParte());
        }

    }


    private void listarRegistros(ParteIndisponivel evt) {
        if(evt != null) {
            this.parteSelecionado = evt;
            gridRegistros.setItems(evt.getRegistros());
            gridRegistros.getDataProvider().refreshAll();
        }else{
            gridValidar.deselectAll();
            gridEncontrados.deselectAll();
            gridRegistros.deselectAll();
            gridRegistros.setItems(new ArrayList<>());
            gridRegistros.getDataProvider().refreshAll();
        }
    }
    private void processarArquivo() {
        try {

            panelLoading.setVisible(true);

            ResultadoImportacaoIndisponibilidadeXMLVO retornoUpload = indisponibilidadeService.enviarArquivoIndisponibilidade(this.arquivo, null
//                    () -> {
//                getUI().access(new Runnable() {
//                    ResultadoImportacaoIndisponibilidadeXMLVO retorno = null;
//
//                    public void setRetorno(ResultadoImportacaoIndisponibilidadeXMLVO retorno) {
//                        this.retorno = retorno;
//                    }
//
//                    @Override
//                    public void run() {
//                        arquivo.deleteOnExit();
//                        Notification.show("SUCESSO","Indisponibilidade processada com sucesso" , Notification.Type.ERROR_MESSAGE);
//                        panelLoading.setVisible(false);
//                        verificarIndisponibilidade();
//                    }
//                });
//
//            }
            );
            arquivo.deleteOnExit();
            Notification.show("Sucesso",  retornoUpload.toString() + " Importada(s) com sucesso" , Notification.Type.ERROR_MESSAGE);
            panelLoading.setVisible(false);
            verificarIndisponibilidade();

        } catch (Exception e) {
            Notification.show("Erro", e.getMessage(), Notification.Type.ERROR_MESSAGE);
        }
    }
}
