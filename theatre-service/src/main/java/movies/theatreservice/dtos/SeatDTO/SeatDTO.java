package movies.theatreservice.dtos.SeatDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String code;

    @NotBlank(message = "Screen Code is required")
    private String screenCode;

    @NotBlank(message = "Seat Type Code is required")
    private String seatTypeCode;

    @NotBlank(message = "Row Name is required")
    private String rowName;

    @NotNull(message = "Seat Number is required")
    @Min(value = 1, message = "Seat number must be positive")
    private Integer seatNumber;

    private int gridRow;
    private int gridCol;

    @Builder.Default
    private boolean isActive = true;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant createdAt;
}