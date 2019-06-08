package br.com.exmart.rtdpjlite.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.util.List;

import br.com.exmart.rtdpjlite.BaseTest;
import br.com.exmart.rtdpjlite.model.Natureza;

public class NaturezaRepositoryTest extends BaseTest {
	@Autowired NaturezaRepository naturezaRepository;
	
	@Test
	public void when_findNaturezaByNome_returnsNaturezas() {
		String nome = "%livro%";
		Pageable pageable=new PageRequest(0,10,new Sort(Sort.Direction.ASC,"id"));
		Page<Natureza> naturezas=naturezaRepository.findByNomeLikeIgnoreCase(nome,pageable);
		for (Natureza natureza : naturezas) {
			logger.info(natureza.getNome());
		}
		assertThat(naturezas.getSize()).isGreaterThan(0);
	}
	
	@Test
	public void when_findAllNatureza_returnsNaturezas() {
        List<Natureza> naturezas = naturezaRepository.findAll();
		for (Natureza natureza : naturezas) {
			logger.info(natureza.getNome());
		}
		assertThat(naturezas.size()).isGreaterThan(0);
	}
	
}
