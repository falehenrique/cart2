package br.com.exmart.rtdpjlite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.exmart.rtdpjlite.model.NumeroProtocolo;

@Repository
public interface GeradorProtocoloRepository extends JpaRepository<NumeroProtocolo,Long> {
	@Query(value="select 1 as id, nextval('rtdpj.protocolo_exame_td') as numero",nativeQuery=true)
	NumeroProtocolo getNumeroProtocoloExameTD();

	@Query(value="select 1 as id, nextval('rtdpj.protocolo_exame_pj') as numero",nativeQuery=true)
	NumeroProtocolo getNumeroProtocoloExamePJ();

	@Query(value="select 1 as id, nextval('rtdpj.protocolo_prenotacao_td') as numero",nativeQuery=true)
	NumeroProtocolo getNumeroProtocoloPrenotacaoTD();

	@Query(value="select 1 as id, nextval('rtdpj.protocolo_prenotacao_pj') as numero",nativeQuery=true)
	NumeroProtocolo getNumeroProtocoloPrenotacaoPJ();

	@Query(value="select 1 as id, nextval('rtdpj.protocolo_certidao_td') as numero",nativeQuery=true)
	NumeroProtocolo getNumeroProtocoloCertidaoTD();

	@Query(value="select 1 as id, nextval('rtdpj.protocolo_certidao_td') as numero",nativeQuery=true)
	NumeroProtocolo getNumeroProtocoloCertidaoPJ();
}
