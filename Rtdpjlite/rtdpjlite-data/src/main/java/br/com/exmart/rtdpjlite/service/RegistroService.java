package br.com.exmart.rtdpjlite.service;

import br.com.exmart.rtdpjlite.annotations.NotFoundException;
import br.com.exmart.rtdpjlite.model.*;
import br.com.exmart.rtdpjlite.repository.RegistroRepository;
import br.com.exmart.rtdpjlite.service.rest.fonetica.FoneticaService;
import br.com.exmart.rtdpjlite.util.ObjetoBuscaVO;
import br.com.exmart.rtdpjlite.vo.financeiro.ServicoCalculado;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RegistroService {

    @Autowired
    private ObjectMapper jacksonObjectMapper;
    @Autowired
    private RegistroRepository registroRepository;
    @Autowired
    private ConfiguracaoService configuracaoService;
    @Autowired
    private ArquivoService arquivoService;
    @Autowired
    private ProtocoloService protocoloService;

    public List<Registro> findByNumeroRegistroAndEspecialidade(Long numeroRegistro, TipoProtocolo tipoProtocolo) {
            return findByNumeroRegistroAndEspecialidade( numeroRegistro, TipoProtocolo.recuperaEspecialidade(tipoProtocolo));
    }
    public List<Registro> findByNumeroRegistroAndEspecialidade(Long numeroRegistro, String especialidade) {
            return registroRepository.findByNumeroRegistroAndEspecialidade(numeroRegistro, especialidade);

    }


    public Long findLastRegistro(String especialidade) {
        return this.registroRepository.findLastRegistro(especialidade);
    }

    public Registro save(Registro registro) {
        return this.registroRepository.save(registro);
    }

    public Registro findByProtocolo(Long numeroProtocolo) {
        return this.registroRepository.findByProtocolo(numeroProtocolo);
    }

    public Registro findByProtocoloIdAndEspecialidade(Long numero, String especialidade) {
        return this.registroRepository.findByProtocoloAndEspecialidade(numero, especialidade);
    }

    public List<Registro> findByIndicadorPessoalNomeOrDocumentoAndEspecialidadeIn(String nome, String documento, Long numeroRegistro, List<String> especialidade) {
        documento = documento.replace(".","").replace("-","").replace("/","");
        return this.registroRepository.findByIndicadorPessoalNomeOrDocumentoAndEspecialidadeIn("%"+nome+"%", "%"+documento+"%", numeroRegistro, especialidade);
    }

    public List<Registro> findByObjeto(List<ObjetoBuscaVO> objetoList){
        try {
            return this.registroRepository.findByObjeto(jacksonObjectMapper.writeValueAsString(objetoList));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();

    }

    public Page<Registro> findByClientePageable(Cliente cliente, Pageable pagina) {
        return this.registroRepository.findByCliente(cliente ,pagina);
    }


    public Registro findByRegistroAndEspecialidade(String registroReferencia, TipoProtocolo tipoProtocolo) {
//        if(tipoProtocolo.equals(TipoProtocolo.CERTIDAO_PJ)){
//            return findByRegistroAndEspecialidade(registroReferencia, "PJ");
//        }else{
            return findByRegistroAndEspecialidade( registroReferencia, TipoProtocolo.recuperaEspecialidade(tipoProtocolo));
//        }
    }

    public Registro findByRegistroAndEspecialidade(String registro, String especialidade) {
        return registroRepository.findByRegistroAndEspecialidade(registro, especialidade);

    }

    public String gerarRegistro(Long ultimoRegistro, LocalDateTime dataRegistro) {
        String nrServentia = String.format("%06d",Integer.parseInt(configuracaoService.findConfiguracao().getCodigoCnsCartorio()));
        return nrServentia+"R"+dataRegistro.getYear()+"B"+String.format("%09d",ultimoRegistro);

    }

    public InputStream recuperarArquivo(Registro registro) throws FileNotFoundException {
        return arquivoService.recuperarDocumentoProtocoloRegistroFileIS(registro.getRegistro(), registro.getNumeroRegistro(), registro.getEspecialidade(), null);
    }

    public List<Registro> findByIndicadorPessoalDocumentoAndEspecialidade(String documento, String natureza, List<Long> protocolosEncontrados, List<String> registrosEncontrados) {
        return registroRepository.findByIndicadorPessoalDocumentoAndEspecialidadeIn(documento, natureza,protocolosEncontrados, registrosEncontrados);
    }
    public List<Registro> findByIndicadorPessoalNomeFoneticoAndEspecialidadeIn(String nomeFonetico, String natureza, List<String> registrosNegativos) {
        return registroRepository.findByIndicadorPessoalNomeFoneticoAndEspecialidadeIn(nomeFonetico, natureza, registrosNegativos);
    }

    public Registro findById(Long id){
        return this.registroRepository.findById(id);
    }

    public Long findMaxByEspecialidade(String especialidade) {
        return registroRepository.findMaxByEspecialidade(especialidade);
    }


    @Autowired
    private ReverterService reverterService;
    @Autowired
    private FoneticaService foneticaService;
    protected static Logger logger = LoggerFactory.getLogger(RegistroService.class);
    @org.springframework.transaction.annotation.Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
    public synchronized  Registro gerarRegistro(Protocolo protocolo, List<ServicoCalculado> servicos, String especialidade, LocalDateTime dtRegistro) throws Exception{
        logger.debug("iniciou gerar Registro");
        Registro novoRegistro = new Registro();
        Long ultimoRegistro = this.findLastRegistro(especialidade);
        if (ultimoRegistro == null) {
            ultimoRegistro = 0L;
        }
        do{
            ultimoRegistro++;
        }while(reverterService.verificarRegistroRevertido(protocolo.getTipo().getId(), ultimoRegistro));

        Long registroRevertido = reverterService.findByProtocoloIdAndTipoProtocoloIdAndNumeroRegistroIsNotNull(protocolo.getId(), protocolo.getTipo().getId());
        if(registroRevertido != null){
            ultimoRegistro = registroRevertido;
        }
        novoRegistro.setDataRegistro(dtRegistro);
        novoRegistro.setValor(protocolo.getValor());
        novoRegistro.setEspecialidade(especialidade);
        novoRegistro.setNrPastaPj(protocolo.getPastaPJ());
        novoRegistro.setNumeroRegistro(ultimoRegistro);
        novoRegistro.setRegistro(gerarRegistro(ultimoRegistro, novoRegistro.getDataRegistro()));
        novoRegistro.setProtocolo(protocolo.getId());
        novoRegistro.setRegistroReferencia(protocolo.getRegistroReferencia());
        novoRegistro.setObjetos(protocolo.getObjetos());
        novoRegistro.setSituacaoAtual(protocolo.getSituacaoAtualRegistro());
        novoRegistro.setObservacao(protocolo.getObservacaoRegistro());
        novoRegistro.setServicos(servicos);
        novoRegistro.setNrContrato(protocolo.getNrContrato());
        novoRegistro.setProtocoloCancelamentoIndisponibilidade(protocolo.getProtocoloCancelamentoIndisponibilidade());
        novoRegistro.setProtocoloIndisponibilidade(protocolo.getProtocoloIndisponibilidade());
        novoRegistro.setNatureza(protocolo.getNatureza());
        novoRegistro.setCliente(protocolo.getCliente());
        novoRegistro.setIcGuardaConservacao(protocolo.getIcGuardaConservacao());
        novoRegistro.setIcPossuiSigiloLegal(protocolo.getIcPossuiSigiloLegal());
        novoRegistro.setNumeroRegistroReferencia(protocolo.getNumeroRegistroReferencia());
        if (protocolo.getSubNatureza() != null) {
            novoRegistro.setSubNatureza(protocolo.getSubNatureza());
        }
        for (ParteProtocolo parte : protocolo.getPartesProtocolo()) {
            IndicadorPessoal indicadorPessoal = IndicadorPessoal.converterPartes(parte, novoRegistro);
            try {
                indicadorPessoal.setNomeFonetico(foneticaService.foneticar(indicadorPessoal.getNome()));
            }catch (Exception e ){
                throw new Exception("Erro ao foneticar o indicador pessoal, entre em contato com o Administrador");
            }
            novoRegistro.getIndicadorPessoal().add(indicadorPessoal);
        }


        logger.debug("finalizou gerar Registro");
        return this.registroRepository.save(novoRegistro);

    }

    public Registro findRegistroFromProtocoloCertidao(Long protocoloId) {
        Protocolo protocoloCertidao = protocoloService.findOne(protocoloId);
        if(protocoloCertidao.getTipo().equals(TipoProtocolo.CERTIDAO_PJ) || protocoloCertidao.getTipo().equals(TipoProtocolo.CERTIDAO_TD)){
            return findByRegistroAndEspecialidade(protocoloCertidao.getRegistroReferencia(), TipoProtocolo.recuperaEspecialidade(protocoloCertidao.getTipo()));
        }
        throw new NotFoundException("Registro não encontrao");
    }
}
