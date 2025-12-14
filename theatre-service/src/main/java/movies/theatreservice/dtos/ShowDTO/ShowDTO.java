package movies.theatreservice.dtos.ShowDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShowDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String code;

    @NotBlank(message = "Screen Code is required")
    private String screenCode;

    @NotBlank(message = "Movie Code is required")
    private String movieCode;

    // READ_ONLY: Enriched data from the Catalog (The frontend doesn't send this, we send it back)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String movieTitle;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String posterUrl;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer durationMinutes;

    @NotNull(message = "Start Time is required")
    private Instant startTime;

    // READ_ONLY: Calculated by backend
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant endTime;

    // Config for input AND display for output
    @NotEmpty(message = "Price configuration is required")
    @Valid
    private List<ShowPriceDTO> prices;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant createdAt;

    // Inner static class for the price list items
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ShowPriceDTO {
        @NotBlank(message = "Seat Type Code is required")
        private String seatTypeCode;

        @NotNull(message = "Price is required")
        @Min(value = 0, message = "Price cannot be negative")
        private BigDecimal price;

        // Optional: Read-only name for display (e.g., "VIP")
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        private String seatTypeName;
    }
}