package movies.moviesservice.repository.language;

import movies.moviesservice.entity.language.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LanguageRepository extends JpaRepository<Language, Long> {
    Optional<Language> findByNameIgnoreCase(String name);
}