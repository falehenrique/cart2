package br.com.exmart.rtdpjlite.service;

import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.exmart.rtdpjlite.BaseTest;
import br.com.exmart.rtdpjlite.model.Usuario;

public class
UsuarioServiceTest extends BaseTest {
	@Autowired UsuarioService usuarioService;
	
	@Test
	public void whenGetEscreventes_returnsEscreventes() {
		List<Usuario> escreventes = new ArrayList<Usuario>();
		escreventes = usuarioService.getEscreventes();
		escreventes.forEach(escrevente->{
			getLogger().info(escrevente.getNome());
		});
		assertThat(escreventes.size()).isGreaterThan(0);
	}
}
