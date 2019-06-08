package br.com.exmart.rtdpjlite.service;

import br.com.exmart.rtdpjlite.model.Nacionalidade;
import br.com.exmart.rtdpjlite.repository.NacionalidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NacionalidadeService {
    @Autowired
    private NacionalidadeRepository nacionalidadeRepository;


    public long countAnyMatching(Optional<String> filter) {
        return 0;
    }

    public Page<Nacionalidade> findAnyMatching(Optional<String> filter, Pageable pageable) {
        return null;
    }

    public Page<Nacionalidade> find(Pageable pageable) {
        return null;
    }

    public List<Nacionalidade> findAll() {
        return nacionalidadeRepository.findAll(new Sort("nome"));
    }
}
