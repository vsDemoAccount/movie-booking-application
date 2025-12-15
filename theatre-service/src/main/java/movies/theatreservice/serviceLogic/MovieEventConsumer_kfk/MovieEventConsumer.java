package movies.theatreservice.serviceLogic.MovieEventConsumer_kfk;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import movies.theatreservice.dtos.MovieCreatedEvent;
import movies.theatreservice.entity.CatalogMovie_kfk.CatalogMovie;
import movies.theatreservice.repository.CatalogMovieRepository.CatalogMovieRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieEventConsumer {

    private final CatalogMovieRepository catalogMovieRepository;

    // Ensure 'movie-created-topic' matches what your Movie Service sends
    @KafkaListener(topics = "movie-created-topic", groupId = "theatre-service-group")
    public void consumeMovieEvent(MovieCreatedEvent event) {
        log.info("Received Movie Event: {}", event.getTitle());

        CatalogMovie movie = CatalogMovie.builder()
                .code(event.getCode())
                .title(event.getTitle())
                .durationMinutes(event.getDurationMinutes())
                .posterUrl(event.getPosterUrl())
                .certification(event.getCertification())
                // Convert String back to LocalDate
                .releaseDate(event.getReleaseDate() != null ? LocalDate.parse(event.getReleaseDate()) : null)
                .languages(event.getLanguages())
                .genres(event.getGenres())
                .syncedAt(Instant.now())
                .build();

        catalogMovieRepository.save(movie);
    }
}