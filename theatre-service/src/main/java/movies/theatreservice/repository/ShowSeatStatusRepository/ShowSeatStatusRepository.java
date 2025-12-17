package movies.theatreservice.repository.ShowSeatStatusRepository;

import movies.theatreservice.entity.ShowSeatStatus.ShowSeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowSeatStatusRepository extends JpaRepository<ShowSeatStatus, Long> {

    List<ShowSeatStatus> findByShow_Code(String showCode);

    // UPDATED: Check for conflicts using Show Code
    @Query("SELECT COUNT(s) FROM ShowSeatStatus s WHERE s.show.code = :showCode AND s.seat.id IN :seatIds")
    long countOccupiedSeats(@Param("showCode") String showCode, @Param("seatIds") List<Long> seatIds);
}