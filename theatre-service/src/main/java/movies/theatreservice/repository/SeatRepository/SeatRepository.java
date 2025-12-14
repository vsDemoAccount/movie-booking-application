package movies.theatreservice.repository.SeatRepository;

import movies.theatreservice.entity.Seat.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    Optional<Seat> findByCode(String code);

    // Get all seats for a specific screen (Critical for UI layout)
    List<Seat> findByScreen_Code(String screenCode);

    // Check if a specific seat position exists in a specific screen
    boolean existsByScreen_CodeAndRowNameAndSeatNumber(String screenCode, String rowName, int seatNumber);
}