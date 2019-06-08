package br.com.exmart.indicadorRTDPJ.ui.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.appreciated.app.layout.annotations.MenuCaption;
import com.github.appreciated.app.layout.annotations.MenuIcon;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Notification;

import br.com.exmart.indicadorRTDPJ.ui.ViewCadastroCliente;
import br.com.exmart.indicadorRTDPJ.ui.util.GerenciarJanela;
import br.com.exmart.rtdpjlite.model.Cliente;
import br.com.exmart.rtdpjlite.model.portal.UsuarioPortal;
import br.com.exmart.rtdpjlite.service.ClienteService;
import br.com.exmart.rtdpjlite.service.UsuarioPortalService;

@SpringView(name = "clientes")
@UIScope
@MenuCaption("Cadastro Cliente")
@MenuIcon(VaadinIcons.STORAGE)
public class ViewCadastroClienteImpl extends ViewCadastroCliente implements View {

    private ClienteService clienteService;
    private UsuarioPortalService usuarioPortalService;
    private Cliente clienteSelecionado;

    @Autowired
    public ViewCadastroClienteImpl(ClienteService clienteService, UsuarioPortalService usuarioPortalService) {
        this.clienteService = clienteService;
        this.usuarioPortalService = usuarioPortalService;
        comboCliente.setItems(clienteService.findAll());
        
        comboCliente.addValueChangeListener(evt-> listarResponsavel(evt.getValue()));

        gridResponsavel.removeAllColumns();
        gridResponsavel.addColumn("nome").setCaption("Nome");
        gridResponsavel.addColumn("email").setCaption("Email");
        gridResponsavel.addColumn("lembreteSenha").setCaption("Lembrete");
        btnNovoResponsavel.addClickListener(evt-> novoUsuario());
    }

    private void novoUsuario() {
        if(this.clienteSelecionado != null) {
            UsuarioPortal novo = new UsuarioPortal();
            novo.setCliente(this.clienteSelecionado);
            GerenciarJanela.abrirJanela(null, 50, 50, new UsuarioPortalCadastroImpl(novo), closeEvent -> {listarResponsavel(this.clienteSelecionado);}, false);
        }else {
            Notification.show("Atenção","Favor seleciona um cliente", Notification.Type.ERROR_MESSAGE);
        }
    }

    private void listarResponsavel(Cliente cliente) {
        gridResponsavel.setItems(new ArrayList<>());
        this.clienteSelecionado = null;
        if(cliente != null){
            this.clienteSelecionado = cliente;
            gridResponsavel.setItems(usuarioPortalService.findByCliente(cliente));
        }
        gridResponsavel.getDataProvider().refreshAll();
    }
}
