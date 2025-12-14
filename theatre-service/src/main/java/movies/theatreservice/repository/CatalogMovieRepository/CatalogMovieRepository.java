package movies.theatreservice.repository.CatalogMovieRepository;

import movies.theatreservice.entity.CatalogMovie_kfk.CatalogMovie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CatalogMovieRepository extends JpaRepository<CatalogMovie, String> {
    // Basic findById (which takes the String code) is already provided by JpaRepository
    // You can add custom finders here if needed later, e.g.:
    // Optional<CatalogMovie> findByTitle(String title);
}