package movies.moviesservice.dtos.MovieCastDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import movies.moviesservice.entity.CastRole;
import movies.moviesservice.validations.onCreate.Create;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieCastDto {
    @Null(groups = Create.class, message = "Code must not be provided when creating or updating a Movie Cast")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String code;
    private String movieCode;
    private String personCode;
    private String personName;
    private CastRole role;
    private String characterName;

}

