package movies.theatreservice.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class MovieCreatedEvent {
    private String code;
    private String title;
    private int durationMinutes;
    private String posterUrl;
    private String genre;

    private String certification;
    private String releaseDate;
    private String primaryGenre;
    private Set<String> genres;
    private Set<String> languages;
}