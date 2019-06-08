package br.com.exmart.indicadorRTDPJ.ui.vo;

import com.github.appreciated.app.layout.builder.entities.DefaultNotification;
import com.vaadin.server.Resource;

public class RTDPJNotificacao extends DefaultNotification {
    private Long notificacaoId;



    public RTDPJNotificacao(String title, String description, Long notificacaoId) {
        super(title, description);
        this.notificacaoId = notificacaoId;
    }

    public RTDPJNotificacao(String title, String description, Resource image,Long notificacaoId) {
        super(title, description, image);
        this.notificacaoId = notificacaoId;
    }

    public RTDPJNotificacao(String title, String description, boolean dismissible,Long notificacaoId) {
        super(title, description, dismissible);
        this.notificacaoId = notificacaoId;
    }

    public RTDPJNotificacao(String title, String description, Priority priority,Long notificacaoId) {
        super(title, description, priority);
        this.notificacaoId = notificacaoId;
    }

    public RTDPJNotificacao(String title, String description, Priority priority, boolean dismissible,Long notificacaoId) {
        super(title, description, priority, dismissible);
        this.notificacaoId = notificacaoId;
    }

    public RTDPJNotificacao(String title, String description, Resource image, Priority priority, boolean dismissible,Long notificacaoId) {
        super(title, description, image, priority, dismissible);
        this.notificacaoId = notificacaoId;
    }

    public Long getNotificacaoId() {
        return notificacaoId;
    }
}
