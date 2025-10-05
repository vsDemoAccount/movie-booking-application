package movies.moviesservice.entity.language;

import jakarta.persistence.*;
import lombok.*;
import movies.moviesservice.entity.Movie;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "languages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 6)
    private String code;

    @ManyToMany(mappedBy = "languages")
    @Builder.Default
    private Set<Movie> movies = new HashSet<>();
}

