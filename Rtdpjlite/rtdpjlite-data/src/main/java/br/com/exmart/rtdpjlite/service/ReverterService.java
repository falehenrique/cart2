package br.com.exmart.rtdpjlite.service;

import br.com.exmart.rtdpjlite.model.*;
import br.com.exmart.rtdpjlite.repository.ProtocoloRepository;
import br.com.exmart.rtdpjlite.repository.RegistroRepository;
import br.com.exmart.rtdpjlite.repository.ReversaoRepository;
import br.com.exmart.rtdpjlite.service.enums.Diretorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ReverterService {
    @Autowired
    private ProtocoloRepository protocoloRepository;
    @Autowired
    private RegistroRepository registroRepository;
    @Autowired
    private ArquivoService arquivoService;
    @Autowired
    private ReversaoRepository reversaoRepository;


    public boolean verificarRegistroRevertido(Integer tipoProtocoloId, Long numeroRegistro ){
        Optional<Reversao> reversao = reversaoRepository.findFirstByProtocoloTipoIdAndNumeroRegistro(tipoProtocoloId, numeroRegistro);
        return reversao.isPresent();
    }

    @org.springframework.transaction.annotation.Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
    public boolean reverterProtocolo(Protocolo protocolo, String motivo, Usuario usuario) throws Exception {
        try {
            arquivoService.excluirArquivos(Diretorio.PROTOCOLO, protocolo.getNumero(), TipoProtocolo.recuperaEspecialidade(protocolo.getTipo()), protocolo.getTipo());
            Reversao reversao = new Reversao(usuario.getId(), LocalDateTime.now(),motivo, protocolo.getId(),protocolo.getTipo().getId(), null);
            Integer retorno = protocoloRepository.reverterProtocolo(protocolo.getNumero().intValue(), protocolo.getTipo().getId());
            reversaoRepository.save(reversao);
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("Erro ao excluir arquivos, Favor contatar um Administrador");
        }
        return true;
    }
    @org.springframework.transaction.annotation.Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
    public boolean reverterRegistro(Protocolo protocolo, Registro registro, String motivo, Usuario usuario) throws Exception {
        try {
            arquivoService.excluirArquivos(Diretorio.REGISTRO, registro.getNumeroRegistro(), registro.getEspecialidade(), null);
            Reversao reversao = new Reversao(usuario.getId(), LocalDateTime.now(),motivo, protocolo.getId(), protocolo.getTipo().getId(), registro.getNumeroRegistro());
            Integer retorno = registroRepository.reverterRegistro(registro.getRegistro(),registro.getEspecialidade());
            reversaoRepository.save(reversao);
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("Erro ao excluir arquivos, Favor contatar um Administrador");
        }
        return true;
    }

    public Long findByProtocoloIdAndTipoProtocoloIdAndNumeroRegistroIsNotNull(Long protocoloId, Integer tipoProtocoloId) {
        Optional<Reversao> reversao = reversaoRepository.findFirstByProtocoloIdAndProtocoloTipoIdAndNumeroRegistroIsNotNull(protocoloId, tipoProtocoloId);
        if(reversao.isPresent()){
            return  reversao.get().getNumeroRegistro();
        }
        return null;
    }
}
