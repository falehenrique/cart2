package br.com.exmart.rtdpjlite.service;

import br.com.exmart.rtdpjlite.model.Modelo;
import br.com.exmart.rtdpjlite.model.TipoModelo;
import br.com.exmart.rtdpjlite.repository.ModeloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModeloService {
    @Autowired
    private ModeloRepository modeloRepository;

    public List<Modelo> findModeloByTipo(Long idNatureza, Long idSubNatureza, TipoModelo tipoModelo){
        if(idSubNatureza == null){
            return modeloRepository.findByNaturezaAndSubNatureza(idNatureza, tipoModelo.getId());
        }else {
            return modeloRepository.findByNaturezaAndSubNatureza(idNatureza, idSubNatureza, tipoModelo.getId());
        }
    }
}
