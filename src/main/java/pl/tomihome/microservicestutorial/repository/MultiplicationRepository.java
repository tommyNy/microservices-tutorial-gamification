package pl.tomihome.microservicestutorial.repository;

import org.springframework.data.repository.CrudRepository;
import pl.tomihome.microservicestutorial.domain.Multiplication;

public interface MultiplicationRepository extends CrudRepository<Multiplication, Long> {
}
