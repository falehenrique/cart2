package br.com.exmart.rtdpjlite.vo.indisponibilidade;

public class ParteIndisponivelRegistro {
    private Long numeroRegistro;
    private String registro;
    private String nrPastaPj;
    private Long id;

    public ParteIndisponivelRegistro( Long numeroRegistro, String registro, String nrPastaPj, Long id) {
        this.numeroRegistro = numeroRegistro;
        this.registro = registro;
        this.nrPastaPj = nrPastaPj;
        this.id = id;
    }


    public Long getNumeroRegistro() {
        return numeroRegistro;
    }

    public void setNumeroRegistro(Long numeroRegistro) {
        this.numeroRegistro = numeroRegistro;
    }

    public String getRegistro() {
        return registro;
    }

    public void setRegistro(String registro) {
        this.registro = registro;
    }

    public String getNrPastaPj() {
        return nrPastaPj;
    }

    public void setNrPastaPj(String nrPastaPj) {
        this.nrPastaPj = nrPastaPj;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof  ParteIndisponivelRegistro){
            ParteIndisponivelRegistro registro = (ParteIndisponivelRegistro) obj;
            return registro.getId().equals(this.getId());
        }
        return false;
    }
}
