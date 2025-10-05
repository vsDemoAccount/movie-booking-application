package movies.moviesservice.entity.person;

import jakarta.persistence.*;
import lombok.*;
import movies.moviesservice.entity.movieCast.MovieCast;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "persons")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private LocalDate birthDate;
    @Column(length = 2000)
    private String bio;
    private String photoUrl;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<MovieCast> movieRoles = new HashSet<>();
}
