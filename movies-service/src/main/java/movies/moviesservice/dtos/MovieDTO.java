package movies.moviesservice.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import movies.moviesservice.dtos.MovieCastDTO.MovieCastDto;
import movies.moviesservice.entity.MovieStatus;
import movies.moviesservice.validations.onCreate.Create;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovieDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String code;

    @NotBlank(message = "Title is required", groups = Create.class)
    private String title;

    private String synopsis;

    @Positive(message = "Duration must be positive")
    private Integer durationMinutes;

    @NotNull(message = "Release date is required", groups = Create.class)
    private LocalDate releaseDate;

    @NotEmpty(message = "At least one language is required", groups = Create.class)
    private Set<String> languageCodes;

    @NotBlank(message = "Certification is required", groups = Create.class)
    private String certification;

    private MovieStatus status;
    private String posterUrl;
    private String bannerUrl;
    private Double imdbRating;

    private String franchiseCode;
    private Set<String> genreCodes;
    private Set<String> tagCodes;
//    private Set<MovieCastDto> cast;


}

