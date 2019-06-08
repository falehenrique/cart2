package br.com.exmart.rtdpjlite.controller;

import br.com.exmart.rtdpjlite.model.Objeto;
import br.com.exmart.rtdpjlite.service.ObjetoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/objeto")
public class ObjetoController {

    @Autowired
    private ObjetoService objetoService;

    @GetMapping( value = "/")
    public List<Objeto> findAll(){
        return objetoService.findAll();
    }
}
