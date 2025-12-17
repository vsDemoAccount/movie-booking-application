package movies.theatreservice.dtos.LockSeatsRequestDTO;

import lombok.Data;
import java.util.List;

@Data
public class LockSeatsRequestDTO {
    private String showCode;        // Changed from Long showId
    private List<String> seatCodes;// e.g. ["Sea-123", "Sea-456"]
    private String bookingRefId;    // Temporary Booking ID
}