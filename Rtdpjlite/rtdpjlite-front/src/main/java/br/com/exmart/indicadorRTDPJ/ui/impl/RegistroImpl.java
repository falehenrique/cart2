package br.com.exmart.indicadorRTDPJ.ui.impl;

import br.com.exmart.indicadorRTDPJ.MyUI;
import br.com.exmart.indicadorRTDPJ.ui.Registro;
import br.com.exmart.indicadorRTDPJ.ui.component.ChecklistItemComponentImpl;
import br.com.exmart.indicadorRTDPJ.ui.util.GerenciarJanela;
import br.com.exmart.rtdpjlite.exception.ErrorListException;
import br.com.exmart.rtdpjlite.exception.NotUseSelo;
import br.com.exmart.rtdpjlite.model.*;
import br.com.exmart.rtdpjlite.service.*;
import br.com.exmart.rtdpjlite.service.rest.seloeletronico.SeloEletronicoRest;
import br.com.exmart.rtdpjlite.vo.financeiro.CustasCartorio;
import br.com.exmart.rtdpjlite.vo.financeiro.ServicoEtiqueta;
import br.com.exmart.rtdpjlite.vo.seloeletronico.SeloParteRTDPJ;
import br.com.exmart.rtdpjlite.vo.seloeletronico.SeloRTDPJ;
import br.com.exmart.util.BeanLocator;
import com.google.common.base.Strings;
import com.vaadin.data.BeanValidationBinder;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.MessageBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.spring.events.EventBus;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class RegistroImpl extends Registro implements PassoProtocolo{

    protected static Logger logger= LoggerFactory.getLogger(RegistroImpl.class);
    private EventBus.ViewEventBus eventBus;
    private ChecklistService checklistService;
    private Protocolo protocolo;
    private ProtocoloService protocoloService;
    private BeanValidationBinder<Protocolo> binderProtocolo = new BeanValidationBinder<>(Protocolo.class);
    private FinanceiroService financeiroService;
    private ArquivoService arquivoService;
    private boolean isExameCalculo = false;
    private ModeloService modeloService;
    private String natureza = "Títulos e Documentos e Pessoa Jurídica";

    private SeloEletronicoRest seloEletronicoRest;

    public RegistroImpl(Protocolo protocolo, EventBus.ViewEventBus eventBus){
        logger.debug("iniciou RegistroImpl");
        this.eventBus = eventBus;
        this.protocoloService = BeanLocator.find(ProtocoloService.class);
        this.checklistService = BeanLocator.find(ChecklistService.class);
        this.modeloService = BeanLocator.find(ModeloService.class);
        this.financeiroService = BeanLocator.find(FinanceiroService.class);
        this.arquivoService = BeanLocator.find(ArquivoService.class);
        this.seloEletronicoRest = BeanLocator.find(SeloEletronicoRest.class);

        this.protocolo = protocolo;
        btnRegistrar.addClickListener(evt-> btnRegistrarClickListener());
        List<ServicoEtiqueta> etiquetas = new ArrayList<>();


        btnDevolver.addClickListener(evt-> {
            MessageBox
                .createQuestion()
                .withCaption("Devolver Protocolo")
                .withMessage("Confirmar devolução?")
                .withYesButton(() -> {
                    GerenciarJanela.abrirJanela("", 80, 90, new EditorTextoImpl(getNaoMarcados(), modeloService.findModeloByTipo(this.protocolo.getNatureza().getId(), this.protocolo.getSubNatureza() != null ? this.protocolo.getSubNatureza().getId() : null, TipoModelo.NOTA_DEVOLUCAO), this.protocolo , EditorTextoImpl.ACAO.DEVOLVER_PROTOCOLO, this.eventBus), null, true, true, false, true);}, ButtonOption.caption("SIM"))
                .withNoButton(ButtonOption.caption("NÃO"))
                .open();
        });

        gerarChecklist();

        bindCampos();
        gerarTimeline();

        this.isExameCalculo = protocolo.getTipo().equals(TipoProtocolo.EXAMECALCULO_PJ) || protocolo.getTipo().equals(TipoProtocolo.EXAMECALCULO_TD);
        logger.debug("acabou RegistroImpl");
    }

    private void btnRegistrarClickListener() {


        try {
            if(protocolo.isIcRegistrarTituloOutraPraca()){
                if(protocolo.getCartorioParceiroList().size() <=0){
                    throw new ErrorListException("Cartório Parceiro", Arrays.asList(new String[]{"Favor informar pelo menos 1"}));
                }else{
                    for(ProtocoloCartorioParceiro protocoloCartorioParceiro : protocolo.getCartorioParceiroList()){
                        if(protocoloCartorioParceiro.getId() == null ){
                            throw new ErrorListException("Cartório Parceiro", Arrays.asList(new String[]{"Favor salvar o protocolo antes de registrar"}));
                        }
                    }
                }
            }
            if(!TipoProtocolo.isExameCalculo(protocolo.getTipo())) {
                try {
                    seloEletronicoRest.validarSelo(protocolo,
                            financeiroService.listarServicosProtocolo(this.protocolo.getNumero(), protocolo.getTipo()),
                            SeloParteRTDPJ.fromParteProtocolo(protocolo.getPartesProtocolo()));
                }catch (NotUseSelo e){

                }
            }
            if(verificaChecklistObrigatorio()){
                if(isExameCalculo){
                    MessageBox
                            .createQuestion()
                            .withCaption("Apto para Registro")
                            .withMessage("Confirmar?")
                            .withYesButton(() -> {

                                GerenciarJanela.abrirJanela("", 80, 90, new EditorTextoImpl(null,modeloService.findModeloByTipo(this.protocolo.getNatureza().getId(), this.protocolo.getSubNatureza() != null ? this.protocolo.getSubNatureza().getId() : null, TipoModelo.APTO_REGISTRO), this.protocolo, EditorTextoImpl.ACAO.APTO_REGISTRO, this.eventBus), null, true, true, false, true);
//                            this.eventBus.publish(this, new ProtocoloEvent.AptoRegistroProtocolo(this.protocolo));

                            }, ButtonOption.caption("SIM"))
                            .withNoButton(ButtonOption.caption("NÃO"))
                            .open();
                }else {
                    if (protocoloService.isArquivoEnviado(protocolo.getNumero(), ((MyUI)UI.getCurrent()).getEspecialidadeAtual().getEspecialidade(), protocolo.getTipo())) {
                        try {
                            File arquivo = arquivoService.recuperarDocumentoProtocoloFile(protocolo.getNumero(),TipoProtocolo.recuperaEspecialidade(protocolo.getTipo()),protocolo.getTipo());
                            GerenciarJanela.abrirJanela("FINALIZAR REGISTRO", 80, 90, new ModalRegistroImpl(this.protocolo, modeloService.findModeloByTipo(this.protocolo.getNatureza().getId(), this.protocolo.getSubNatureza() != null ? this.protocolo.getSubNatureza().getId() : null, TipoModelo.CARIMBO),financeiroService.recuperarCustasServico(financeiroService.listarServicosProtocolo(protocolo.getNumero(), protocolo.getTipo()), ((MyUI) UI.getCurrent()).getCustasCartorio() ), ModalRegistroImpl.TipoDocumento.REGISTRO, arquivo, new ArrayList<>(), null, false), null, true, true, true, true);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Notification.show("Erro", "Arquivo PDF do registro não encontrado, favor realizar o upload do mesmo na aba Documentos", Notification.Type.ERROR_MESSAGE);
                    }
                }

            }

        } catch (ErrorListException e) {
            Notification.show("Erro", e.getMessage() + "\n" + e.recuperaErros(), Notification.Type.ERROR_MESSAGE);
//            e.printStackTrace();
        }

    }
    boolean retorno = true;
    private boolean verificaChecklistObrigatorio() {
        retorno = true;
        String msgPessoa = "";
        panelChecklist.iterator().forEachRemaining(item ->{
            if(item instanceof ChecklistItemComponentImpl) {
                if(((ChecklistItemComponentImpl)item).getChecklistItem().isObrigatorio()){
                    if(!((ChecklistItemComponentImpl)item).isChecked()){
                        this.retorno= false;
                    }
                }
            }
        });
        if(!isExameCalculo) {
            if(this.protocolo.getPartesProtocolo().size() <=0){
                retorno = false;
                msgPessoa = ", se as partes foram qualificadas";
            }
            if (Strings.isNullOrEmpty(situacaoAtualRegistro.getValue())) {
                msgPessoa = msgPessoa + ", se o campo Situação Atual foi preenchido";
                retorno = false;
            }
        }
        if(!retorno){
            Notification.show("Atenção", "Verifique se todos os itens obrigatórios do checklist foram preenchidos"+ msgPessoa, Notification.Type.ERROR_MESSAGE);
        }


        return this.retorno;
    }

    private void gerarTimeline() {
        for(CardTimeline card : this.protocoloService.getTimeLine(this.protocolo.getRegistroReferencia(), TipoProtocolo.recuperaEspecialidade(this.protocolo.getTipo()))){
            if(card != null) {
                timeline.addComponent(new CardLinhaTempoImpl(card, null));
            }
        }
    }

    private void bindCampos() {

        binderProtocolo.bind(situacaoAtualRegistro, "situacaoAtualRegistro");
        binderProtocolo.bind(observacaoRegistro, "observacaoRegistro");
        binderProtocolo.setBean(this.protocolo);
    }

    private void gerarChecklist() {
        if(this.protocolo.getNatureza() != null){
            List<ChecklistGrupo> grupos = checklistService.findBySubNatureza(this.protocolo.getSubNatureza().getId());
            for(ChecklistGrupo grupo : grupos){
                Label label = new Label(grupo.getNome());
                label.addStyleName("margin large blue-grey800 light");
                panelChecklist.addComponent(label);
                for(ChecklistItem item : grupo.getItens()){
                    for(ChecklistProtocolo itemProtocolo : this.protocolo.getChecklist()){
                        if(itemProtocolo.getChecklistItem().getId().equals(item.getId())){
                            item.setCheckado(true);
                            break;
                        }
                    }
                    panelChecklist.addComponent(new ChecklistItemComponentImpl(item));
                }
            }
        }
    }

    @Override
    public void habilitarForm(boolean habilitar, boolean icProtocoloEditavel) {
        if(!icProtocoloEditavel){
            habilitar = icProtocoloEditavel;
        }
        panelChecklist.setEnabled(habilitar);
        observacaoRegistro.setEnabled(habilitar);
        situacaoAtualRegistro.setEnabled(habilitar);
        btnRegistrar.setEnabled(habilitar);
        btnDevolver.setEnabled(habilitar);
    }

    @Override
    public void cancelar() {

    }

    @Override
    public void focus() {

    }


    private List<ChecklistItem> getNaoMarcados(){
        List<ChecklistItem> retorno = new ArrayList<>();
        panelChecklist.iterator().forEachRemaining(item ->{
            if(item instanceof ChecklistItemComponentImpl) {
                if(!((ChecklistItemComponentImpl)item).isChecked()){
                    retorno.add(((ChecklistItemComponentImpl)item).getChecklistItem());
                }
            }
        });
        return  retorno;
    }
    @Override
    public void validate() throws Exception {
        if(this.protocolo.getChecklist() == null){
            this.protocolo.setChecklist(new HashSet<>());
        }else{
            this.protocolo.getChecklist().removeAll(this.protocolo.getChecklist());
        }
        panelChecklist.iterator().forEachRemaining(item ->{
            if(item instanceof ChecklistItemComponentImpl) {
                if(((ChecklistItemComponentImpl)item).isChecked()){
                    this.protocolo.getChecklist().add(new ChecklistProtocolo(((ChecklistItemComponentImpl)item).getChecklistItem(), this.protocolo.getId()));
                }

            }
        });


    }

    @Override
    public void removerListener() {

    }

    @Override
    public void addListener() {

    }


}
