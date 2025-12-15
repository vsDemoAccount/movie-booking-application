package movies.moviesservice.scheduler.EventRetryScheduler;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movies.moviesservice.entity.FailedEvent.FailedEvent;
import movies.moviesservice.repository.FailedEventRepository.FailedEventRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@RequiredArgsConstructor
public class EventRetryScheduler {

    private final FailedEventRepository failedEventRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    // Run every 10 minutes.
    // fixedDelay = 600000ms (10 mins) means wait 10 mins AFTER the previous job finishes.
    @Scheduled(fixedDelay = 600000)
    public void retryFailedEvents() {

        // 1. Fetch PENDING events
        List<FailedEvent> failedEvents = failedEventRepository.findByStatus("PENDING");

        if (failedEvents.isEmpty()) {
            return; // No work to do, exit immediately (0% CPU usage)
        }

        log.info("🔄 Retry Scheduler found {} failed events. Attempting resend...", failedEvents.size());

        for (FailedEvent event : failedEvents) {
            try {
                // 2. We need to turn the JSON String back into an Object for Kafka
                // Using Object.class lets Jackson create a Map, which the Kafka Serializer handles fine.
                Object payload = objectMapper.readValue(event.getPayload(), Object.class);

                // 3. Try Sending to Kafka (Wait max 2 seconds for confirmation)
                // We use .get() to make this blocking so we know if it succeeded
                kafkaTemplate.send(event.getTopic(), payload).get(2, TimeUnit.SECONDS);

                // 4. If we reach here, it was SUCCESSFUL
                event.setStatus("SENT");
                event.setErrorMessage(null);
                failedEventRepository.save(event);
                log.info("✅ Successfully resent event ID: {}", event.getId());

            } catch (Exception e) {
                // 5. If it FAILS again
                log.warn("⚠️ Retry failed for event ID: {}. Error: {}", event.getId(), e.getMessage());

                event.setRetryCount(event.getRetryCount() + 1);
                event.setErrorMessage(e.getMessage());

                // If it has failed 5 times, stop trying.
                if (event.getRetryCount() >= 2) {
                    event.setStatus("FAILED_PERMANENTLY");
                    log.error("❌ Marking event ID: {} as FAILED_PERMANENTLY after 5 attempts.", event.getId());
                }

                failedEventRepository.save(event);
            }
        }
    }
}