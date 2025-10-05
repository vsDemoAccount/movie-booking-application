package movies.moviesservice.entity.MovieMedia;


import jakarta.persistence.*;
import lombok.*;
import movies.moviesservice.entity.MediaType;
import movies.moviesservice.entity.Movie;

@Entity
@Table(name = "movie_media")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieMedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @Enumerated(EnumType.STRING)
    private MediaType type;

    @Column(nullable = false)
    private String url;
}
