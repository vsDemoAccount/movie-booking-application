// java
package movies.moviesservice.services.movies_service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import movies.moviesservice.client.UserClient;
import movies.moviesservice.dtos.CheckPermissionRequest;
import movies.moviesservice.dtos.MovieCreatedEvent_kfk.MovieCreatedEvent;
import movies.moviesservice.dtos.MovieDTO;
import movies.moviesservice.Mappers.MovieMapper.MovieMapper;
import movies.moviesservice.entity.FailedEvent.FailedEvent;
import movies.moviesservice.entity.Movie;
import movies.moviesservice.entity.language.Language;
import movies.moviesservice.entity.genre.Genre;
import movies.moviesservice.entity.Franchise.Franchise;
import movies.moviesservice.entity.Tag.Tag;
import movies.moviesservice.entity.person.Person;
import movies.moviesservice.repository.FailedEventRepository.FailedEventRepository;
import movies.moviesservice.repository.movies_repo.MoviesRepository;
import movies.moviesservice.repository.language.LanguageRepository;
import movies.moviesservice.repository.GenreRepository.GenreRepository;
import movies.moviesservice.repository.franchiseRepository.franchiseRepository;
import movies.moviesservice.repository.TagRepository.TagRepository;
import movies.moviesservice.repository.PersonRepository.PersonRepository;
import movies.moviesservice.security.SecurityUtils;
import movies.moviesservice.services.MovieEventProducer.MovieEventProducer;
import movies.moviesservice.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MoviesService {

    private final MoviesRepository moviesRepository;
    private final MovieMapper movieMapper;
    private final LanguageRepository languageRepository;
    private final GenreRepository genreRepository;
    private final franchiseRepository franchiseRepository;
    private final TagRepository tagRepository;
    private final PersonRepository personRepository;
    private final MovieEventProducer movieEventProducer;

    private final FailedEventRepository failedEventRepository;
    private final ObjectMapper objectMapper;
    private final UserClient userClient;


    @Autowired
    public MoviesService(
            MoviesRepository moviesRepository,
            MovieMapper movieMapper,
            LanguageRepository languageRepository,
            GenreRepository genreRepository,
            franchiseRepository franchiseRepository,
            TagRepository tagRepository,
            PersonRepository personRepository,
            MovieEventProducer movieEventProducer,
            FailedEventRepository failedEventRepository,
            ObjectMapper objectMapper,
            UserClient userClient) {
        this.moviesRepository = moviesRepository;
        this.movieMapper = movieMapper;
        this.languageRepository = languageRepository;
        this.genreRepository = genreRepository;
        this.franchiseRepository = franchiseRepository;
        this.tagRepository = tagRepository;
        this.personRepository = personRepository;
        this.movieEventProducer = movieEventProducer;
        this.failedEventRepository = failedEventRepository;
        this.objectMapper = objectMapper;
        this.userClient = userClient;
    }

    private Language findLanguageByCode(String code) {
        return languageRepository.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("Language not found: " + code));
    }

    private Genre findGenreByCode(String code) {
        return genreRepository.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("Genre not found: " + code));
    }

    private Franchise findFranchiseByCode(String code) {
        return franchiseRepository.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("Franchise not found: " + code));
    }

    private Tag findTagByCode(String code) {
        return tagRepository.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found: " + code));
    }

    private Person findPersonByCode(String code) {
        return personRepository.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("Person not found: " + code));
    }

    @Transactional
    public MovieDTO createMovie(MovieDTO dto) throws AccessDeniedException {
        if (dto == null) {
            throw new IllegalArgumentException("Movie data is required");
        }

        if (dto.getTitle() != null && moviesRepository.existsByTitleIgnoreCase(dto.getTitle())) {
            throw new IllegalArgumentException("Movie with the same title already exists");
        }

        Movie movie = movieMapper.toEntity(dto);
        // Remove: movie.setCode(null); - let @PrePersist handle it

        String currentUserId = SecurityUtils.getUserId();

        // 2. Check Global Permission (Scope is NULL)
        CheckPermissionRequest req = CheckPermissionRequest.builder()
                .userId(currentUserId)
                .permissionCode("MOVIE_MANAGE")
                .scopeRefCode(null) // Global permission check
                .build();
        ResponseEntity<ApiResponse<Boolean>> response = userClient.checkPermission(req);

        if (response.getBody() == null || !Boolean.TRUE.equals(response.getBody().getData())) {
            throw new AccessDeniedException("Only Admins can create movies.");
        }

        // Resolve language codes to entities
        if (dto.getLanguageCodes() != null && !dto.getLanguageCodes().isEmpty()) {
            Set<Language> languages = dto.getLanguageCodes().stream()
                    .map(this::findLanguageByCode)
                    .collect(Collectors.toSet());
            movie.setLanguages(languages);
        }

        // Resolve genre codes
        if (dto.getGenreCodes() != null && !dto.getGenreCodes().isEmpty()) {
            Set<Genre> genres = dto.getGenreCodes().stream()
                    .map(this::findGenreByCode)
                    .collect(Collectors.toSet());
            movie.setGenres(genres);
        }

        // Resolve franchise code
        if (dto.getFranchiseCode() != null && !dto.getFranchiseCode().isBlank()) {
            Franchise franchise = findFranchiseByCode(dto.getFranchiseCode());
            movie.setFranchise(franchise);
        }

        // Resolve tag codes
        if (dto.getTagCodes() != null && !dto.getTagCodes().isEmpty()) {
            Set<Tag> tags = dto.getTagCodes().stream()
                    .map(this::findTagByCode)
                    .collect(Collectors.toSet());
            movie.setTags(tags);
        }

        Movie savedMovie = moviesRepository.save(movie);

        MovieCreatedEvent event = MovieCreatedEvent.builder()
                .code(savedMovie.getCode())
                .title(savedMovie.getTitle())
                .durationMinutes(savedMovie.getDurationMinutes())
                .posterUrl(savedMovie.getPosterUrl())
                .certification(savedMovie.getCertification())
                .releaseDate(savedMovie.getReleaseDate() != null ? savedMovie.getReleaseDate().toString() : null)
                .primaryGenre(savedMovie.getGenres().isEmpty() ? "General" : savedMovie.getGenres().iterator().next().getName())
                .genres(savedMovie.getGenres().stream().map(Genre::getName).collect(Collectors.toSet()))
                .languages(savedMovie.getLanguages().stream().map(Language::getName).collect(Collectors.toSet()))
                .build();

        try {
            movieEventProducer.sendMovieCreated(event);
        } catch (Exception e) {
            System.err.println("⚠️ Kafka is DOWN. Switching to Fallback: Saving event to DB.");
            handleKafkaFailure(event, "movie-created-topic", e.getMessage());
        }

        return movieMapper.toDTO(savedMovie);
    }

    private void handleKafkaFailure(Object event, String topic, String errorMsg) {
        try {
            String payload = objectMapper.writeValueAsString(event);

            FailedEvent failedEvent = FailedEvent.builder()
                    .topic(topic)
                    .payload(payload)
                    .status("PENDING") // Status for the scheduler to pick up
                    .errorMessage(errorMsg)
                    .createdAt(Instant.now())
                    .retryCount(0)
                    .build();

            failedEventRepository.save(failedEvent);

        } catch (JsonProcessingException jsonEx) {
            System.err.println("❌ Critical Error: Could not serialize event data: " + jsonEx.getMessage());
        }
    }

    public MovieDTO getMovieByCode(String code) {
        Movie movie = moviesRepository.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("Movie not found with code: " + code));
        return movieMapper.toDTO(movie);
    }

    @Transactional
    public MovieDTO updateMovie(String code, MovieDTO dto) {
        Movie movie = moviesRepository.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("Movie not found with code: " + code));

        // Update basic fields
        if (dto.getTitle() != null && !dto.getTitle().equalsIgnoreCase(movie.getTitle())) {
            if (moviesRepository.existsByTitleIgnoreCase(dto.getTitle())) {
                throw new IllegalArgumentException("Movie with the same title already exists");
            }
            movie.setTitle(dto.getTitle());
        }

        if (dto.getSynopsis() != null) movie.setSynopsis(dto.getSynopsis());
        if (dto.getDurationMinutes() != null && dto.getDurationMinutes() > 0) {
            movie.setDurationMinutes(dto.getDurationMinutes());
        }
        if (dto.getReleaseDate() != null) movie.setReleaseDate(dto.getReleaseDate());
        if (dto.getCertification() != null) movie.setCertification(dto.getCertification());
        if (dto.getStatus() != null) movie.setStatus(dto.getStatus());
        if (dto.getPosterUrl() != null) movie.setPosterUrl(dto.getPosterUrl());
        if (dto.getBannerUrl() != null) movie.setBannerUrl(dto.getBannerUrl());
        if (dto.getImdbRating() != null) movie.setImdbRating(dto.getImdbRating());

        // Update language codes
        if (dto.getLanguageCodes() != null) {
            Set<Language> languages = dto.getLanguageCodes().stream()
                    .map(this::findLanguageByCode)
                    .collect(Collectors.toSet());
            movie.setLanguages(languages);
        }

        // Update genre codes
        if (dto.getGenreCodes() != null) {
            Set<Genre> genres = dto.getGenreCodes().stream()
                    .map(this::findGenreByCode)
                    .collect(Collectors.toSet());
            movie.setGenres(genres);
        }

        // Update franchise code
        if (dto.getFranchiseCode() != null) {
            if (dto.getFranchiseCode().isBlank()) {
                movie.setFranchise(null);
            } else {
                Franchise franchise = findFranchiseByCode(dto.getFranchiseCode());
                movie.setFranchise(franchise);
            }
        }

        // Update tag codes
        if (dto.getTagCodes() != null) {
            Set<Tag> tags = dto.getTagCodes().stream()
                    .map(this::findTagByCode)
                    .collect(Collectors.toSet());
            movie.setTags(tags);
        }

        Movie updatedMovie = moviesRepository.save(movie);

        MovieCreatedEvent event = MovieCreatedEvent.builder()
                .code(updatedMovie.getCode())
                .title(updatedMovie.getTitle())
                .durationMinutes(updatedMovie.getDurationMinutes())
                .posterUrl(updatedMovie.getPosterUrl())
                .certification(updatedMovie.getCertification())
                .releaseDate(updatedMovie.getReleaseDate() != null ? updatedMovie.getReleaseDate().toString() : null)
                .primaryGenre(updatedMovie.getGenres().isEmpty() ? "General" : updatedMovie.getGenres().iterator().next().getName())
                .genres(updatedMovie.getGenres().stream().map(Genre::getName).collect(Collectors.toSet()))
                .languages(updatedMovie.getLanguages().stream().map(Language::getName).collect(Collectors.toSet()))
                .build();

        try {
            movieEventProducer.sendMovieUpdated(event);
        } catch (Exception e) {
            System.err.println("Kafka Update Failed. Saving to Fallback DB.");
            handleKafkaFailure(event, "movie-updated-topic", e.getMessage());
        }

        return movieMapper.toDTO(updatedMovie);
    }

    @Transactional
    public void deleteMovie(String code) {
        if (!moviesRepository.existsByCode(code)) {
            throw new EntityNotFoundException("Movie not found with code: " + code);
        }
        moviesRepository.deleteByCode(code);

        try {
            movieEventProducer.sendMovieDeleted(code);
        } catch (Exception e) {
            System.err.println("Kafka Delete Failed. Saving to Fallback DB.");
            // For simple strings, we wrap them in a simple object or just save the string
            handleKafkaFailure(code, "movie-deleted-topic", e.getMessage());
        }
    }

    public Page<MovieDTO> getAllMovies(Pageable pageable) {
        return moviesRepository.findAll(pageable)
                .map(movieMapper::toDTO);
    }
}

