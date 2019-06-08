package br.com.exmart.indicadorRTDPJ.ui.custom.event;

import br.com.exmart.indicadorRTDPJ.ui.util.Especialidade;

import java.io.Serializable;

public class EspecialidadeEvent implements Serializable {

    private final Especialidade especialidade;
    public EspecialidadeEvent(Especialidade especialidade) {
        this.especialidade = especialidade;
    }

    public Especialidade getEspecialidade() {
        return especialidade;
    }
}
