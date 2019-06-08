package br.com.exmart.indicadorRTDPJ.ui.impl;

import br.com.exmart.indicadorRTDPJ.ui.CartorioParceiro;
import br.com.exmart.indicadorRTDPJ.ui.custom.event.ProtocoloEvent;
import br.com.exmart.indicadorRTDPJ.ui.util.GerenciarJanela;
import br.com.exmart.rtdpjlite.model.*;
import br.com.exmart.rtdpjlite.service.CartorioParceiroService;
import br.com.exmart.rtdpjlite.service.EstadoService;
import br.com.exmart.rtdpjlite.vo.financeiro.ServicoCalculado;
import br.com.exmart.util.BeanLocator;
import com.vaadin.data.HasValue;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.MessageBox;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CartorioParceiroImpl extends CartorioParceiro implements PassoProtocolo{

    CartorioParceiroService cartorioParceiroService;

    Protocolo protocolo;
    public CartorioParceiroImpl(Protocolo protocolo) {
        this.protocolo = protocolo;

        estado.setItems(BeanLocator.find(EstadoService.class).findAll());
        estado.addValueChangeListener(evt->listarCidade(evt));
        cidade.addValueChangeListener(evt->listarCartorios(evt));
        btnAddCartorio.addClickListener(evt-> addCartorioListener());
        this.cartorioParceiroService = BeanLocator.find(CartorioParceiroService.class);
        btnConvidarCartorio.addClickListener(evt -> adicionarNovoCartorioParceiro());
        comboCartorioParceiro.setItemCaptionGenerator(br.com.exmart.rtdpjlite.model.CartorioParceiro::getAlias);
        comboCartorioParceiro.setEmptySelectionAllowed(true);
        gridCartorioParceiro.setItems(this.protocolo.getCartorioParceiroList());
        atualizarCartorioParceiros();
        gridCartorioParceiro.getColumn("cartorioParceiro").setCaption("Cartório Parceiro");
        gridCartorioParceiro.addComponentColumn(servico -> {
            Button button = new Button(VaadinIcons.TRASH);
            button.addStyleName("borderless");
            button.addClickListener(click -> {
                this.protocolo.getCartorioParceiroList().remove(servico);
                this.gridCartorioParceiro.getDataProvider().refreshAll();
            });
//					Notification.show("Clicked: " + servico.toString(), Notification.Type.TRAY_NOTIFICATION));
            return button;
        }).setWidth(100);
    }

    private void adicionarNovoCartorioParceiro() {
        limparCampos();
        GerenciarJanela.abrirJanela(null, 80, 90, new CadastroCartorioParceiroImpl(), null, false);
    }

    private void limparCampos() {
        estado.clear();
        comboCartorioParceiro.setItems(new ArrayList<>());
        cidade.setItems(new ArrayList<>());
        comboCartorioParceiro.getDataProvider().refreshAll();
        cidade.getDataProvider().refreshAll();
    }

    private void listarCartorios(HasValue.ValueChangeEvent<Cidade> evt) {
        comboCartorioParceiro.setItems(new ArrayList<>());
        if(evt.getValue() !=null){
            comboCartorioParceiro.setItems(cartorioParceiroService.findByCidade(evt.getValue()));
        }
        comboCartorioParceiro.getDataProvider().refreshAll();
    }

    private void listarCidade(HasValue.ValueChangeEvent<Estado> evt) {
        cidade.setItems(new ArrayList<>());
        if(evt.getValue() != null){
            cidade.setItems(evt.getValue().getCidades());
        }
        cidade.getDataProvider().refreshAll();
    }

    private void addCartorioListener() {
        if(comboCartorioParceiro.getValue() != null){
            ProtocoloCartorioParceiro novo = new ProtocoloCartorioParceiro();
            novo.setCartorioParceiro(comboCartorioParceiro.getValue());
            this.protocolo.getCartorioParceiroList().add(novo);
            gridCartorioParceiro.getDataProvider().refreshAll();
            comboCartorioParceiro.clear();
        }
    }

    private void atualizarCartorioParceiros() {
        comboCartorioParceiro.clear();
        comboCartorioParceiro.setItems((s, s1) -> {return s.toUpperCase().startsWith(s1.toUpperCase());},cartorioParceiroService.findAll());
    }



    private void verificarFormaEnvio(HasValue.ValueChangeEvent<FormaEntrega> evt) {
        if(evt != null){
            if(evt.getValue() != null){
                if(evt.getValue().getNome().startsWith("ENCAMINHAR")){
                    panelAddCartorio.setVisible(true);
                }else{
                    panelAddCartorio.setVisible(false);
                }
            }
        }
    }

    @Override
    public void habilitarForm(boolean habilitar, boolean icProtocoloEditavel) {
        if(!icProtocoloEditavel){
            habilitar = icProtocoloEditavel;
        }
        comboCartorioParceiro.setEnabled(habilitar);
        cidade.setEnabled(habilitar);
        estado.setEnabled(habilitar);
        btnAddCartorio.setEnabled(habilitar);
        btnConvidarCartorio.setEnabled(habilitar);
        gridCartorioParceiro.getColumns().get(gridCartorioParceiro.getColumns().size()-1).setHidden(!habilitar);
    }

    @Override
    public void cancelar() {

    }

    @Override
    public void focus() {
        estado.focus();
    }

    @Override
    public void validate() throws Exception {

    }

    @Override
    public void removerListener() {

    }

    @Override
    public void addListener() {

    }
}
