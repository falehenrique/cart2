package br.com.exmart.rtdpjlite.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.joda.money.Money;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.exmart.rtdpjlite.model.custom.JsonObjetoProtocoloUserTypeSingle;
import br.com.exmart.rtdpjlite.model.custom.JsonParteVOUserTypeSingle;
import br.com.exmart.rtdpjlite.vo.ObjetoProtocoloVO;
import br.com.exmart.rtdpjlite.vo.ParteVO;

@Entity
@TypeDefs({@TypeDef(name = "JsonParteVOUserTypeSingle", typeClass = JsonParteVOUserTypeSingle.class),
        @TypeDef(name = "JsonObjetoProtocoloUserTypeSingle", typeClass = JsonObjetoProtocoloUserTypeSingle.class)})
@Table(schema = "rtdpj")
public class IndicadorPessoalVO {
    @Id
    @Column
    private Long id;
    @Column(name = "dt_registro")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDateTime dtRegistro;
    @Column(name = "numero_registro")
    private String numeroRegistro;
    @Column(name = "nome")
    private String subnatureza;
    @Column(name = "pasta")
    private String pasta;
    @Column(name = "registro")
    private String registro;
    @Column(name = "tp_especialidade")
    private String especialidade;
    
    @Column(name = "protocolo")
    private Long protocolo;

    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyAmount",
	   	  parameters = {@org.hibernate.annotations.Parameter(name = "currencyCode", value = "BRL")})
    @Column(name="valor")
    private Money valor;
    
    @Column(name="nr_contrato")
    private String numeroContrato;
    
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(name="data_doc")
    private LocalDateTime dataContrato;
    
    
    @Type(type = "JsonObjetoProtocoloUserTypeSingle")
    private ObjetoProtocoloVO objetos;
    @Type(type = "JsonParteVOUserTypeSingle")
    private ParteVO parte;

    @Column(name="observacao")
    private String observacao;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDtRegistro() {
        return dtRegistro;
    }

    public void setDtRegistro(LocalDateTime dtRegistro) {
        this.dtRegistro = dtRegistro;
    }

    public String getNumeroRegistro() {
        return numeroRegistro;
    }

    public void setNumeroRegistro(String numeroRegistro) {
        this.numeroRegistro = numeroRegistro;
    }

    public String getSubnatureza() {
        return subnatureza;
    }

    public void setSubnatureza(String subnatureza) {
        this.subnatureza = subnatureza;
    }

    public String getPasta() {
        return pasta;
    }

    public void setPasta(String pasta) {
        this.pasta = pasta;
    }

    public ObjetoProtocoloVO getObjetos() {
        return objetos;
    }
    
    
	public Money getValor() {
		return valor;
	}

	public void setValor(Money valor) {
		this.valor = valor;
	}

	public String getNumeroContrato() {
		return numeroContrato;
	}

	public void setNumeroContrato(String numeroContrato) {
		this.numeroContrato = numeroContrato;
	}

	public LocalDateTime getDataContrato() {
		return dataContrato;
	}

	public void setDataContrato(LocalDateTime dataContrato) {
		this.dataContrato = dataContrato;
	}

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getNomeObjeto(){
        if(objetos != null){
            return objetos.getNome();
        }else{
            return " - ";
        }
    }

    public void setObjetos(ObjetoProtocoloVO objetos) {
        this.objetos = objetos;
    }

    public ParteVO getParte() {
        return parte;
    }

    public void setParte(ParteVO parte) {
        this.parte = parte;
    }

    public String getAtributo1() {
        if(objetos == null) {
            return "";
        }else{
            try {
                return objetos.getAtributos().get(0).getNome() + " - <strong>" + objetos.getAtributos().get(0).getValor() + "</strong>";
            }catch (Exception e){
                return "";
            }
        }
    }
    public String getAtributo2() {
        if(objetos == null) {
            return "";
        }else{
            try {
                return objetos.getAtributos().get(1).getNome() + " - <strong>" + objetos.getAtributos().get(1).getValor()+ "</strong>";
            }catch (Exception e){
                return "";
            }
        }
    }
    public String getAtributo3() {
        if(objetos == null) {
            return "";
        }else{
            try {
                return objetos.getAtributos().get(2).getNome() + " - <strong>" + objetos.getAtributos().get(2).getValor()+ "</strong>";
            }catch (Exception e){
                return "";
            }
        }
    }

    public String getRegistro() {
        return registro;
    }

    public void setRegistro(String registro) {
        this.registro = registro;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

	public Long getProtocolo() {
		return protocolo;
	}

	public void setProtocolo(Long protocolo) {
		this.protocolo = protocolo;
	}

    
}
