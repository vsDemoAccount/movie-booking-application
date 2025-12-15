package movies.theatreservice.dtos.SeatLayoutDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatLayoutDTO {
    // The screen we are filling with seats
    private String screenCode;

    // Grid dimensions
    private int totalRows;       // e.g., 10 (Rows A to J)
    private int columnsPerRow;   // e.g., 15 (Seats 1 to 15)

    // Key: Row Name ("A", "B"), Value: SeatTypeCode ("VIP", "STANDARD")
    // If a row is missing here, we default to "STANDARD"
    private Map<String, String> rowTypeMapping;

    // Columns that are empty (aisles). e.g. [5, 10] means column 5 and 10 are empty.
    private List<Integer> aisleColumns;
}
