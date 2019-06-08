package br.com.exmart.rtdpjlite.repository;

import br.com.exmart.rtdpjlite.model.Cliente;
import br.com.exmart.rtdpjlite.model.Registro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;

import java.util.List;

public interface RegistroRepository extends JpaRepository<Registro, Long> {

    Registro findByRegistroAndEspecialidade(String registro, String especialidade);

    @Query(value = "SELECT MAX(numero_registro) FROM rtdpj.registro WHERE tp_especialidade = ?1", nativeQuery = true)
    Long findLastRegistro(String especialidade);

    Registro findByProtocolo(Long numeroProtocolo);

    Registro findByProtocoloAndEspecialidade(Long protocolo, String especialidade);

    @Query(nativeQuery = true, value = "SELECT r.* FROM rtdpj.registro r JOIN rtdpj.indicador_pessoal ip ON ip.registro_id = r.id WHERE r.tp_especialidade IN (?4) AND (ip.nome ilike ?1 or replace(replace(replace(ip.cpf_cnpj,'/',''),'-',''), '.', '') ilike ?2) UNION SELECT r2.* FROM registro r2 WHERE r2.numero_registro = ?3 and r2.tp_especialidade IN (?4)")
    List<Registro> findByIndicadorPessoalNomeOrDocumentoAndEspecialidadeIn(String nome, String documento, Long numeroRegistro, List<String> especialidade);

    //		List<Protocolo> r = getRepository().findByObjeto("[{\"codObjeto\":\"1\", \"nomeCampo\": \"RENAVAM\", \"valorCampo\": \"12345\"},{\"codObjeto\":\"3\", \"nomeCampo\": \"TIPO\", \"valorCampo\": \"Boi\"}]");
    @Query(value="select * from rtdpj.findRegistroByObjeto(cast (?1 as json));", nativeQuery=true)
    List<Registro> findByObjeto (String gson);

    Page<Registro> findByCliente(Cliente cliente, Pageable pagina);

    Registro findByRegistro(String registroReferencia);

    List<Registro> findByNumeroRegistroAndEspecialidade(Long numeroRegistro, String especialidade);

    @Query(nativeQuery = true, value = "SELECT r.* FROM rtdpj.registro r JOIN rtdpj.indicador_pessoal ip ON ip.registro_id = r.id WHERE r.tp_especialidade = ?2 AND ip.cpf_cnpj = ?1 AND protocolo_id not IN ( ?3) AND registro not in (?4)")
    List<Registro> findByIndicadorPessoalDocumentoAndEspecialidadeIn( String documento, String especialidade, List<Long> protocolosEncontrados, List<String> registrosEncontrados);

    @Query(nativeQuery = true, value = "SELECT r.* FROM rtdpj.registro r JOIN rtdpj.indicador_pessoal ip ON ip.registro_id = r.id WHERE r.tp_especialidade = ?2 AND ip.nome_fonetico = ?1 and coalesce(ip.cpf_cnpj,'') = '' and registro not in (?3)")
    List<Registro> findByIndicadorPessoalNomeFoneticoAndEspecialidadeIn(String nomeFonetico, String natureza, List<String> registrosNegativos);

    Registro findById(Long id);

    @Procedure(procedureName = "rtdpj.reverter_registro")
    Integer reverterRegistro(String registro, String especialidade);

    @Query(nativeQuery = true, value = "SELECT MAX(numero_registro) FROM rtdpj.registro WHERE tp_especialidade = ?1")
    Long findMaxByEspecialidade(String especialidade);
}
