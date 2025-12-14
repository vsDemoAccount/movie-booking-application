package movies.moviesservice.dtos.MovieCreatedEvent_kfk;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieCreatedEvent {
    private String code;           // "Mov-123"
    private String title;          // "Avengers"
    private int durationMinutes;   // 180
    private String posterUrl;
    private String genre;          // "Action"
}