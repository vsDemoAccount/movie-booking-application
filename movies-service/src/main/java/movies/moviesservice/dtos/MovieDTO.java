package movies.moviesservice.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
//import movies.moviesservice.dtos.ExternalRatingDTO.ExternalRatingDTO;
import movies.moviesservice.dtos.MovieCastDTO.MovieCastDto;
//import movies.moviesservice.dtos.MovieMediaDTO.MovieMediaDTO;
//import movies.moviesservice.dtos.RegionRightsDTO.RegionRightsDTO;
import movies.moviesservice.entity.MovieStatus;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO {
    private String code;
    @NotNull
    private String title;
    private String synopsis;
    private int durationMinutes;
    @NotNull
    private LocalDate releaseDate;
    // Use simple String set for transport to avoid exposing internal entity types
    @NotNull
    private Set<String> languages;
    @NotNull
    private String certification;
    private MovieStatus status;
    private String posterUrl;
    private Instant createdAt;
    private Instant updatedAt;

    private Set<String> genres;
    private String franchise;
    private Set<MovieCastDto> cast;
}
