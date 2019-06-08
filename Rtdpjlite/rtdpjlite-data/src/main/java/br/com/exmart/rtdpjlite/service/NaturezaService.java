package br.com.exmart.rtdpjlite.service;

import br.com.exmart.rtdpjlite.model.Natureza;
import br.com.exmart.rtdpjlite.model.SubNatureza;
import br.com.exmart.rtdpjlite.repository.NaturezaRepository;
import br.com.exmart.util.BeanLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NaturezaService implements CrudService<Natureza>{

	@Autowired
	private NaturezaRepository naturezaRepository;

//	@Cacheable(value = "naturezaCache")
	public List<Natureza> findAll(){
		return getRepository().findAll();
	}
//	@Cacheable(value ="naturezaByTipo", key = "#tipoId")
	public List<Natureza> findAllByTipo(Long tipoId){
		return getRepository().findByNaturezaTipoId(tipoId);
	}
	
	@Override
	public NaturezaRepository getRepository() {
		return naturezaRepository;
	}

	@Override
	public long countAnyMatching(Optional<String> filter) {
		if (filter.isPresent()) {
			String repositoryFilter = "%" + filter.get() + "%";
			return getRepository().countByNome(repositoryFilter);
		} else {
			return getRepository().count();
		}
	}

	@Override
	public Page<Natureza> findAnyMatching(Optional<String> filter, Pageable pageable) {
		if (filter.isPresent()) {
			String repositoryFilter = "%" + filter.get() + "%";
			return getRepository().findByNomeLikeIgnoreCase(repositoryFilter, pageable);
		} else {
			return find(pageable);
		}
	}

	@Override
	public Page<Natureza> find(Pageable pageable) {
		return getRepository().findAll(pageable);
	}

	public Natureza findBySubnatureza(SubNatureza subNatureza) {
		return this.getRepository().findBySubNaturezas_id(subNatureza.getId());
	}

	public Natureza findByNome(String nome) {
		return this.naturezaRepository.findByNome(nome);
	}

    public Natureza findById(Long id) {
		return naturezaRepository.findOne(id);
    }
}
