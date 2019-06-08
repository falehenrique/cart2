package br.com.exmart.indicadorRTDPJ.ui.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.apache.commons.lang3.StringUtils;

import br.com.exmart.indicadorRTDPJ.ui.util.GerenciarJanela;
import br.com.exmart.rtdpjlite.model.Registro;
import br.com.exmart.rtdpjlite.model.TipoProtocolo;
import br.com.exmart.rtdpjlite.service.LinkCompartilhadoService;
import br.com.exmart.rtdpjlite.service.RegistroService;
import com.vaadin.ui.*;
import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.MessageBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.vaadin.data.HasValue;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;


import br.com.exmart.indicadorRTDPJ.MyUI;
import br.com.exmart.indicadorRTDPJ.ui.ModalBusca;
import br.com.exmart.indicadorRTDPJ.ui.util.Especialidade;
import br.com.exmart.rtdpjlite.model.IndicadorPessoalVO;
import br.com.exmart.rtdpjlite.model.Objeto;
import br.com.exmart.rtdpjlite.service.IndicadorPessoalService;
import br.com.exmart.rtdpjlite.service.ObjetoService;
import br.com.exmart.rtdpjlite.service.rest.fonetica.FoneticaService;
import br.com.exmart.util.BeanLocator;


public class ModalBuscaImpl extends ModalBusca{

	private static final long serialVersionUID = 1L;

	protected static Logger logger = LoggerFactory.getLogger(ModalBuscaImpl.class);
    private ObjetoService objetoService;
    private IndicadorPessoalService indicadorPessoalService;
    private FoneticaService foneticaService;
    private RegistroService registroService;
    private LinkCompartilhadoService linkCompartilhadoService;
    public ModalBuscaImpl() {
        this.objetoService = BeanLocator.find(ObjetoService.class);
        this.indicadorPessoalService = BeanLocator.find(IndicadorPessoalService.class);
        this.foneticaService = BeanLocator.find(FoneticaService.class);
        this.registroService = BeanLocator.find(RegistroService.class);
        this.linkCompartilhadoService = BeanLocator.find(LinkCompartilhadoService.class);

        btnFiltros.addClickListener(evt -> mostrarFiltros());
        btnBuscar.addClickListener(evt->buscarClickListener(evt));
        seletorBusca.setEmptySelectionAllowed(false);
        seletorBusca.setItemCaptionGenerator(TipoBusca::getNome);
        seletorBusca.addValueChangeListener(evt->seletorBuscaListener(evt));
        comboObjeto.setItems(objetoService.findAll());
        comboObjeto.addValueChangeListener(evt->comboObjetoListener(evt));
        comboObjeto.setPageLength(300);
        comboAtributo.setPageLength(300);
        btnBuscarIndicadorReal.addClickListener(evt->btnBuscarIndicadorRealClickListener(evt));
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        seletorBusca.setPageLength(0);
        @SuppressWarnings("serial")
		ShortcutListener action = new ShortcutListener("Action", ShortcutAction.KeyCode.ENTER, null) {
          
			@Override
            public void handleAction(Object sender, Object target) {
                if (target == txtBusca) {
                    buscarClickListener(null);
                } else if (target == txtBuscaReal) {
                    btnBuscarIndicadorRealClickListener(null);
                }
            }
        };
        getActionManager().addAction(action);
        gerarTipoBusca(false);
        seletorBusca.setEmptySelectionAllowed(false);
        preencherDadosPesquisados();
        btnGerarLinkCompartilhamento.addClickListener(evt->btnGerarLinkCompartilhamentoClick());
        txtBusca.focus();

    }

    private void btnGerarLinkCompartilhamentoClick() {
        if(grid.getSelectedItems().size()> 0) {
            MessageBox
                    .createQuestion()
                    .withCaption("Compartilhar")
                    .withMessage("Gerar link de compartilhamento externo para TODOS os registro SELECIONADOS?")
                    .withYesButton(() -> {
                        List<Registro> registrosLink = new ArrayList<>();
                        for(IndicadorPessoalVO registro: grid.getSelectedItems()) {
                            registrosLink.add(registroService.findByRegistroAndEspecialidade(registro.getRegistro(), registro.getEspecialidade()));
                        }
                        try {
                            String link = linkCompartilhadoService.gerarLink(registrosLink, ((MyUI) UI.getCurrent()).getUsuarioLogado());
                            TextField textField = new TextField();
                            textField.setStyleName("borderless");
                            textField.setValue(link);
                            textField.setReadOnly(true);
                            textField.setWidth("600px");
                            MessageBox.create()
                                    .withWidth("650px")
                                    .withCaption("Link compartilhado")
                                    .withMessage(textField)
                                    .withCloseButton(ButtonOption.caption("Fechar"))
                                    .open();
                            textField.focus();
                            textField.selectAll();
                        }catch (Exception e){
                            Notification.show("ATENÇÃO",e.getMessage(), Notification.Type.ERROR_MESSAGE);
                        }
                    }, ButtonOption.caption("SIM"), ButtonOption.focus())
                    .withNoButton(ButtonOption.caption("NÃO"))
                    .open();
        }else{
            Notification.show("ATENÇÃO","Favor selecionar pelo menos 1 registro", Notification.Type.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
	public void preencherDadosPesquisados(){
    	MyUI myUI = ((MyUI) UI.getCurrent());
    	Object itens = myUI.getItemSession("itens");
    	Object textoBusca = myUI.getItemSession("textoBusca");
    	Object tipoBusca =  myUI.getItemSession("tipoBusca");
    	
    	if(!Objects.isNull(itens))  grid.setItems((List<IndicadorPessoalVO>)itens);
        if(!Objects.isNull(textoBusca)) txtBusca.setValue((String)textoBusca);
        if(!Objects.isNull(tipoBusca))
        	seletorBusca.setValue((TipoBusca)tipoBusca);
        else
        	setarBuscaPadrao();
  } 
  		
     
    
    
    private void setarBuscaPadrao() {
        if(( (MyUI) UI.getCurrent()).getEspecialidadeAtual().equals(Especialidade.PJ)){
            seletorBusca.setValue(new TipoBusca("INDICADOR-PESSOAL-PJ","PJ - INDICADOR PESSOAL"));
        }else{
            seletorBusca.setValue(new TipoBusca("INDICADOR-PESSOAL-TD","TD - INDICADOR PESSOAL"));
        }
    }


    private void btnBuscarIndicadorRealClickListener(Button.ClickEvent evt) {
        List<IndicadorPessoalVO> indicadorPessoalVOS = indicadorPessoalService.findByObjeto(comboObjeto.getValue().getId(), comboAtributo.getValue().getNome(), txtBuscaReal.getValue(), null);
        if(indicadorPessoalVOS.size() <= 0 ){
            Notification.show(
                    "Resultado da Busca", "Nenhum registro encontrado", Notification.Type.WARNING_MESSAGE);
        }
        grid.setItems(indicadorPessoalVOS);
        grid.setVisible(true);
        seletorBusca.focus();

    }

    private void comboObjetoListener(HasValue.ValueChangeEvent<Objeto> evt) {
        if(evt != null){
            comboAtributo.clear();
            comboAtributo.setItems(evt.getValue().getAtributos());
        }
    }

    private void seletorBuscaListener(HasValue.ValueChangeEvent<TipoBusca> evt) {
        if(evt != null){
            if(evt.getValue() != null){
                if(evt.getValue().getValor().startsWith("INDICADOR-REAL")){
                    buscaPessoal.setVisible(false);
                    buscaReal.setVisible(true);
                    panelProtocolos.removeComponent(panelProtocolos.getComponent(panelProtocolos.getComponentCount()-1));
                    panelProtocolos.addComponentsAndExpand(grid);

                }else if(evt.getValue().getValor().startsWith("INDICADOR-PESSOAL") || evt.getValue().getValor().startsWith("REGISTROS")){
                    buscaPessoal.setVisible(true);
                    buscaReal.setVisible(false);
                    panelProtocolos.removeComponent(panelProtocolos.getComponent(panelProtocolos.getComponentCount()-1));
                    panelProtocolos.addComponentsAndExpand(grid);
                }else{
                    buscaPessoal.setVisible(false);
                    buscaReal.setVisible(false);
                    buscaPessoal.setVisible(false);
                    buscaReal.setVisible(false);
                    panelProtocolos.removeComponent(grid);
                    panelProtocolos.addComponentsAndExpand(new BuscarProtocolosImpl());
                }
            }
        }
    }

    
    private void buscarClickListener(Button.ClickEvent evt) {
        if(!Strings.isNullOrEmpty(txtBusca.getValue())) {
        	
        	
        	if(seletorBusca.getValue() == null){
                setarBuscaPadrao();
            }
            List<String> especialidades = new ArrayList<>();
            if (seletorBusca.getValue().getValor().contains("PJ")) {
                especialidades.add("PJ");
            } else {
                especialidades.add("TD");
            }
            try {
                if (txtBusca.getValue() != null) {
                    List<IndicadorPessoalVO> indicadorPessoalVOS = new ArrayList<>();
                    if (seletorBusca.getValue().getValor().contains("REGISTROS-")) {
                        Long nrRegistro = Long.parseLong(txtBusca.getValue());
                        indicadorPessoalVOS = indicadorPessoalService.findByRegistro(nrRegistro, especialidades.get(0), null);
                    } else if (seletorBusca.getValue().getValor().contains("INDICADOR-PESSOAL-")) {
                        try {
                            String nomeFonetico = foneticaService.foneticar(txtBusca.getValue())+"%";
                            if(ckTipoBuscaNome.getValue())
                                nomeFonetico = "%"+nomeFonetico;


                            Pattern p = Pattern.compile("-?\\d+");
                            Matcher m = p.matcher(txtBusca.getValue());
                            String numeros = "%";
                            boolean entrou = false;
                            while (m.find()) {
                                entrou = true;
                                numeros = numeros + m.group()+"%";
                            }
                            if(!entrou)
                                numeros = null;

                            if(nomeFonetico.equalsIgnoreCase("null%"))
                                numeros = null;

                            logger.debug("Buscando nome fonetico: " + nomeFonetico);
                            if(!Strings.isNullOrEmpty(numeros)){
                                logger.debug("Buscando nome fonetico complmentos: " + numeros);
                            }
                            indicadorPessoalVOS = indicadorPessoalService.findByParteFonetico(nomeFonetico, txtBusca.getValue(), especialidades.get(0), numeros);
                        } catch (Exception e) {
                            String nome = txtBusca.getValue()+"%";
                            if(ckTipoBuscaNome.getValue())
                                nome = "%"+nome;
                            indicadorPessoalVOS = indicadorPessoalService.findByParte(nome, txtBusca.getValue(), especialidades.get(0));
                        }
                    } else if (seletorBusca.getValue().getValor().contains("PROTOCOLOS")) {
                        System.out.println("deve listar protocolo");
                    }
                    if (indicadorPessoalVOS.size() <= 0) {
                        Notification.show("Resultado da Busca", "Nenhum registro encontrado", Notification.Type.WARNING_MESSAGE);
                    }
                    grid.setItems(indicadorPessoalVOS);
                    grid.setVisible(true);
                    
                    MyUI myUI = ((MyUI) UI.getCurrent());
                    myUI.setItemSession("itens",indicadorPessoalVOS);
                    myUI.setItemSession("textoBusca",txtBusca.getValue());
                    myUI.setItemSession("tipoBusca",seletorBusca.getValue());
                }
            } catch (NumberFormatException e) {
                Notification.show("Atenção", "Este tipo de busca só aceita caracteres numéricos", Notification.Type.ERROR_MESSAGE);
            }
        }else{
            Notification.show("Atenção", "Para realizar uma busca digite ao menos 1 caractere", Notification.Type.ERROR_MESSAGE);
        }
    }

    private void mostrarFiltros(){
        panelIndicadorPessoal.setVisible(!panelIndicadorPessoal.isVisible());
        ckTipoBuscaNome.setValue(false);
    }


    public void gerarTipoBusca(boolean icView){
        List<TipoBusca> retorno = new ArrayList<>();
        retorno.add(new TipoBusca("REGISTROS-TD","TD - REGISTRO"));
        retorno.add(new TipoBusca("INDICADOR-REAL","TD - INDICADOR REAL"));
        retorno.add(new TipoBusca("INDICADOR-PESSOAL-TD","TD - INDICADOR PESSOAL"));
        retorno.add(new TipoBusca("REGISTROS-PJ","PJ - REGISTRO"));
        retorno.add(new TipoBusca("INDICADOR-PESSOAL-PJ","PJ - INDICADOR PESSOAL"));
        if(!icView)
        retorno.add(new TipoBusca("PROTOCOLOS","PROTOCOLOS"));

        seletorBusca.setItems(retorno);

    }


    public class TipoBusca {
        private String nome;
        private String valor;

        public TipoBusca(String valor, String nome) {
            this.nome = nome;
            this.valor = valor;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getValor() {
            return valor;
        }

        public void setValor(String valor) {
            this.valor = valor;
        }
    }
}
