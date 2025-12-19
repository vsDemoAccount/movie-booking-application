package movies.theatreservice.schedular;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movies.theatreservice.repository.ShowSeatStatusRepository.ShowSeatStatusRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class LockCleanupScheduler {

    private final ShowSeatStatusRepository showSeatStatusRepository;

    // Run every 1 minute (60,000 ms)
    @Scheduled(fixedRate = 600000)     //10 min
    @Transactional
    public void releaseExpiredLocks() {
        // Define "Expired" as: Locked more than 10 minutes ago
        Instant cutoffTime = Instant.now().minus(10, ChronoUnit.MINUTES);

        int deletedCount = showSeatStatusRepository.deleteExpiredLocks(cutoffTime);

        if (deletedCount > 0) {
            log.info("🔓 Released {} expired seat locks (Zombie bookings).", deletedCount);
        }
    }
}