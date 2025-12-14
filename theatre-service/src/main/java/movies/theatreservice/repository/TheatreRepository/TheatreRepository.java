package movies.theatreservice.repository.TheatreRepository;


import movies.theatreservice.entity.Theatre.Theatre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TheatreRepository extends JpaRepository<Theatre, Long> {
    Optional<Theatre> findByCode(String code);

    // Find all theatres in a specific city (Useful for filters)
    List<Theatre> findByCity_Code(String cityCode);
}