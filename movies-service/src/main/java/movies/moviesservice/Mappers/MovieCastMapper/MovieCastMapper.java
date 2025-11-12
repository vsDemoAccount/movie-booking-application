package movies.moviesservice.Mappers.MovieCastMapper;

import org.springframework.stereotype.Component;
import movies.moviesservice.dtos.MovieCastDTO.MovieCastDto;
import movies.moviesservice.entity.movieCast.MovieCast;
import movies.moviesservice.repository.PersonRepository.PersonRepository;
import movies.moviesservice.repository.movies_repo.MoviesRepository;

@Component
public class MovieCastMapper {
    private final PersonRepository personRepository;
    private final MoviesRepository movieRepository;

    public MovieCastMapper(PersonRepository personRepository, MoviesRepository movieRepository) {
        this.personRepository = personRepository;
        this.movieRepository = movieRepository;
    }

    public MovieCastDto toDto(MovieCast mc) {
        if (mc == null) return null;
        MovieCastDto d = new MovieCastDto();
        d.setCode(mc.getCode());
        d.setPersonCode(mc.getPerson() != null ? mc.getPerson().getCode() : null);
        d.setPersonName(mc.getPerson() != null ? mc.getPerson().getName() : null);
        d.setMovieCode(mc.getMovie() != null ? mc.getMovie().getCode() : null);
        d.setRole(mc.getRole());
        d.setCharacterName(mc.getCharacterName());
        return d;
    }

    public MovieCast toEntity(MovieCastDto d) {
        if (d == null) return null;
        MovieCast mc = new MovieCast();
        mc.setCode(d.getCode());
        mc.setCharacterName(d.getCharacterName());
        mc.setRole(d.getRole());
        if (d.getPersonCode() != null) {
            personRepository.findByCode(d.getPersonCode()).ifPresent(mc::setPerson);
        }
        if (d.getMovieCode() != null) {
            movieRepository.findByCode(d.getMovieCode()).ifPresent(mc::setMovie);
        }
        return mc;
    }
}
