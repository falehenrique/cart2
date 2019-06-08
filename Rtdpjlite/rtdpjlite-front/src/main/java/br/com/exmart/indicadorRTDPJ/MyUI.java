package br.com.exmart.indicadorRTDPJ;

import br.com.exmart.indicadorRTDPJ.ui.RelatorioMovimentoDiario;
import br.com.exmart.indicadorRTDPJ.ui.impl.*;
import br.com.exmart.indicadorRTDPJ.ui.navigator.NavigationManager;
import br.com.exmart.indicadorRTDPJ.ui.util.Especialidade;
import br.com.exmart.indicadorRTDPJ.ui.util.GerenciarJanela;
import br.com.exmart.indicadorRTDPJ.ui.view.BuscarIndicadorView;
import br.com.exmart.indicadorRTDPJ.ui.view.BuscarPedidoView;
import br.com.exmart.indicadorRTDPJ.ui.view.BuscarProtocoloView;
import br.com.exmart.indicadorRTDPJ.ui.vo.RTDPJNotificacao;
import br.com.exmart.rtdpjlite.exception.ErrorListException;
import br.com.exmart.rtdpjlite.exception.NotUseSelo;
import br.com.exmart.rtdpjlite.model.*;
import br.com.exmart.rtdpjlite.repository.StatusRepository;
import br.com.exmart.rtdpjlite.service.*;
import br.com.exmart.rtdpjlite.service.rest.seloeletronico.SeloEletronicoRest;
import br.com.exmart.rtdpjlite.vo.financeiro.CustasCartorio;

import com.github.appreciated.app.layout.AppLayout;
import com.github.appreciated.app.layout.annotations.MenuCaption;
import com.github.appreciated.app.layout.behaviour.AppLayoutComponent;
import com.github.appreciated.app.layout.behaviour.Behaviour;
import com.github.appreciated.app.layout.builder.AppLayoutConfiguration;
import com.github.appreciated.app.layout.builder.CDIAppLayoutBuilder;
import com.github.appreciated.app.layout.builder.design.AppLayoutDesign;
import com.github.appreciated.app.layout.builder.elements.builders.CDISubmenuBuilder;
import com.github.appreciated.app.layout.builder.entities.DefaultBadgeHolder;
import com.github.appreciated.app.layout.builder.entities.DefaultNotificationHolder;
import com.github.appreciated.app.layout.builder.factories.DefaultSpringNavigationElementInfoProducer;
import com.github.appreciated.app.layout.component.MenuHeader;
import com.github.appreciated.app.layout.component.button.AppBarNotificationButton;
import com.vaadin.annotations.*;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.PushStateNavigation;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewLeaveAction;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.MessageBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import javax.servlet.annotation.WebServlet;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Push
//@PushStateNavigation
@EnableAsync
@Viewport("width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no")
@SpringUI
@Theme("mytheme")
@MenuCaption("UI")
@Title("#RTDPJ-WEB")
//@PushStateNavigation
//@WebServlet(value = "/*", asyncSupported = true)
//@VaadinServletConfiguration(productionMode = true, ui = MyUI.class, heartbeatInterval=10, closeIdleSessions=true)
public class MyUI extends UI {

    protected static Logger logger= LoggerFactory.getLogger(MyUI.class);
    private final SpringViewProvider viewProvider;
    private final NavigationManager navigationManager;
    private Usuario usuario;
    private UsuarioService usuarioService;
    private FinanceiroService financeiroService;
    private String natureza= "Títulos e Documentos e Pessoa Jurídica";
    private List<CustasCartorio> custasCartorio;
    private Button btnBusca = new Button("",VaadinIcons.SEARCH);
    private Button btnLogout = new Button("",VaadinIcons.SIGN_OUT);
    private AppLayoutComponent layout;
    private UsuarioNotificacaoService usuarioNotificacaoService;

    private List<Natureza> naturezas;
    private List<SubNatureza> subNaturezas;
    private List<FormaEntrega> formaEntregas;
    private List<Status> status;
    private List<Qualidade> qualidades;
    private List<TipoDocumento> tipoDocumentos;
    private List<Nacionalidade> nacionalidades;
    private List<RegimeBens> regimeBens;
    private List<EstadoCivil> estadoCivils;
    private List<Objeto> objetos;
    private List<Profissao> profissaos;
    private List<Estado> estados;
    private List<TipoLogradouro> tipoLogradouros;
    SeloEletronicoRest seloEletronicoRest;
    private ProtocoloService protocoloService;

    public String getNatureza() {
        return natureza;
    }

    private boolean icGhostscript = false;

    @Autowired
    public MyUI(SpringViewProvider viewProvider, NavigationManager navigationManager, UsuarioService usuarioService, FinanceiroService financeiroService, UsuarioNotificacaoService usuarioNotificacaoService,
                NaturezaService naturezaService,
                SubNaturezaService subNaturezaService,
                FormaEntregaService formaEntregaService,
                StatusRepository statusRepository,
                QualidadeService qualidadeService,
                TipoDocumentoService tipoDocumentoService,
                NacionalidadeService nacionalidadeService,
                RegimeBensService regimeBensService,
                EstadoCivilService estadoCivilService,
                ObjetoService objetoService,
                ProfissaoService profissaoService,
                EstadoService estadoService,
                TipoLogradouroService tipoLogradouroService,
                SeloEletronicoRest seloEletronicoRest,
                ProtocoloService protocoloService
                ) {
        this.viewProvider = viewProvider;
        this.navigationManager = navigationManager;
        this.usuarioService = usuarioService;
        this.financeiroService = financeiroService;
        this.usuarioNotificacaoService = usuarioNotificacaoService;
        btnBusca.setStyleName("borderless");
        btnBusca.setSizeFull();
        btnBusca.addClickListener(evt -> abrirBusca());
        btnLogout.setStyleName("borderless");
        btnLogout.setSizeFull();
        btnLogout.addClickListener(evt -> logout());
        btnLogout.setIconAlternateText("Logout");


        this.naturezas = naturezaService.findAll();
        this.subNaturezas = subNaturezaService.findAll();
        this.formaEntregas = formaEntregaService.findAll();
        this.status = statusRepository.findByIcSistemaIsFalseOrderByNome();
        this.qualidades = qualidadeService.findAll();
        this.tipoDocumentos = tipoDocumentoService.findAll();
        this.nacionalidades = nacionalidadeService.findAll();
        this.regimeBens = regimeBensService.findAll();
        this.estadoCivils = estadoCivilService.findAll();
        this.objetos = objetoService.findAll();
        this.profissaos = profissaoService.findAll();
        this.estados = estadoService.findAll();
        this.tipoLogradouros = tipoLogradouroService.findAll();
        this.protocoloService = protocoloService;

        this.seloEletronicoRest = seloEletronicoRest;
    }

    private void logout() {
        getUI().getPage().setLocation("/logout");
    }


    private void abrirBusca() {
        GerenciarJanela.abrirJanela("",90,80, new ModalBuscaImpl(),null, true, true, false, true);    }

    public static String URL_BACK = "http://localhost:8080/rtdpjapi";

    private Especialidade especialidadeAtual = Especialidade.TD;
    public static Navigator navigator;
    public static PrincipalRTDPJImpl mainView;


    DefaultNotificationHolder notifications = new DefaultNotificationHolder();
    DefaultBadgeHolder badge = new DefaultBadgeHolder();
    @Override
    protected void init(VaadinRequest vaadinRequest) {

        CDIAppLayoutBuilder layoutBuild = AppLayout.getCDIBuilder(Behaviour.LEFT_OVERLAY)
                .withViewProvider(() -> viewProvider)
                .withNavigationElementInfoProducer(new DefaultSpringNavigationElementInfoProducer())
                .withTitle("RTDPJ-WEB")
                .addToAppBar(new AppBarNotificationButton(notifications, true))
                .add(new MenuHeader("RTDPJ-WEB (V:2018.0.2)", new ThemeResource("icon-192.png")))
                .addToAppBar(this.btnBusca)
                .addToAppBar(this.btnLogout)
                .withDesign(AppLayoutDesign.MATERIAL)
                .add(BalcaoImpl.class)
                .add(CDISubmenuBuilder.get("NOVO PROTOCOLO TD", VaadinIcons.RECORDS)
                        .add(ViewProtocoloExameCalculoTd.class)
                        .add(ViewPrenotacaoTd.class)
                        .add(ViewProtocoloCertidaoTd.class)
                        .build())
                .add(CDISubmenuBuilder.get("NOVO PROTOCOLO PJ", VaadinIcons.BUILDING)
                        .add(ViewProtocoloExameCalculoPj.class)
                        .add(ViewPrenotacaoPj.class)
                        .add(ViewProtocoloCertidaoPj.class)
                        .build())
                .add(BuscarPedidoView.class)
                .add(BuscarProtocoloView.class)
                .add(BuscarIndicadorView.class)
                .add(ViewIndisponibilidadeImpl.class)
                .add(CDISubmenuBuilder.get("RELATÓRIOS", VaadinIcons.NEWSPAPER)
                        .add(RelatorioProtocoloOficialPjImpl.class)
                        .add(RelatorioProtocoloOficialTdImpl.class)
                        .add(RelatorioRecolhimentoExtraJudicialImpl.class)
                        .add(RelatorioProtocoloOficialExamePjImpl.class)
                        .add(RelatorioProtocoloOficialExameTdImpl.class)
                        .add(RelatorioMovimentoDiarioImpl.class)
                        .add(RelatorioNaturezaImpl.class)
                        .add(RelatorioMovimentacaoFaturadoImpl.class)
                        .build()
                );
                try{
                    seloEletronicoRest.utilizaSelo();
                    layoutBuild.add(CDISubmenuBuilder.get("INFORMES", VaadinIcons.PAPERPLANE)
                            .add(InformeSelosImpl.class)
                            .build()
                    );
                }catch (NotUseSelo e){

                }
                layoutBuild
                .add(CDISubmenuBuilder.get("LOTE", VaadinIcons.NEWSPAPER)
                    .add(ViewLoteImpl.class)
                        .addClickable("Registrar",VaadinIcons.CHECK, clickEvent -> {
                            MessageBox
                                    .createInfo()
                                    .withCaption("Registrar em Lote")
                                    .withMessage("Deseja registrar TODOS os protocolos que estão marcados com o status 'REGISTRAR EM LOTE'?")
                                    .withYesButton(() -> {
                                        try {
                                            this.protocoloService.registrarProtocolosLote(this.getCustasCartorio(), this.getUsuarioLogado());
                                            navigateTo("protocolos");
                                        }catch (ErrorListException ele){
                                            String mensagem = ele.getErroList().toString();
                                            mensagem = mensagem.replace("[","");
                                            mensagem = mensagem.replaceAll(",",";");
                                            mensagem = mensagem.replace("]","");
                                            Notification.show("Atenção", mensagem, Notification.Type.ERROR_MESSAGE);
                                            ele.printStackTrace();
                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                        }
                                    }, ButtonOption.caption("SIM"))
                                    .withCloseButton(ButtonOption.caption("NÃO"))
                                    .open();
                        })
                    .build());
                layoutBuild.add(CDISubmenuBuilder.get("ADM", VaadinIcons.TOOLS)
                        .add(ViewCadastroClienteImpl.class)
                        .build());
                layout = layoutBuild.build();
        setContent(layout);




//        badge.setCount(4);
//        notifications.addNotification(new DefaultNotification("Teste", "1"));
//        notifications.addNotification(new DefaultNotification("Title1", "Descricao", VaadinIcons.ACADEMY_CAP, DefaultNotification.Priority.HIGH, false));
//        notifications.addNotification(new DefaultNotification("Title2", "Descricao2", VaadinIcons.ACADEMY_CAP, DefaultNotification.Priority.MEDIUM, false));
//        notifications.addNotification(new DefaultNotification("Title3", "Descricao3", VaadinIcons.ACADEMY_CAP, DefaultNotification.Priority.LOW, true));
//        setContent(mainView);
//        navigationManager.navigateToDefaultView();
        this.getNavigator().addViewChangeListener(evt -> viewChangeListener(evt));
    }

    private boolean viewChangeListener(ViewChangeListener.ViewChangeEvent evt) {
        if(evt.getNewView() != null) {
            Page.Styles styles = Page.getCurrent().getStyles();
            if (evt.getNewView() instanceof ViewProtocoloExameCalculoTd || evt.getNewView() instanceof ViewPrenotacaoTd || evt.getNewView() instanceof ViewProtocoloCertidaoTd) {
                layout.setTitle("RTDPJ-WEB - REGISTRO DE TÍTULOS E DOCUMENTOS");
                setEspecialidadeAtual(Especialidade.TD);
                styles.add(".mytheme .app-layout-behaviour-left-overlay app-toolbar { background-color: #00C896 }");
                styles.add(".mytheme .v-label v-widget v-label-large v-label-colored { color: #00C896 !important}");
                styles.add(".mytheme .v-tabsheet-tabitem v-tabsheet-tabitem-selected v-tabsheet-tabitem-focus { color: #00C896 !important}");
                styles.add(".mytheme .v-button .v-button primary{ background-color: #00C896 !important}");
//TODO      AQUI KOKAO
//                styles.add(".v-app .v-selection-color { color: #00C896 !important; background: #00C896 !important;}");
//                setTheme(Especialidade.TD.getTema());
            } else if (evt.getNewView() instanceof ViewProtocoloExameCalculoPj || evt.getNewView() instanceof ViewPrenotacaoPj || evt.getNewView() instanceof ViewProtocoloCertidaoPj){
                layout.setTitle("RTDPJ-WEB - REGISTRO CIVIL DAS PESSOAS JURÍDICAS");
                setEspecialidadeAtual(Especialidade.PJ);
                styles.add(".mytheme .app-layout-behaviour-left-overlay app-toolbar { background-color: #4B96C8 }");
                //TODO      AQUI KOKAO
//                setTheme(Especialidade.PJ.getTema());
            }else {
//                setTheme("mytheme");
                layout.setTitle("RTDPJ-WEB");
                styles.add(".mytheme .app-layout-behaviour-left-overlay app-toolbar { background-color: #4b4b65 }");
                //TODO      AQUI KOKAO
            }


        }
        return true;
    }


    public void navigateTo(String viewName) {
        Collection<Window> windows = UI.getCurrent().getWindows();

        Object[] array = windows.toArray();
        for (int i = 0; i < array.length; i++) {
            ((Window)array[i]).close();
        }
        this.getNavigator().navigateTo(viewName);
    }


    public void setEspecialidadeAtual(Especialidade especialidadeAtual) {
        this.especialidadeAtual = especialidadeAtual;
    }

    public Especialidade getEspecialidadeAtual() {
        return especialidadeAtual;
    }

    public Usuario getUsuarioLogado() {
        return usuario;
    }

    @PostConstruct
    private void postConstructor(){
        SecurityContext auth = SecurityContextHolder.getContext();
        User user = (User) auth.getAuthentication().getPrincipal();
        this.usuario = usuarioService.findByEmail(user.getUsername());
        logger.debug("Usuario logado: " + this.usuario.getNome());
        this.custasCartorio = financeiroService.findAllCustas();
        listarNoficacao();
    }

    public List<CustasCartorio> getCustasCartorio() {
        return custasCartorio;
    }

    public void addNotification(String assunto, String descricao) {
        usuarioNotificacaoService.adicionarNotificacao(getUsuarioLogado().getId(), assunto, descricao);
        listarNoficacao();
    }

    private void listarNoficacao() {
        List<UsuarioNotificacao>  noficacaoList = usuarioNotificacaoService.findByUsuarioIdAndLidaIsNull(getUsuarioLogado().getId());
        for(UsuarioNotificacao notificacao: noficacaoList) {
            notifications.addNotification(new RTDPJNotificacao(notificacao.getAssunto(), notificacao.getDescricao(),  notificacao.getId()));
            notifications.addNotificationClickedListener(evt->notificacaoClikListener((RTDPJNotificacao) evt));
        }
    }

    private void notificacaoClikListener(RTDPJNotificacao evt) {
        usuarioNotificacaoService.marcarComoLida(evt.getNotificacaoId());
        notifications.removeNotification(evt);

    }

    public void setItemSession(String key, Object value) {
    	getSession().setAttribute(key, value);
    }

    public Object getItemSession(String key) {
    	return getSession().getAttribute(key);
    }
    
    
    public void cleanItemSession(String key) {
    	getSession().setAttribute(key,"");
    }
    
    public List<Natureza> getNaturezas() {
        return naturezas;
    }

    public List<SubNatureza> getSubNaturezas() {
        return subNaturezas;
    }

    public List<FormaEntrega> getFormaEntregas() {
        return formaEntregas;
    }

    public List<Status> getStatus() {
        return status;
    }

    public List<Qualidade> getQualidades() {
        return qualidades;
    }

    public List<TipoDocumento> getTipoDocumentos() {
        return tipoDocumentos;
    }

    public List<Nacionalidade> getNacionalidades() {
        return nacionalidades;
    }

    public List<RegimeBens> getRegimeBens() {
        return regimeBens;
    }

    public List<EstadoCivil> getEstadoCivils() {
        return estadoCivils;
    }

    public List<Objeto> getObjetos() {
        return objetos;
    }

    public List<Profissao> getProfissaos() {
        return profissaos;
    }

    public List<TipoLogradouro> getTipoLogradouros() {
        return tipoLogradouros;
    }

    public List<Estado> getEstados() {
        return estados;
    }
}
