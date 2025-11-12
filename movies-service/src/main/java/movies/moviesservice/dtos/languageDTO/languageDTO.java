package movies.moviesservice.dtos.languageDTO;

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
public class languageDTO {

    @NotBlank
    private String name;

    @Null(groups = Create.class, message = "Code must not be provided when creating or updating a language")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String code;
}
