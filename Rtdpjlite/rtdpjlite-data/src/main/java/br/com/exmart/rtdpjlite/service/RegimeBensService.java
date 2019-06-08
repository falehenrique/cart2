package br.com.exmart.rtdpjlite.service;

import br.com.exmart.rtdpjlite.model.RegimeBens;
import br.com.exmart.rtdpjlite.repository.RegimeBensRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RegimeBensService implements CrudService<RegimeBens> {

    @Autowired private RegimeBensRepository regimeBensRepository;

    @Override
    public RegimeBensRepository getRepository() {
        return this.regimeBensRepository;
    }

    @Override
    public long countAnyMatching(Optional<String> filter) {
        return 0;
    }

    @Override
    public Page<RegimeBens> findAnyMatching(Optional<String> filter, Pageable pageable) {
        return null;
    }

    @Override
    public Page<RegimeBens> find(Pageable pageable) {
        return null;
    }

    public List<RegimeBens> findAll(){
       return regimeBensRepository.findAll(new Sort("nome"));
    }
}
