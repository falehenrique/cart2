package br.com.exmart.rtdpjlite.service;

import br.com.exmart.rtdpjlite.model.Objeto;
import br.com.exmart.rtdpjlite.repository.ObjetoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ObjetoService implements CrudService<Objeto> {
    @Autowired
    private ObjetoRepository objetoRepository;
    @Override
    public ObjetoRepository getRepository() {
        return this.objetoRepository;
    }

    @Override
    public long countAnyMatching(Optional<String> filter) {
        return 0;
    }

    @Override
    public Page<Objeto> findAnyMatching(Optional<String> filter, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Objeto> find(Pageable pageable) {
        return null;
    }

    public List<Objeto> findAll(){
//        return getRepository().findAll();
        return getRepository().findAll(new Sort(Sort.Direction.ASC, "nome"));
    }

    @EntityGraph(value = "Objeto.full", type = EntityGraph.EntityGraphType.LOAD)
    public Objeto findByNome(String nome) {
        return objetoRepository.findByNome(nome);
    }
}
