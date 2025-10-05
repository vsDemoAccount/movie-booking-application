package movies.moviesservice.entity.Tag;

import jakarta.persistence.*;
import lombok.*;
import movies.moviesservice.entity.Movie;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tags")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "tags")
    @Builder.Default
    private Set<Movie> movies = new HashSet<>();
}
