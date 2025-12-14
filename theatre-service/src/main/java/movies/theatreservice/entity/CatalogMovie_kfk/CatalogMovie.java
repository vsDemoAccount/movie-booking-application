package movies.theatreservice.entity.CatalogMovie_kfk;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;

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
    private String code; // Matches "Mov-123" from Movie Service

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private int durationMinutes;

    private String posterUrl;
    private String genre;

    @Column(name = "synced_at", nullable = false)
    private Instant syncedAt;
}