package br.com.exmart.indicadorRTDPJ.ui.impl;

import java.io.IOException;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Window;

import br.com.exmart.indicadorRTDPJ.ui.UsuarioPortalCadastro;
import br.com.exmart.rtdpjlite.model.portal.UsuarioPortal;
import br.com.exmart.rtdpjlite.service.UsuarioPortalService;
import br.com.exmart.util.BeanLocator;
import freemarker.template.TemplateException;

public class UsuarioPortalCadastroImpl extends UsuarioPortalCadastro {
    private BeanValidationBinder<UsuarioPortal> binder = new BeanValidationBinder<>(UsuarioPortal.class);
    private Window window;
    public UsuarioPortalCadastroImpl(UsuarioPortal usuarioPortal) {
        binder.bind(nome,"nome");
        binder.bind(email, "email");
        binder.setBean(usuarioPortal);

        btnCancelar.addClickListener(clickEvent -> {this.window.close();});
        btnSalvar.addClickListener(evt->salvar());
    }

    private void salvar() {
        BinderValidationStatus<UsuarioPortal> validacao = this.binder.validate();
        if(validacao.hasErrors()){
            Notification.show("Atenção","Ops ;) Acho que faltou preencher algum campo", Notification.Type.ERROR_MESSAGE);
        }else{
            try {
            	BeanLocator.find(UsuarioPortalService.class).novo(this.binder.getBean());
                this.window.close();
            } catch (IOException e) {
                Notification.show("Atenção",e.getMessage(), Notification.Type.ERROR_MESSAGE);
                e.printStackTrace();
            } catch (TemplateException e) {
                Notification.show("Atenção",e.getMessage(), Notification.Type.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}
