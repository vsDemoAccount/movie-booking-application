package movies.moviesservice.services.MovieEventProducer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movies.moviesservice.dtos.MovieCreatedEvent_kfk.MovieCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieEventProducer {

    // CHANGED TO <String, Object> to handle both Event objects and String codes
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC_CREATE = "movie-created-topic";
    private static final String TOPIC_UPDATE = "movie-updated-topic";
    private static final String TOPIC_DELETE = "movie-deleted-topic";

    public void sendMovieCreated(MovieCreatedEvent event) {
        log.info("📢 Publishing Create Event: {}", event.getTitle());
        kafkaTemplate.send(TOPIC_CREATE, event);
    }

    public void sendMovieUpdated(MovieCreatedEvent event) {
        log.info("📢 Publishing Update Event: {}", event.getTitle());
        kafkaTemplate.send(TOPIC_UPDATE, event);
    }

    public void sendMovieDeleted(String movieCode) {
        log.info("📢 Publishing Delete Event: {}", movieCode);
        // Sending String (Code) directly
        kafkaTemplate.send(TOPIC_DELETE, movieCode);
    }
}