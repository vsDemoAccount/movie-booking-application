package movies.moviesservice.dtos.PersonDTO;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import movies.moviesservice.validations.onCreate.Create;

import java.time.Instant;
import java.time.LocalDate;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonDto {
    @Null(groups = Create.class, message = "Code must not be provided when creating or updating a person")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    public String code;
    public String name;
    public LocalDate birthDate;
    public String bio;
    public String photoUrl;

}

