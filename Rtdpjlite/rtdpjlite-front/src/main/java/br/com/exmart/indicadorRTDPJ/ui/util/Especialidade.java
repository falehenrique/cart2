package br.com.exmart.indicadorRTDPJ.ui.util;

public enum Especialidade {
    TD("TD","mytheme"),
    PJ("PJ","mytheme");

    private String especialidade;
    private String tema;

    Especialidade(String especialidade, String tema) {
        this.especialidade = especialidade;
        this.tema = tema;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public String getTema() {
        return tema;
    }
}
