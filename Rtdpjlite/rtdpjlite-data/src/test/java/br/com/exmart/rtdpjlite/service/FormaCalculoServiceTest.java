package br.com.exmart.rtdpjlite.service;

import br.com.exmart.rtdpjlite.BaseTest;
import br.com.exmart.rtdpjlite.model.FormaCalculo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FormaCalculoServiceTest extends BaseTest{
	@Autowired FormaCalculoService formaCalculoService;
	
	@Test
	public void whenFindAll_returns_List() {
		List<FormaCalculo> formas = formaCalculoService.findAll();
		assertThat(formas.size()).isGreaterThan(0);
	}
}
