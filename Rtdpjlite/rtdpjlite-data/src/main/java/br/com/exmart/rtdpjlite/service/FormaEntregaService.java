package br.com.exmart.rtdpjlite.service;

import br.com.exmart.rtdpjlite.model.FormaEntrega;
import br.com.exmart.rtdpjlite.repository.FormaEntregaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FormaEntregaService implements CrudService<FormaEntrega>{
	@Autowired FormaEntregaRepository formaEntregaRepository;
	
	@Override
	public CrudRepository<FormaEntrega, Long> getRepository() {
		return formaEntregaRepository;
	}

	public List<FormaEntrega> findAll(){
		return formaEntregaRepository.findAll();
	}
	
	@Override
	public long countAnyMatching(Optional<String> filter) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Page<FormaEntrega> findAnyMatching(Optional<String> filter, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<FormaEntrega> find(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}


    public FormaEntrega findByNome(String nome) {
		return this.formaEntregaRepository.findByNome(nome);
    }
}
