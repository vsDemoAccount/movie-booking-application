package movies.theatreservice.repository.SeatTypeRepository;

import movies.theatreservice.entity.SeatType.SeatType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeatTypeRepository extends JpaRepository<SeatType, Long> {
    Optional<SeatType> findByCode(String code);
    boolean existsByName(String name);
}