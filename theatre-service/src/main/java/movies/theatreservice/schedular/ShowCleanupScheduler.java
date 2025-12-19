package movies.theatreservice.schedular;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movies.theatreservice.repository.ShowRepository.ShowRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class ShowCleanupScheduler {

    private final ShowRepository showRepository;

    // Run every 30 minutes
    @Scheduled(fixedRate = 1800000)
    @Transactional
    public void markShowsAsCompleted() {
        Instant now = Instant.now();

        int updatedCount = showRepository.updateStatusForPastShows(now);

        if (updatedCount > 0) {
            log.info("🏁 Marked {} past shows as COMPLETED.", updatedCount);
        }
    }
}