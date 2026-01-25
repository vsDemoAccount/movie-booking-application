package booking.bookingapplication.scheduler;

import booking.bookingapplication.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * PHASE 4: Booking Expiry Scheduler
 * Runs every 1 minute to clean up expired bookings
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class BookingExpiryScheduler {

    private final BookingService bookingService;

    /**
     * Auto-expire bookings where payment wasn't completed in time
     * Runs: Every minute (cron: "0 * * * * *")
     */
    @Scheduled(cron = "0 * * * * *")
    public void cleanupExpiredBookings() {
        log.debug("Running booking expiry cleanup");

        int count = bookingService.expireOldBookings();

        if (count > 0) {
            log.info("Expired {} booking(s)", count);
        }
    }
}

