package movies.theatreservice.entity.city;

import jakarta.persistence.*;
import lombok.*;
import movies.theatreservice.entity.Theatre.Theatre;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cities")
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 16, unique = true)
    private String code;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String state; // Can be an entity later if needed, String is fine for mid-level

    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Theatre> theatres = new HashSet<>();

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant updatedAt;

    @PrePersist
    public void onPrePersist() {
        if (this.code == null) {
            this.code = "Cit-" + UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        }
        Instant now = Instant.now();
        if (this.createdAt == null) this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void onPreUpdate() {
        this.updatedAt = Instant.now();
    }
}