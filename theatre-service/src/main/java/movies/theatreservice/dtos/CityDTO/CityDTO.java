package movies.theatreservice.dtos.CityDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CityDTO {

    // READ_ONLY: Frontend receives this, but cannot send it
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String code;

    @NotBlank(message = "City name is required")
    private String name;

    @NotBlank(message = "State is required")
    private String state;

    // READ_ONLY: System managed fields
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant updatedAt;
}