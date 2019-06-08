package br.com.exmart.indicadorRTDPJ.ui.component;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;

public class AlertaButton extends Button {

    Tipos tipo;
    public AlertaButton(Tipos tipo) {
        this.tipo=tipo;
        this.addClickListener(evt -> clickEventListener(evt));
        mostrarAlertas();

    }

    public void mostrarAlertas() {
        configurarBotao(tipo.getCaption(), tipo.getIcon(), tipo.getStyleName());
    }

    private void configurarBotao(String caption, Resource icon, String styleName){
        this.setCaption(caption);
        this.setIcon(icon);
        this.setStyleName(styleName);
    }

    private void clickEventListener(ClickEvent evt) {


    }

//    public void esconderAlertas() {
//
//        configurarBotao(null, null, tipo.getStyleName());
//    }


    public enum Tipos{
        INDISPONIBILIDADE("alertaIndisponibilidade", VaadinIcons.INFO_CIRCLE,"Indisponibilidade"),
        IMAGEM_INEXISTENTE("alertaImagem", VaadinIcons.FILE_REMOVE,"Imagem inexistente"),
        CONTRADITORIO("alertaContraditorio", VaadinIcons.EXCLAMATION,"Contraditório");

        private final String caption;
        private final String styleName;
        private final Resource icon;

        Tipos(String styleName, Resource icon, String caption) {
            this.styleName = styleName;
            this.icon = icon;
            this.caption = caption;
        }

        public String getStyleName() {
            return styleName;
        }

        public Resource getIcon() {
            return icon;
        }

        public String getCaption() {
            return caption;
        }

    }
}
