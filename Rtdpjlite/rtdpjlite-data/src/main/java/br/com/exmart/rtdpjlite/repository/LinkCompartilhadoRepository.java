package br.com.exmart.rtdpjlite.repository;

import br.com.exmart.rtdpjlite.model.LinkCompartilhado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkCompartilhadoRepository extends JpaRepository<LinkCompartilhado, String> {
    LinkCompartilhado findById(String id);
}
