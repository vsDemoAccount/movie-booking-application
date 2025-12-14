package movies.theatreservice.dtos.ScreenDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScreenDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String code;

    @NotBlank(message = "Screen name is required")
    private String name;

    @NotBlank(message = "Theatre Code is required")
    private String theatreCode;

    // Optional: E.g., ["Dolby Atmos", "Recliner Seats"]
    private Set<String> features;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant updatedAt;
}