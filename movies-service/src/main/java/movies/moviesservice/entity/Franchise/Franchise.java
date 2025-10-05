package movies.moviesservice.entity.Franchise;

import jakarta.persistence.*;
import lombok.*;
import movies.moviesservice.entity.Movie;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "franchises")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Franchise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 2000)
    private String description;

    @OneToMany(mappedBy = "franchise")
    @Builder.Default
    private Set<Movie> movies = new HashSet<>();
}

