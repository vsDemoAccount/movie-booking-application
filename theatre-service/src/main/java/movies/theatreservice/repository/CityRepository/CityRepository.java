package movies.theatreservice.repository.CityRepository;

import movies.theatreservice.entity.city.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    // Find a city by its public code
    Optional<City> findByCode(String code);

    // Check if exists (useful for validation)
    boolean existsByCode(String code);

    // Check for duplicate names before creating
    boolean existsByName(String name);
}