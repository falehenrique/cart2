package br.com.exmart.rtdpjlite.repository;

import br.com.exmart.rtdpjlite.model.*;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.util.Date;
import java.util.List;
import br.com.exmart.rtdpjlite.model.ProtocoloGrid;

@Repository
public interface ProtocoloRepository extends JpaRepository<Protocolo, Long>, JpaSpecificationExecutor<Protocolo>{
	
//	@Query(value="select * from protocolo where numero=? limit 1", nativeQuery=true)
	Protocolo findByNumero(Long numero);


	@Query(value="select * from rtdpj.protocolo where numero=?", nativeQuery=true)
	List<Protocolo> findAll(Integer protocolo);
	@EntityGraph(value = "Protocolo.tudo", type = EntityGraph.EntityGraphType.LOAD)
	@Transactional(readOnly=true)
	List<Protocolo> findByNumeroAndTipoIn(Long protocolo, List<TipoProtocolo> tipoProtocolos);

	@EntityGraph(value = "Protocolo.tudo", type = EntityGraph.EntityGraphType.LOAD)
	@Transactional(readOnly=true)
	Protocolo findByNumeroAndTipo(Long protocolo, TipoProtocolo tipoProtocolo);

	@Query(value="select * from rtdpj.protocolo order by data_protocolo desc limit ?2 offset ?1", nativeQuery=true)
	List<Protocolo> findAll(int offset, int limit);
	

	@Query(value="select * from rtdpj.protocolo WHERE data_protocolo between ?3 and ?4 and (cast(numero as varchar) = ?5 or apresentante = ?5) and tipo_protocolo_id in (?6) order by data_protocolo desc limit ?2 offset ?1", nativeQuery=true)
	List<Protocolo> findAll(int offset, int limit, Date dtInicial, Date dtFinal, String filter, List<Integer> tipoProtocolos);
	@Query(value="select * from rtdpj.protocolo WHERE data_protocolo between ?3 and ?4 and tipo_protocolo_id in (?5) order by data_protocolo desc limit ?2 offset ?1", nativeQuery=true)
	List<Protocolo> findAll(int offset, int limit, Date dtInicial, Date dtFinal, List<Integer> tipoProtocolos);

	@Query(value="select count(*) from rtdpj.protocolo WHERE data_protocolo between ?1 and ?2 and (cast(numero as varchar) = ?3 or apresentante = ?3) and tipo_protocolo_id in (?4) ", nativeQuery=true)
	Integer countFindAll(Date dtInicial, Date dtFinal, String filter, List<Integer> tipoProtocolos);
	@Query(value="select count(*) from rtdpj.protocolo WHERE data_protocolo between ?1 and ?2 and tipo_protocolo_id in (?3)", nativeQuery=true)
	Integer countFindAll(Date dtInicial, Date dtFinal, List<Integer> tipoProtocolos);

//	@Query(value="select * from protocolo where objetos @> '[{\"atributos\":[{\"nome\": \"RENAVAM\"}]}]' and objetos @> '[{\"atributos\":[{\"valor\": \"?1\"]}]';", nativeQuery=true)
//	List<Protocolo> findByRenavam ( String vlr);

//		List<Protocolo> r = getRepository().findByObjeto("[{\"codObjeto\":\"1\", \"nomeCampo\": \"RENAVAM\", \"valorCampo\": \"12345\"},{\"codObjeto\":\"3\", \"nomeCampo\": \"TIPO\", \"valorCampo\": \"Boi\"}]");
	@Query(value="select * from rtdpj.findProtocoloByObjeto(cast (?1 as json));", nativeQuery=true)
	List<Protocolo> findByObjeto ( String gson);



    Protocolo findByNumeroRegistroAndTipo(String registroReferencia, TipoProtocolo tipoProtocolo);

    @Query(value = "SELECT numero FROM rtdpj.protocolo WHERE nr_exame_calculo_referencia =?1 and tipo_protocolo_id = ?2", nativeQuery = true)
    Long findProtocoloByExameCalculo(Long nrExameCalculo, Integer protocoloExame);


	@Modifying
	@Query(value = "update rtdpj.protocolo  set texto_carimbo = ?2 where id = ?1", nativeQuery = true)
	void atualizarTextoCarimbo(Long id, String textoCarimbo);

	@Modifying
	@Query(value = "update rtdpj.protocolo set texto_certidao = ?2 where id = ?1", nativeQuery = true)
	void atualizarTextoCertidao(Long id, String textoCertidao);

	@EntityGraph(value = "Protocolo.tudo", type = EntityGraph.EntityGraphType.LOAD)
	Page<Protocolo> findByCliente(Cliente idCliente, Pageable pagina);

	@EntityGraph(value = "Protocolo.tudo", type = EntityGraph.EntityGraphType.LOAD)
	Protocolo findById(Long Id);

	@EntityGraph(value = "Protocolo.tudo", type = EntityGraph.EntityGraphType.LOAD)
	List<Protocolo> findByTipo(TipoProtocolo tipo);

	@Override
	Page<Protocolo> findAll(Specification<Protocolo> specification, Pageable pageable);

	@Procedure(procedureName = "rtdpj.reverter_protocolo")
    Integer reverterProtocolo(Integer numeroProtocolo, Integer id);

	@Query(nativeQuery = true, value = "SELECT numero FROM rtdpj.protocolo WHERE id = ?1")
    Long findNumeroById(Long protocolo);

	@Query(nativeQuery =  true , value =  "SELECT count(*) FROM rtdpj.protocolo_cartorio_parceiro WHERE protocolo_id = ?1")
    int countCartorioParceirosById(Long protocoloId);

	@Query(nativeQuery = true, value =
			"SELECT * FROM rtdpj.protocolo p " +
			"WHERE (SELECT sp.status_id FROM rtdpj.status_protocolo sp WHERE sp.protocolo_id = p.id " +
			"ORDER BY data desc LIMIT 1) = ?1 AND dt_registro is null")
    List<Protocolo> findByLastStatus(Long id);

    Protocolo findByNumeroAndPedido(Long numeroProtocolo, Long pedidoId);
}
