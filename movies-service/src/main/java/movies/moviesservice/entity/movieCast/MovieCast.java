// java
package movies.moviesservice.entity.movieCast;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import movies.moviesservice.entity.CastRole;
import movies.moviesservice.entity.person.Person;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "movie_cast")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MovieCast {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 16, unique = true)
    private String code;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    @JsonBackReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private movies.moviesservice.entity.Movie movie;

    @ManyToOne(optional = false)
    @JoinColumn(name = "person_id")
    private Person person;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private movies.moviesservice.entity.CastRole role;

    private String characterName;

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant updatedAt;

    @PrePersist
    public void onPrePersist() {
        if (this.code == null) {
            this.code = "mc-" + UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        }
        if (this.createdAt == null) {
            this.createdAt = Instant.now(); // stored in UTC
        }
    }
    @PreUpdate
    public void onPreUpdate() {
        this.updatedAt = Instant.now();
    }
}
