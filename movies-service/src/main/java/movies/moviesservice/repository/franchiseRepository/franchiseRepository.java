package movies.moviesservice.repository.franchiseRepository;

import movies.moviesservice.entity.Franchise.Franchise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface franchiseRepository extends JpaRepository<Franchise, Long> {
    Optional<Franchise> findByCode(String code);
    boolean existsByCode(String code);
    void deleteByCode(String code);
}