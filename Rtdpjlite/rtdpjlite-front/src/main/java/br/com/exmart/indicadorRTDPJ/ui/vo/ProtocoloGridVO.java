package br.com.exmart.indicadorRTDPJ.ui.vo;

import java.util.Date;

public class ProtocoloGridVO {
	
	private Long protocolo;
	private String natureza;
	private Date vencimento;
	private String status;
	private String especialidade;
	private String parte;
	private Long cpfParte;
	private Long saldo;
//	private String protIcon;
//	private Button btnIcon;
	
	
	
	public ProtocoloGridVO(Long protocolo, String natureza, Date vencimento, String status, String especialidade, String parte, Long cpfParte, Long saldo) {
		super();
		this.protocolo = protocolo;
		this.natureza = natureza;
		this.vencimento = vencimento;
		this.status = status;
		this.especialidade = especialidade;
		this.parte = parte;
		this.cpfParte = cpfParte;
		this.saldo = saldo;
//		this.protIcon = protIcon;
//		this.btnIcon = btnIcon;
	}
	
	public Long getProtocolo() {
		return protocolo;
	}
	public void setProtocolo(Long protocolo) {
		this.protocolo = protocolo;
	}
	public String getNatureza() {
		return natureza;
	}
	public void setNatureza(String natureza) {
		this.natureza = natureza;
	}
	public Date getVencimento() {
		return vencimento;
	}
	public void setVencimento(Date vencimento) {
		this.vencimento = vencimento;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getEspecialidade() {
		return especialidade;
	}
	public void setEspecialidade(String especialidade) {
		this.especialidade = especialidade;
	}
	public String getParte() {
		return parte;
	}
	public void setParte(String parte) {
		this.parte = parte;
	}
	public Long getCpfParte() {
		return cpfParte;
	}
	public void setCpfParte(Long cpfParte) {
		this.cpfParte = cpfParte;
	}
	public Long getValorDoc() {
		return saldo;
	}
	public void setValorDoc(Long saldo) {
		this.saldo = saldo;
	}
//	public String getProtIcon() {
//		return protIcon;
//	}
//	public void setProtIcon(String protIcon) {
//		this.protIcon = protIcon;
//	}
//	public Button getBtnIcon() {
//		return btnIcon;
//	}
//	public void setBtnIcon(Button btnIcon) {
//		this.btnIcon = btnIcon;
//	}
	
}
