package movies.moviesservice.dtos.MovieCreatedEvent_kfk;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

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

    private String certification;   // e.g., "UA", "A"
    private String releaseDate;     // Send as String (e.g., "2025-12-25") to avoid timezone issues

    private String primaryGenre;    // e.g. "Action" (For simple cards)
    private Set<String> genres;     // e.g. ["Action", "Sci-Fi"] (For detailed filters)
    private Set<String> languages;
}