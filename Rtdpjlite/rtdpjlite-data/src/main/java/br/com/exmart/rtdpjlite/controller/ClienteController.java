package br.com.exmart.rtdpjlite.controller;

import br.com.exmart.rtdpjlite.model.Cliente;
import br.com.exmart.rtdpjlite.model.Protocolo;
import br.com.exmart.rtdpjlite.service.ClienteService;
import br.com.exmart.rtdpjlite.service.ProtocoloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cliente")
public class ClienteController {
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private ProtocoloService protocoloService;

    @RequestMapping(value = "/{idCliente}/protocolo")//http://localhost:8080/cliente/2/protocolo?page=1&size=5
    public Page<Protocolo> findByCliente(@PathVariable String idCliente, Pageable pagina){
        Cliente cliente = clienteService.findByIdentificacao(idCliente);
        if(cliente == null){
            throw new RuntimeException("Cliente não encontrado");
        }
        Page<Protocolo>  retorno =  protocoloService.findByClientePageable(cliente, pagina);
        return retorno;
    }

    @RequestMapping(value = "/")//http://localhost:8080/cliente/2/protocolo?page=1&size=5
    public Long findIdBYEmail(@RequestParam String email){
        Cliente cliente = clienteService.findByEmailIgnoreCase(email);
        if(cliente == null){
            throw new RuntimeException("Cliente não encontrado");
        }
        return cliente.getId();
    }
}
