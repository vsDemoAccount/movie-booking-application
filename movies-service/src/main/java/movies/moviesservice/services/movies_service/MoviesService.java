// java
package movies.moviesservice.services.movies_service;


import jakarta.persistence.EntityNotFoundException;
import movies.moviesservice.dtos.MovieDTO;
import movies.moviesservice.dtos.MovieCastDTO.MovieCastDto;
import movies.moviesservice.Mappers.MovieMapper.MovieMapper;
import movies.moviesservice.entity.Movie;
import movies.moviesservice.entity.language.Language;
import movies.moviesservice.entity.genre.Genre;
import movies.moviesservice.entity.Franchise.Franchise;
import movies.moviesservice.entity.Tag.Tag;
import movies.moviesservice.entity.movieCast.MovieCast;
import movies.moviesservice.entity.person.Person;
import movies.moviesservice.repository.movies_repo.MoviesRepository;
import movies.moviesservice.repository.language.LanguageRepository;
import movies.moviesservice.repository.GenreRepository.GenreRepository;
import movies.moviesservice.repository.franchiseRepository.franchiseRepository;
import movies.moviesservice.repository.TagRepository.TagRepository;
import movies.moviesservice.repository.PersonRepository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
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

    @Autowired
    public MoviesService(
            MoviesRepository moviesRepository,
            MovieMapper movieMapper,
            LanguageRepository languageRepository,
            GenreRepository genreRepository,
            franchiseRepository franchiseRepository,
            TagRepository tagRepository,
            PersonRepository personRepository) {
        this.moviesRepository = moviesRepository;
        this.movieMapper = movieMapper;
        this.languageRepository = languageRepository;
        this.genreRepository = genreRepository;
        this.franchiseRepository = franchiseRepository;
        this.tagRepository = tagRepository;
        this.personRepository = personRepository;
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
    public MovieDTO createMovie(MovieDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Movie data is required");
        }

        if (dto.getTitle() != null && moviesRepository.existsByTitleIgnoreCase(dto.getTitle())) {
            throw new IllegalArgumentException("Movie with the same title already exists");
        }

        Movie movie = movieMapper.toEntity(dto);
        movie.setCode(null); // Let @PrePersist generate code

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

        // Resolve cast person codes
//        if (dto.getCast() != null && !dto.getCast().isEmpty()) {
//            Set<MovieCast> castEntities = dto.getCast().stream()
//                    .map(castDto -> {
//                        MovieCast mc = MovieCast.builder()
//                                .person(findPersonByCode(castDto.getPersonCode()))
//                                .role(castDto.getRole())
//                                .characterName(castDto.getCharacterName())
//                                .movie(movie)
//                                .build();
//                        return mc;
//                    })
//                    .collect(Collectors.toSet());
//            movie.setCast(castEntities);
//        }

        Movie savedMovie = moviesRepository.save(movie);
        return movieMapper.toDTO(savedMovie);
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

        // Update cast
//        if (dto.getCast() != null) {
//            movie.getCast().clear();
//            Set<MovieCast> castEntities = dto.getCast().stream()
//                    .map(castDto -> MovieCast.builder()
//                            .person(findPersonByCode(castDto.getPersonCode()))
//                            .role(castDto.getRole())
//                            .characterName(castDto.getCharacterName())
//                            .movie(movie)
//                            .build())
//                    .collect(Collectors.toSet());
//            movie.setCast(castEntities);
//        }

        Movie updatedMovie = moviesRepository.save(movie);
        return movieMapper.toDTO(updatedMovie);
    }

    @Transactional
    public void deleteMovie(String code) {
        if (!moviesRepository.existsByCode(code)) {
            throw new EntityNotFoundException("Movie not found with code: " + code);
        }
        moviesRepository.deleteByCode(code);
    }

    public Page<MovieDTO> getAllMovies(Pageable pageable) {
        return moviesRepository.findAll(pageable)
                .map(movieMapper::toDTO);
    }
}

