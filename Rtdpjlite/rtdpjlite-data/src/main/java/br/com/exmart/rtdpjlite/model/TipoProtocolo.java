package br.com.exmart.rtdpjlite.model;

public enum TipoProtocolo {
	EXAMECALCULO_TD(10),
	PRENOTACAO_TD(20),
	CERTIDAO_TD(30),
	EXAMECALCULO_PJ(40),
	PRENOTACAO_PJ(50),
	CERTIDAO_PJ(60)
	;
	Integer id;

	TipoProtocolo(Integer id) {
		this.id = id;
	}

	public static boolean isExameCalculo(TipoProtocolo tipo) {
		if(tipo.equals(TipoProtocolo.EXAMECALCULO_PJ) || tipo.equals(TipoProtocolo.EXAMECALCULO_TD)){
			return true;
		}
		return false;
	}

	public static boolean isPrenotacao(TipoProtocolo tipo) {
		if(tipo.equals(TipoProtocolo.PRENOTACAO_PJ) || tipo.equals(TipoProtocolo.PRENOTACAO_TD)){
			return true;
		}
		return false;
	}

	public static boolean isPj(TipoProtocolo tipo) {
		if(tipo.equals(TipoProtocolo.PRENOTACAO_PJ) || tipo.equals(TipoProtocolo.EXAMECALCULO_PJ)|| tipo.equals(TipoProtocolo.CERTIDAO_PJ)){
			return true;
		}
		return false;
	}

    public Integer getId() {
		return id;
	}

	public static TipoProtocolo fromCode(Integer code) {
		for (TipoProtocolo status :TipoProtocolo.values()){
			if (status.getId().equals(code)){
				return status;
			}
		}
		throw new UnsupportedOperationException(
				"The code " + code + " is not supported!");
	}
	public static String recuperaEspecialidade(TipoProtocolo tipo){
		if(tipo.equals(TipoProtocolo.EXAMECALCULO_PJ) || tipo.equals(TipoProtocolo.PRENOTACAO_PJ) || tipo.equals(TipoProtocolo.CERTIDAO_PJ)){
			return "PJ";
		}else{
			return "TD";
		}
	}

	public static TipoProtocolo recuperaTipoPrenotacaoByEspecialidade(String especialidade){
		if(especialidade.equalsIgnoreCase("PJ")){
			return TipoProtocolo.PRENOTACAO_PJ;
		}else if(especialidade.equalsIgnoreCase("TD")){
			return TipoProtocolo.PRENOTACAO_TD;
		}else{
			return null;
		}
	}
	public static TipoProtocolo recuperaTipoCertidaoByEspecialidade(String especialidade){
		if(especialidade.equalsIgnoreCase("PJ")){
			return TipoProtocolo.CERTIDAO_PJ;
		}else if(especialidade.equalsIgnoreCase("TD")){
			return TipoProtocolo.CERTIDAO_TD;
		}else{
			return null;
		}
	}

	public static String getTipoProtocolo(TipoProtocolo tipo){
		if (tipo.equals(TipoProtocolo.EXAMECALCULO_TD)) {
			return "TDE";
		}
		else if (tipo.equals(TipoProtocolo.EXAMECALCULO_PJ)) {
			return "PJE";
		}
		else if (tipo.equals(TipoProtocolo.CERTIDAO_TD)) {
			return "TDC";
		}
		else if (tipo.equals(TipoProtocolo.CERTIDAO_PJ)) {
			return "PJC";
		}
		else if (tipo.equals(TipoProtocolo.PRENOTACAO_TD)) {
			return "TDP";
		}
		else if (tipo.equals(TipoProtocolo.PRENOTACAO_PJ)) {
			return "PJP";
		}
		else {
			return "";
		}
	}

    public static boolean isCertidao(TipoProtocolo tipo) {
		if(tipo.equals(TipoProtocolo.CERTIDAO_PJ) || tipo.equals(TipoProtocolo.CERTIDAO_TD)){
			return true;
		}
		return false;
    }
}
