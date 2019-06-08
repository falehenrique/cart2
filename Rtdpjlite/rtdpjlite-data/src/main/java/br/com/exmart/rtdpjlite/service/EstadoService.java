package br.com.exmart.rtdpjlite.service;

import br.com.exmart.rtdpjlite.model.Estado;
import br.com.exmart.rtdpjlite.repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstadoService implements CrudService<Estado> {
    @Autowired
    private EstadoRepository estadoRepository;
    @Override
    public EstadoRepository getRepository() {
        return estadoRepository;
    }

    @Override
    public long countAnyMatching(Optional<String> filter) {
        return 0;
    }

    @Override
    public Page<Estado> findAnyMatching(Optional<String> filter, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Estado> find(Pageable pageable) {
        return null;
    }



    public List<Estado> findAll(){
        return this.getRepository().findAll(new Sort(Sort.Direction.ASC, "nome"));
    }
}
