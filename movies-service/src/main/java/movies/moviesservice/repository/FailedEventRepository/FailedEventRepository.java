package movies.moviesservice.repository.FailedEventRepository;

import movies.moviesservice.entity.FailedEvent.FailedEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FailedEventRepository extends JpaRepository<FailedEvent, Long> {
    // Used by the Scheduler later to find retry candidates
    List<FailedEvent> findByStatus(String status);
}