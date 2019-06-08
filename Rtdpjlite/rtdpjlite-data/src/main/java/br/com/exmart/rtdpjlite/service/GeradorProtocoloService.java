package br.com.exmart.rtdpjlite.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.exmart.rtdpjlite.model.TipoProtocolo;
import br.com.exmart.rtdpjlite.repository.GeradorProtocoloRepository;
import org.springframework.transaction.annotation.Isolation;

@Service
public class GeradorProtocoloService {
	@Autowired GeradorProtocoloRepository geradorProtocoloRepository;
	
	@Transactional(Transactional.TxType.NOT_SUPPORTED)
	public Long getNumero(TipoProtocolo tipoProtocolo) {
		//TODO Refatorar para o Enum ou Custom Generator no JPA ?
		switch(tipoProtocolo) {
		case EXAMECALCULO_TD :
			return geradorProtocoloRepository.getNumeroProtocoloExameTD().getNumero();
		case EXAMECALCULO_PJ :
			return geradorProtocoloRepository.getNumeroProtocoloExamePJ().getNumero();
		case PRENOTACAO_TD :
			return geradorProtocoloRepository.getNumeroProtocoloPrenotacaoTD().getNumero();
		case PRENOTACAO_PJ :
			return geradorProtocoloRepository.getNumeroProtocoloPrenotacaoPJ().getNumero();
		case CERTIDAO_TD :
			return geradorProtocoloRepository.getNumeroProtocoloCertidaoTD().getNumero();
		case CERTIDAO_PJ :
			return geradorProtocoloRepository.getNumeroProtocoloCertidaoPJ().getNumero();
		default:
			return null;
		}
	}
}
