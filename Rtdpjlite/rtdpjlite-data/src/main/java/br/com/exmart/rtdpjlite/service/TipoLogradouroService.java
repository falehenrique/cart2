package br.com.exmart.rtdpjlite.service;

import br.com.exmart.rtdpjlite.model.TipoLogradouro;
import br.com.exmart.rtdpjlite.repository.TipoLogradouroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoLogradouroService {
    @Autowired
    private TipoLogradouroRepository tipoLogradouroRepository;

    public List<TipoLogradouro> findAll(){
        return tipoLogradouroRepository.findAll(new Sort(Sort.Direction.ASC,"nome"));
    }
}
