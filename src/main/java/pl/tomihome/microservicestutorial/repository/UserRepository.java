package pl.tomihome.microservicestutorial.repository;

import org.springframework.data.repository.CrudRepository;
import pl.tomihome.microservicestutorial.domain.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByAlias(String alias);
}
