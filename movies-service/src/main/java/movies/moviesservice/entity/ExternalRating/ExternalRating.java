package movies.moviesservice.entity.ExternalRating;

import jakarta.persistence.*;
import lombok.*;
import movies.moviesservice.entity.Movie;

@Entity
@Table(name = "external_ratings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExternalRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @Column(nullable = false)
    private String source;

    private Double rating;
}
