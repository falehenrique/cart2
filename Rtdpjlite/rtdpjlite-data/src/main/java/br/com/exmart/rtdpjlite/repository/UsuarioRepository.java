package br.com.exmart.rtdpjlite.repository;

import br.com.exmart.rtdpjlite.model.Protocolo;
import br.com.exmart.rtdpjlite.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query(value="select usuario.* from rtdpj.usuario join rtdpj.usuario_dados using(id) where id_cargo = (select id from rtdpj.cargo where cargo = ?1);", nativeQuery=true)
    List<Usuario> findEscreventes(String cargo);

    Usuario findByEmail(String username);

    Usuario findById(long id);
}
