package movies.moviesservice.entity.movieCast;

import jakarta.persistence.*;
import lombok.*;
import movies.moviesservice.entity.CastRole;
import movies.moviesservice.entity.Movie;
import movies.moviesservice.entity.person.Person;

@Entity
@Table(name = "movie_cast")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieCast {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne(optional = false)
    @JoinColumn(name = "person_id")
    private Person person;

    @Enumerated(EnumType.STRING)
    private CastRole role;

    private String characterName;
}
