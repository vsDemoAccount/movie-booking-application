package movies.moviesservice.repository.CastRepository;

import movies.moviesservice.entity.movieCast.MovieCast;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CastRepository extends JpaRepository<MovieCast, Long> {
    Optional<MovieCast> findByCode(String code);
    boolean existsByCode(String code);
}
