package br.com.exmart.rtdpjlite.service;

import br.com.exmart.rtdpjlite.model.Escrevente;
import br.com.exmart.rtdpjlite.model.Protocolo;
import br.com.exmart.rtdpjlite.model.Usuario;
import br.com.exmart.rtdpjlite.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService  {
	@Autowired
	private UsuarioRepository usuarioRepository;

	public List<Usuario> getEscreventes(){
		List<Usuario> escreventes = usuarioRepository.findEscreventes("Escrevente");
		return escreventes;
	}


	public Usuario findByEmail(String username) {
		return usuarioRepository.findByEmail(username);
	}

	public Usuario findById(long id) {
		return usuarioRepository.findById(id);
	}

	public Usuario getUsuarioPortal() {
		Usuario user = findByEmail("portal@portal.com.br");
		if(user == null){
			user = new Usuario();
			user.setEmail("portal@portal.com.br");
			user.setNome("Usuario portal");
			user.setRole("admin");
			//senha = 2osasco
			user.setSenha("70dbe0d7648ef0848a002555784debb28d8a2088");
			usuarioRepository.save(user);

		}
		return user;
	}
}
