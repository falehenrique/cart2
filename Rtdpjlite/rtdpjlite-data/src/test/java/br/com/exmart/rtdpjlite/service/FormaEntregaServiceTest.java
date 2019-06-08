package br.com.exmart.rtdpjlite.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.exmart.rtdpjlite.BaseTest;
import br.com.exmart.rtdpjlite.model.FormaEntrega;

public class FormaEntregaServiceTest extends BaseTest {
	@Autowired FormaEntregaService formaEntregaService;
	@Test
	public void A_whenFindAll() {
		List<FormaEntrega> formas = formaEntregaService.findAll();
		assertThat(formas.size()).isGreaterThan(0);
	}
}
