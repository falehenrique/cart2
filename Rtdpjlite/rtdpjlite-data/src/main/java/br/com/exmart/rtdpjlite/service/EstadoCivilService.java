package br.com.exmart.rtdpjlite.service;

import br.com.exmart.rtdpjlite.model.EstadoCivil;
import br.com.exmart.rtdpjlite.repository.EstadoCivilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstadoCivilService implements CrudService<EstadoCivil>{

    @Autowired
    private EstadoCivilRepository estadoCivilRepository;

    @Override
    public CrudRepository<EstadoCivil, Long> getRepository() {
        return this.estadoCivilRepository;
    }

    @Override
    public long countAnyMatching(Optional<String> filter) {
        return 0;
    }

    @Override
    public Page<EstadoCivil> findAnyMatching(Optional<String> filter, Pageable pageable) {
        return null;
    }

    @Override
    public Page<EstadoCivil> find(Pageable pageable) {
        return null;
    }

    public List<EstadoCivil> findAll() {
        return estadoCivilRepository.findAll(new Sort("nome"));
    }
}
