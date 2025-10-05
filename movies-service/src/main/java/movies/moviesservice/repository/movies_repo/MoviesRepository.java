// java
package movies.moviesservice.repository.movies_repo;

import movies.moviesservice.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MoviesRepository extends JpaRepository<Movie, Long> {
    boolean existsByTitleIgnoreCase(String title);
    Optional<Movie> findByCode(String code);
    boolean existsByCode(String code);
    void deleteByCode(String code);
}
