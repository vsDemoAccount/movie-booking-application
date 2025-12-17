package movies.theatreservice.repository.ShowRepository;

import movies.theatreservice.entity.Show.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {

    Optional<Show> findByCode(String code);

    // Custom Query: Check if a show overlaps with existing shows in the same screen
    // Logic: (NewStart < OldEnd) AND (NewEnd > OldStart)
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Show s " +
            "WHERE s.screen.id = :screenId " +
            "AND s.startTime < :newEndTime " +
            "AND s.endTime > :newStartTime")
    boolean existsOverlappingShow(@Param("screenId") Long screenId,
                                  @Param("newStartTime") Instant newStartTime,
                                  @Param("newEndTime") Instant newEndTime);
}