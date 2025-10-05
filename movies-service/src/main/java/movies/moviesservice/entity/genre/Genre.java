package movies.moviesservice.entity.genre;

import jakarta.persistence.*;
import lombok.*;
import movies.moviesservice.entity.Movie;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "genres")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "genres")
    @Builder.Default
    private Set<Movie> movies = new HashSet<>();
}

