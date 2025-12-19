package movies.theatreservice.serviceLogic.MovieEventConsumer_kfk;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movies.theatreservice.dtos.MovieCreatedEvent;
import movies.theatreservice.entity.CatalogMovie_kfk.CatalogMovie;
import movies.theatreservice.repository.CatalogMovieRepository.CatalogMovieRepository;
import movies.theatreservice.repository.ShowRepository.ShowRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieEventConsumer {

    private final CatalogMovieRepository catalogMovieRepository;
    private final ShowRepository showRepository;

    // 1. CREATE LISTENER
    @KafkaListener(topics = "movie-created-topic", groupId = "theatre-service-group")
    public void consumeMovieCreated(MovieCreatedEvent event) {
        log.info(" Kafka: Creating Movie -> {}", event.getTitle());
        saveToCatalog(event);
    }

    // 2. UPDATE LISTENER
    @KafkaListener(topics = "movie-updated-topic", groupId = "theatre-service-group")
    @Transactional
    public void consumeMovieUpdated(MovieCreatedEvent event) {
        log.info(" Kafka: Updating Movie -> {}", event.getTitle());

        // Update the Catalog
        saveToCatalog(event);

        // TODO: Trigger Ripple Effect Check here
        // (Check if duration changed and if it conflicts with future shows)
    }

    // 3. DELETE LISTENER
    @KafkaListener(topics = "movie-deleted-topic", groupId = "theatre-service-group")
    @Transactional
    public void consumeMovieDeleted(String movieCode) {
        log.warn(" Kafka: Request to Delete Movie -> {}", movieCode);

        boolean hasShows = showRepository.existsByMovie_Code(movieCode);

        if (hasShows) {
            log.error(" BLOCKED DELETE: Movie {} has scheduled shows. Archiving instead.", movieCode);
            // Optional: You could mark an 'isDeleted' flag here instead of physical delete
        } else {
            catalogMovieRepository.deleteById(movieCode);
            log.info("🗑️ Movie deleted from Catalog.");
        }
    }

    // Helper method to avoid duplicate code
    private void saveToCatalog(MovieCreatedEvent event) {
        CatalogMovie movie = CatalogMovie.builder()
                .code(event.getCode())
                .title(event.getTitle())
                .durationMinutes(event.getDurationMinutes())
                .posterUrl(event.getPosterUrl())
                .certification(event.getCertification())
                .releaseDate(event.getReleaseDate() != null ? LocalDate.parse(event.getReleaseDate()) : null)
                .languages(event.getLanguages())
                .genres(event.getGenres())
                .syncedAt(Instant.now())
                .build();

        catalogMovieRepository.save(movie);
    }
}