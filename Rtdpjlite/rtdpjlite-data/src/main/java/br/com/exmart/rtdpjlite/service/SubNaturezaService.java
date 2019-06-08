package br.com.exmart.rtdpjlite.service;

import br.com.exmart.rtdpjlite.model.SubNatureza;
import br.com.exmart.rtdpjlite.repository.SubNaturezaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubNaturezaService {

    @Autowired
    private SubNaturezaRepository subNaturezaRepository;

    public List<SubNatureza> findAll(){
        return this.subNaturezaRepository.findAll();
    }

    public SubNatureza findByNome(String nome) {
        return this.subNaturezaRepository.findByNome(nome);
    }
}
