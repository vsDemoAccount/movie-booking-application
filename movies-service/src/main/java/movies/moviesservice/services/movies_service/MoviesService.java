package movies.moviesservice.services.movies_service;

import jakarta.persistence.EntityNotFoundException;
import movies.moviesservice.dtos.MovieDTO;
import movies.moviesservice.Mappers.MovieMapper.MovieMapper;
import movies.moviesservice.entity.Movie;
import movies.moviesservice.repository.movies_repo.MoviesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import movies.moviesservice.entity.languages;
import org.springframework.transaction.annotation.Transactional;


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

        Movie movie = movieMapper.toEntity(dto);
        // Ensure code is not set from dto
        movie.setCode(null);

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
            Set<languages> languageEnums = dto.getLanguages().stream()
                    .map(String::toUpperCase)
                    .map(languages::valueOf)
                    .collect(Collectors.toSet());
            movie.setLanguages(languageEnums);
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
