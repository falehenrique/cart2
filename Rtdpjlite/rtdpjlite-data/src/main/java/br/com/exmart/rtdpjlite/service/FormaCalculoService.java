package br.com.exmart.rtdpjlite.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.exmart.rtdpjlite.model.FormaCalculo;
import br.com.exmart.rtdpjlite.repository.FormaCalculoRepository;

@Service
public class FormaCalculoService implements CrudService<FormaCalculo>{
	@Autowired FormaCalculoRepository formaCalculoRepository;
	
	public List<FormaCalculo> findAll(){
		return formaCalculoRepository.findAll();
	}
	
	public FormaCalculo findOne(Long id){
		return formaCalculoRepository.findOne(id);
	}

	@Override
	public FormaCalculoRepository getRepository() {
		return formaCalculoRepository;
	}

	@Override
	public long countAnyMatching(Optional<String> filter) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Page<FormaCalculo> findAnyMatching(Optional<String> filter, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<FormaCalculo> find(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

}
