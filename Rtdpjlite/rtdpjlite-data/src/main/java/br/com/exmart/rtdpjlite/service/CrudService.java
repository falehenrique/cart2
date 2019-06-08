package br.com.exmart.rtdpjlite.service;

import br.com.exmart.rtdpjlite.model.AbstractEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.io.IOException;
import java.util.Optional;


public interface CrudService<T extends AbstractEntity> {

	CrudRepository<T, Long> getRepository();

	default T save(T entity) throws IOException {
		return getRepository().save(entity);
	}

	default void delete(long id) {
		getRepository().delete(id);
	}

	default Long count() {
		return getRepository().count();
	}

	default T load(long id) {
		return getRepository().findOne(id);
	}

	long countAnyMatching(Optional<String> filter);

	Page<T> findAnyMatching(Optional<String> filter, Pageable pageable);

	Page<T> find(Pageable pageable);
}
