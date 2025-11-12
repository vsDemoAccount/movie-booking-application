package movies.moviesservice.repository.GenreRepository;

import movies.moviesservice.entity.genre.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    Optional<Genre> findByCode(String code);
}