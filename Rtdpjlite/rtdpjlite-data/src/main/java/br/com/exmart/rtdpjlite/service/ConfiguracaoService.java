package br.com.exmart.rtdpjlite.service;

import br.com.exmart.rtdpjlite.model.Configuracao;
import br.com.exmart.rtdpjlite.repository.ConfiguracaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConfiguracaoService implements CrudService<Configuracao> {
    @Autowired
    private ConfiguracaoRepository configuracaoRepository;
    @Override
    public CrudRepository<Configuracao, Long> getRepository() {
        return this.configuracaoRepository;
    }

    @Override
    public long countAnyMatching(Optional<String> filter) {
        return 0;
    }

    @Override
    public Page<Configuracao> findAnyMatching(Optional<String> filter, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Configuracao> find(Pageable pageable) {
        return null;
    }

    public Configuracao findConfiguracao() {
        return this.configuracaoRepository.findOne(1L);
    }
}
