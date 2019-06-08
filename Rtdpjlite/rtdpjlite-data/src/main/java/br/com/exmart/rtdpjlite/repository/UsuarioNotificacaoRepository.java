package br.com.exmart.rtdpjlite.repository;

import br.com.exmart.rtdpjlite.model.UsuarioNotificacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioNotificacaoRepository extends JpaRepository<UsuarioNotificacao, Long> {
    List<UsuarioNotificacao> findByUsuarioId(Long usuarioId);

    List<UsuarioNotificacao> findByUsuarioIdAndLidaIsNull(Long usuarioId);

    UsuarioNotificacao findById(Long id);
}
