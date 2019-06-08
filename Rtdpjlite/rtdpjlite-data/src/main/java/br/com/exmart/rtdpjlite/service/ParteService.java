package br.com.exmart.rtdpjlite.service;

import br.com.exmart.rtdpjlite.model.Parte;
import br.com.exmart.rtdpjlite.repository.ParteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ParteService  implements CrudService<Parte> {

    @Autowired
    private ParteRepository parteRepository;

    @Override
    public ParteRepository getRepository() {
        return parteRepository;
    }

    @Override
    public long countAnyMatching(Optional<String> filter) {
        return 0;
    }

    @Override
    public Page<Parte> findAnyMatching(Optional<String> filter, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Parte> find(Pageable pageable) {
        return null;
    }

    public Parte findFirstByCpfCnpjOrderByIdDesc(String documento){
        return getRepository().findFirstByCpfCnpjOrderByIdDesc(documento);
    }

    public Parte findConjuge(String uuidConjuge, Long id) {
        return getRepository().findFirstByUuidConjugeAndIdNotOrderByIdDesc(uuidConjuge, id);
    }

}
