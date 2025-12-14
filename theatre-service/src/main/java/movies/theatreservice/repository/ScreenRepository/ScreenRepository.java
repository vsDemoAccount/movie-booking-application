package movies.theatreservice.repository.ScreenRepository;

import movies.theatreservice.entity.Screen.Screen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScreenRepository extends JpaRepository<Screen, Long> {
    Optional<Screen> findByCode(String code);

    // Find all screens inside a specific theatre
    List<Screen> findByTheatre_Code(String theatreCode);
}