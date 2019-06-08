package br.com.exmart.rtdpjlite.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.exmart.rtdpjlite.model.FormaPagamento;
import br.com.exmart.rtdpjlite.repository.FormaPagamentoRepository;

@Service
public class FormaPagamentoService implements CrudService<FormaPagamento> {
	@Autowired FormaPagamentoRepository formaPagamentoRepository;
	
	public List<FormaPagamento> findAll(){
		return getRepository().findAll();
	}
	
	public FormaPagamento findOne(Long id){
		return getRepository().findOne(id);
	}

	@Override
	public FormaPagamentoRepository getRepository() {
		return formaPagamentoRepository;
	}

	@Override
	public long countAnyMatching(Optional<String> filter) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Page<FormaPagamento> findAnyMatching(Optional<String> filter, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<FormaPagamento> find(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

}
