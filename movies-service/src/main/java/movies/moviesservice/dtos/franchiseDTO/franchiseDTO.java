package movies.moviesservice.dtos.franchiseDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import movies.moviesservice.validations.onCreate.Create;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class franchiseDTO {

    @Null(groups = Create.class, message = "Code must not be provided when creating and updating a franchise")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String code;

    @NotBlank
    private String name;

    private String description;
}
