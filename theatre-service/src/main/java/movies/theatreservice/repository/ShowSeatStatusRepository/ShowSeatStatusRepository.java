package movies.theatreservice.repository.ShowSeatStatusRepository;

import movies.theatreservice.entity.ShowSeatStatus.ShowSeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface ShowSeatStatusRepository extends JpaRepository<ShowSeatStatus, Long> {

    List<ShowSeatStatus> findByShow_Code(String showCode);

    boolean existsByBookingRefId(String bookingRefId);

    // UPDATED: Check for conflicts using Show Code
    @Query("SELECT COUNT(s) FROM ShowSeatStatus s WHERE s.show.code = :showCode AND s.seat.id IN :seatIds")
    long countOccupiedSeats(@Param("showCode") String showCode, @Param("seatIds") List<Long> seatIds);

    boolean existsBySeat_IdAndShow_StartTimeAfter(Long seatId, Instant now);

    @Modifying // Required for DELETE/UPDATE queries
    @Query("DELETE FROM ShowSeatStatus s WHERE s.status = 'LOCKED' AND s.lockedAt < :cutoffTime")
    int deleteExpiredLocks(@Param("cutoffTime") Instant cutoffTime);
}