package br.com.exmart.indicadorRTDPJ.ui.custom;

import com.vaadin.server.Resource;
import com.vaadin.ui.*;

/**
 * Created by fabioebner on 11/01/17.
 */
public class MyMessageBox extends Window {

    private Button closeButton = new Button("Fechar");
    private Label mensagem = new Label("Teste");
    private HorizontalLayout barraBotoes = new HorizontalLayout();
    private Button btnFocus = null;

    public MyMessageBox(String caption, Resource icon, String message, Button focus, Button... buttons) {
        this.setCaption(caption);
        if(icon != null) {
            this.setIcon(icon);
        }
        VerticalLayout content = new VerticalLayout();
        content.setMargin(true);
        content.setHeight("100%");

        mensagem.setValue(message);

        mensagem.setWidth("100%");
        barraBotoes.setWidth("100%");
        closeButton.addClickListener(evt->fechar());
        closeButton.setStyleName("danger");

        barraBotoes.setSpacing(true);
        adicionarBotao(closeButton);
        for(Button btn : buttons){
            btn.addClickListener(evt->fechar());
            adicionarBotao(btn);
        }
        barraBotoes.setExpandRatio(barraBotoes.getComponent(0),1);

        content.addComponent(mensagem);
        content.addComponent(barraBotoes);
        content.setExpandRatio(mensagem,1);
        content.setExpandRatio(barraBotoes,0);
        this.setContent(content);
        if(focus != null){
            btnFocus = focus;
        }
    }

    private void adicionarBotao(Button botao){
        barraBotoes.addComponent(botao, 0);
        barraBotoes.setExpandRatio(botao,0);
        barraBotoes.setComponentAlignment(botao, Alignment.MIDDLE_RIGHT);
    }

    private void fechar() {
        this.close();
    }

    public void focusBotao(){
        this.btnFocus.focus();
    }
}
