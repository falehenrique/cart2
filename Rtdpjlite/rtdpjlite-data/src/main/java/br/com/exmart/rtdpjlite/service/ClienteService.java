package br.com.exmart.rtdpjlite.service;

import br.com.exmart.rtdpjlite.model.Cliente;
import br.com.exmart.rtdpjlite.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente findByIdentificacao(String identificacao) {
        return this.clienteRepository.findByIdentificacao(identificacao);
    }

    public void save(Cliente novoCliente) {
        this.clienteRepository.save(novoCliente);
    }

    public List<Cliente> findAll() {
        return this.clienteRepository.findAll(new Sort("nome"));
    }

    public Cliente findOne(Long id) {
        return this.clienteRepository.findOne(id);
    }

    public Cliente findByEmailIgnoreCase(String email) {
        return this.clienteRepository.findByEmailIgnoreCase(email);
    }
}
