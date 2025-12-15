package movies.theatreservice.entity.CatalogMovie_kfk;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "movie_catalog")
public class CatalogMovie {

    @Id
    @Column(length = 16, nullable = false)
    private String code;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private int durationMinutes;

    private String posterUrl;

    private String certification; // Stored locally
    private LocalDate releaseDate; // Stored locally


    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "movie_catalog_languages", joinColumns = @JoinColumn(name = "movie_code"))
    @Column(name = "language")
    private Set<String> languages;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "movie_catalog_genres", joinColumns = @JoinColumn(name = "movie_code"))
    @Column(name = "genre")
    private Set<String> genres;

    @Column(name = "synced_at", nullable = false)
    private Instant syncedAt;
}