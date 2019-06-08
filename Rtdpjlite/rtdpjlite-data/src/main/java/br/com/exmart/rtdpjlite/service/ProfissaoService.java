package br.com.exmart.rtdpjlite.service;

import br.com.exmart.rtdpjlite.model.Profissao;
import br.com.exmart.rtdpjlite.repository.ProfissaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfissaoService {

    @Autowired
    private ProfissaoRepository profissaoRepository;

    public List<Profissao> findAll() {
        return this.profissaoRepository.findAll(new Sort("nome"));
    }
}
