package movies.theatreservice.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MovieCreatedEvent {
    private String code;
    private String title;
    private int durationMinutes;
    private String posterUrl;
    private String genre;
}