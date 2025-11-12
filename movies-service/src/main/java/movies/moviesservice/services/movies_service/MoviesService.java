// java
package movies.moviesservice.services.movies_service;

import jakarta.persistence.EntityNotFoundException;
import movies.moviesservice.dtos.*;
import movies.moviesservice.Mappers.MovieMapper.MovieMapper;
import movies.moviesservice.entity.Movie;
import movies.moviesservice.repository.movies_repo.MoviesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import movies.moviesservice.entity.language.Language;
import movies.moviesservice.entity.genre.Genre;
import movies.moviesservice.entity.Franchise.Franchise;
import movies.moviesservice.entity.movieCast.MovieCast;
import movies.moviesservice.entity.person.Person;
//import movies.moviesservice.entity.MovieMedia.MovieMedia;
//import movies.moviesservice.entity.ExternalRating.ExternalRating;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MoviesService {

    private final MoviesRepository moviesRepository;
    private final MovieMapper movieMapper;

    @Autowired
    public MoviesService(MoviesRepository moviesRepository, MovieMapper movieMapper) {
        this.moviesRepository = moviesRepository;
        this.movieMapper = movieMapper;
    }

    public MovieDTO createMovie(MovieDTO dto) {
        if (dto == null) throw new IllegalArgumentException("Movie data is required");

        String title = dto.getTitle();
        if (title != null && moviesRepository.existsByTitleIgnoreCase(title)) {
            throw new IllegalArgumentException("Movie with the same title already exists");
        }

        // mapper creates base entity but relations were ignored on purpose in mapper
        Movie movie = movieMapper.toEntity(dto);

        // ensure code is not set from dto
        movie.setCode(null);

        // populate relations manually from DTO
        if (dto.getLanguages() != null) {
            Set<Language> languageEntities = dto.getLanguages().stream()
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(name -> {
                        Language lang = new Language();
                        lang.setName(name);
                        return lang;
                    })
                    .collect(Collectors.toSet());
            movie.setLanguages(languageEntities);
        } else {
            movie.setLanguages(new HashSet<>());
        }

        if (dto.getGenres() != null) {
            Set<Genre> genreEntities = dto.getGenres().stream()
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(name -> {
                        Genre g = new Genre();
                        g.setName(name);
                        return g;
                    })
                    .collect(Collectors.toSet());
            movie.setGenres(genreEntities);
        } else {
            movie.setGenres(new HashSet<>());
        }

        if (dto.getFranchise() != null && !dto.getFranchise().isBlank()) {
            Franchise f = new Franchise();
            f.setName(dto.getFranchise().trim());
            movie.setFranchise(f);
        } else {
            movie.setFranchise(null);
        }

//        if (dto.getCast() != null) {
//            Set<MovieCast> castEntities = dto.getCast().stream()
//                    .map(cdto -> {
//                        MovieCast mc = new MovieCast();
//                        Person p = new Person();
//                        if (cdto.getPersonId() != null) p.setId(cdto.getPersonId());
//                        if (cdto.getPersonName() != null) p.setName(cdto.getPersonName());
//                        mc.setPerson(p);
//                        // Convert CastRole enum to String for storage
//                        mc.setRole(cdto.getRole() != null ? cdto.getRole().name() : null);
//                        mc.setCharacterName(cdto.getCharacterName());
//                        mc.setMovie(movie);
//                        return mc;
//                    })
//                    .collect(Collectors.toSet());
//            movie.setCast(castEntities);
//        } else {
//            movie.setCast(new HashSet<>());
//        }

//        if (dto.getMedia() != null) {
//            Set<MovieMedia> mediaEntities = dto.getMedia().stream()
//                    .map(md -> {
//                        MovieMedia mm = new MovieMedia();
//                        mm.setType(md.getType());
//                        mm.setUrl(md.getUrl());
//                        mm.setMovie(movie);
//                        return mm;
//                    })
//                    .collect(Collectors.toSet());
//            movie.setMedia(mediaEntities);
//        } else {
//            movie.setMedia(new HashSet<>());
//        }
//
//        if (dto.getExternalRatings() != null) {
//            Set<ExternalRating> ratingEntities = dto.getExternalRatings().stream()
//                    .map(rd -> {
//                        ExternalRating er = new ExternalRating();
//                        er.setSource(rd.getSource());
//                        er.setRating(rd.getRating());
//                        er.setMovie(movie);
//                        return er;
//                    })
//                    .collect(Collectors.toSet());
//            movie.setExternalRatings(ratingEntities);
//        } else {
//            movie.setExternalRatings(new HashSet<>());
//        }

        // regionRights: if Movie entity no longer has regionRights field, it will remain null

        Movie savedMovie = moviesRepository.save(movie);
        return movieMapper.toDTO(savedMovie);
    }

    public MovieDTO getMovieByCode(String code) {
        Movie movie = moviesRepository.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("Movie not found with code: " + code));
        return movieMapper.toDTO(movie);
    }

    public MovieDTO updateMovie(String code, MovieDTO dto) {
        Movie movie = moviesRepository.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("Movie not found with code: " + code));

        // Update fields except code
        if (dto.getTitle() != null && !dto.getTitle().equalsIgnoreCase(movie.getTitle())) {
            if (moviesRepository.existsByTitleIgnoreCase(dto.getTitle())) {
                throw new IllegalArgumentException("Movie with the same title already exists");
            }
            movie.setTitle(dto.getTitle());
        }
        if (dto.getSynopsis() != null) movie.setSynopsis(dto.getSynopsis());
        if (dto.getDurationMinutes() > 0) {
            movie.setDurationMinutes(dto.getDurationMinutes());
        }
        if (dto.getReleaseDate() != null) movie.setReleaseDate(dto.getReleaseDate());
        if (dto.getLanguages() != null) {
            Set<Language> languageEntities = dto.getLanguages().stream()
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(name -> {
                        Language lang = new Language();
                        lang.setName(name);
                        return lang;
                    })
                    .collect(Collectors.toSet());
            movie.setLanguages(languageEntities);
        }
        if (dto.getCertification() != null) movie.setCertification(dto.getCertification());
        if (dto.getStatus() != null) movie.setStatus(dto.getStatus());
        if (dto.getPosterUrl() != null) movie.setPosterUrl(dto.getPosterUrl());
        if (dto.getCreatedAt() != null) movie.setCreatedAt(dto.getCreatedAt());
        if (dto.getUpdatedAt() != null) movie.setUpdatedAt(dto.getUpdatedAt());

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
