package br.com.exmart.rtdpjlite.service;

import br.com.exmart.rtdpjlite.model.UsuarioNotificacao;
import br.com.exmart.rtdpjlite.repository.UsuarioNotificacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UsuarioNotificacaoService {

    @Autowired private UsuarioNotificacaoRepository usuarioNotificacaoRepository;

    public void adicionarNotificacao(Long usuarioId, String assunto, String descricao){
        UsuarioNotificacao novaNotificacao = new UsuarioNotificacao(usuarioId, LocalDateTime.now(), assunto, descricao);
        usuarioNotificacaoRepository.save(novaNotificacao);
    }

    public List<UsuarioNotificacao> findByUsuarioIdAndLidaIsNull(Long usuarioId) {
        return usuarioNotificacaoRepository.findByUsuarioIdAndLidaIsNull(usuarioId);
    }

    public void marcarComoLida(Long id) {
        UsuarioNotificacao notificacao = usuarioNotificacaoRepository.findById(id);
        notificacao.setLida(LocalDateTime.now());
        usuarioNotificacaoRepository.save(notificacao);
    }
}
