package infra.db;

import java.util.Collection;
import java.util.Optional;

public interface Database<T> {

     T save(T entity);

    Optional<T> findById(String id);

    Collection<T> findAll();

    void deleteAll();
}
