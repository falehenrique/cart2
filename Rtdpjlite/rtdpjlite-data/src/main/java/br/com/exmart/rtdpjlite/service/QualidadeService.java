package br.com.exmart.rtdpjlite.service;

import br.com.exmart.rtdpjlite.model.Qualidade;
import br.com.exmart.rtdpjlite.model.SubNatureza;
import br.com.exmart.rtdpjlite.repository.QualidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QualidadeService{

    @Autowired
    private QualidadeRepository qualidadeRepository;


    public Page<Qualidade> findAnyMatching(Optional<String> filter, Pageable pageable) {
        return null;
    }

    public Page<Qualidade> find(Pageable pageable) {
        return null;
    }

    public List<Qualidade> findAll() {
        return qualidadeRepository.findAll(new Sort("nome"));
    }

    public Qualidade findByNome(String nome) {
        return qualidadeRepository.findByNomeIgnoreCase(nome);
    }
}
