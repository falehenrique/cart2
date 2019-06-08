package br.com.exmart.rtdpjlite.service;

import br.com.exmart.rtdpjlite.model.TipoDocumento;
import br.com.exmart.rtdpjlite.repository.TipoDocumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoDocumentoService {
    @Autowired
    private TipoDocumentoRepository tipoDocumentoRepository;

    public List<TipoDocumento> findAll(){
        return tipoDocumentoRepository.findAll(new Sort(Sort.Direction.ASC,"nome"));
    }
}
