package br.com.exmart.rtdpjlite.controller;

import br.com.exmart.rtdpjlite.model.Cliente;
import br.com.exmart.rtdpjlite.model.Registro;
import br.com.exmart.rtdpjlite.service.ClienteService;
import br.com.exmart.rtdpjlite.service.RegistroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/registro")
public class RegistroController {

    @Autowired
    private RegistroService registroService;
    @Autowired
    private ClienteService clienteService;

    @RequestMapping(value = "/cliente/{identificacao}")//localhost:8080/registro/cliente/2?page=1&size=5&sort=name&name.dir=desc
    public Page<Registro> findByCliente(@PathVariable String identificacao, Pageable pagina){
        Cliente cliente = clienteService.findByIdentificacao(identificacao);
        if(cliente == null){
            throw new RuntimeException("Cliente não encontrado");
        }
        Page<Registro>  retorno =  registroService.findByClientePageable(cliente, pagina);
        return retorno;
    }
}
