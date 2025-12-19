package movies.theatreservice.repository.ShowRepository;

import movies.theatreservice.entity.Show.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {

    Optional<Show> findByCode(String code);

    // --- ADD THIS LINE TO FIX THE ERROR ---
    // Checks if ANY show exists for this movie code
    boolean existsByMovie_Code(String movieCode);
    // -------------------------------------

    @Modifying
    @Query("UPDATE Show s SET s.status = 'COMPLETED' WHERE s.endTime < :now AND s.status = 'SCHEDULED'")
    int updateStatusForPastShows(@Param("now") Instant now);

    // Used for Ripple Effect (Movie Updates)
    List<Show> findByMovie_CodeAndStartTimeAfter(String movieCode, Instant now);

    // --- UPDATED OVERLAP QUERY ---
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Show s " +
            "WHERE s.screen.id = :screenId " +
            "AND s.status != 'CANCELLED' " +
            "AND s.startTime < :newEndTime " +
            "AND s.endTime > :newStartTime")
    boolean existsOverlappingShow(@Param("screenId") Long screenId,
                                  @Param("newStartTime") Instant newStartTime,
                                  @Param("newEndTime") Instant newEndTime);

    // --- UPDATED EXCLUDE-SELF QUERY ---
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Show s " +
            "WHERE s.screen.id = :screenId " +
            "AND s.id != :currentShowId " +
            "AND s.status != 'CANCELLED' " +
            "AND s.startTime < :newEndTime " +
            "AND s.endTime > :newStartTime")
    boolean existsOverlappingShowExcludingSelf(@Param("screenId") Long screenId,
                                               @Param("newStartTime") Instant newStartTime,
                                               @Param("newEndTime") Instant newEndTime,
                                               @Param("currentShowId") Long currentShowId);
}