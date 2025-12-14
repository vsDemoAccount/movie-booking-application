package movies.theatreservice.dtos.TheatreDTO;

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
public class TheatreDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String code;

    @NotBlank(message = "Theatre name is required")
    private String name;

    @NotBlank(message = "Address is required")
    private String address;

    private String description;

    @NotBlank(message = "City Code is required")
    private String cityCode;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant updatedAt;
}