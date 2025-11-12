package movies.moviesservice.repository.PersonRepository;

import movies.moviesservice.entity.person.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByCode(String code);
    boolean existsByCode(String code);
}
