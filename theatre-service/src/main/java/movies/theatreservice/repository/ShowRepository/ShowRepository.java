package movies.theatreservice.repository.ShowRepository;

import movies.theatreservice.entity.Show.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {

    // Custom Query: Check if a show overlaps with existing shows in the same screen
    // Logic: (NewStart < OldEnd) AND (NewEnd > OldStart)
    @Query("SELECT s FROM Show s WHERE s.screen.id = :screenId " +
            "AND s.startTime < :endTime " +
            "AND (s.startTime + (s.movie.durationMinutes * 60) SECOND) > :startTime")
    List<Show> findOverlappingShows(@Param("screenId") Long screenId,
                                    @Param("startTime") Instant startTime,
                                    @Param("endTime") Instant endTime);
}