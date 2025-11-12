// java
package movies.moviesservice.services.MovieCastService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import movies.moviesservice.dtos.MovieCastDTO.MovieCastDto;
import movies.moviesservice.entity.movieCast.MovieCast;
import movies.moviesservice.Mappers.MovieCastMapper.MovieCastMapper;
import movies.moviesservice.repository.CastRepository.CastRepository;
import movies.moviesservice.repository.PersonRepository.PersonRepository;
import movies.moviesservice.repository.movies_repo.MoviesRepository;

@Service
@Transactional
public class MovieCastService {
    private final CastRepository castRepository;
    private final MovieCastMapper mapper;
    private final PersonRepository personRepository;
    private final MoviesRepository movieRepository;

    public MovieCastService(CastRepository castRepository, MovieCastMapper mapper,
                           PersonRepository personRepository, MoviesRepository movieRepository) {
        this.castRepository = castRepository;
        this.mapper = mapper;
        this.personRepository = personRepository;
        this.movieRepository = movieRepository;
    }

    public MovieCastDto create(MovieCastDto dto) {
        if (dto.getCode() != null && castRepository.existsByCode(dto.getCode())) {
            throw new IllegalArgumentException("MovieCast with code already exists");
        }
        MovieCast entity = mapper.toEntity(dto);
        MovieCast saved = castRepository.save(entity);
        return mapper.toDto(saved);
    }

    public MovieCastDto findByCode(String code) {
        return castRepository.findByCode(code).map(mapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("MovieCast not found for code: " + code));
    }

    public MovieCastDto updateByCode(String code, MovieCastDto dto) {
        MovieCast existing = castRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("MovieCast not found for code: " + code));

        if (dto.getPersonCode() != null) {
            personRepository.findByCode(dto.getPersonCode())
                    .ifPresent(existing::setPerson);
        }

        if (dto.getMovieCode() != null) {
            movieRepository.findByCode(dto.getMovieCode())
                    .ifPresent(existing::setMovie);
        }

        if (dto.getCharacterName() != null) {
            existing.setCharacterName(dto.getCharacterName());
        }

        if (dto.getRole() != null) {
            existing.setRole(dto.getRole());
        }

        MovieCast saved = castRepository.save(existing);
        return mapper.toDto(saved);
    }

    public void deleteByCode(String code) {
        castRepository.findByCode(code).ifPresent(castRepository::delete);
    }
}
