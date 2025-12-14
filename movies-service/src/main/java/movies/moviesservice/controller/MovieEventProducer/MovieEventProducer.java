package movies.moviesservice.controller.MovieEventProducer;



import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movies.moviesservice.dtos.MovieCreatedEvent_kfk.MovieCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieEventProducer {

    private final KafkaTemplate<String, MovieCreatedEvent> kafkaTemplate;
    private static final String TOPIC = "movie-created-topic";

    public void sendMovieCreated(MovieCreatedEvent event) {
        log.info("📢 Publishing Movie Event to Kafka: Code={}, Title={}", event.getCode(), event.getTitle());

        // We build a message with the TOPIC header and the PAYLOAD (DTO)
        kafkaTemplate.send(MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, TOPIC)
                .build());
    }
}