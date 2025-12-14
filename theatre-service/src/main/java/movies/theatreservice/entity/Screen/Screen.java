package movies.theatreservice.entity.Screen;


import jakarta.persistence.*;
import lombok.*;
import movies.theatreservice.entity.Seat.Seat;
import movies.theatreservice.entity.Theatre.Theatre;
import movies.theatreservice.utils.CodeGeneratorUtil;

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
@Table(name = "screens")
public class Screen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 16, unique = true)
    private String code;

    @Column(nullable = false)
    private String name; // E.g., "Audi 1", "IMAX Screen"

    @ManyToOne(optional = false)
    @JoinColumn(name = "theatre_id")
    private Theatre theatre;

    @OneToMany(mappedBy = "screen", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Seat> seats = new HashSet<>();

    // Supports different technologies (e.g., "Dolby Atmos", "IMAX")
    // Using a simple collection for scalability without complexity
    @ElementCollection
    @CollectionTable(name = "screen_features", joinColumns = @JoinColumn(name = "screen_id"))
    @Column(name = "feature")
    @Builder.Default
    private Set<String> features = new HashSet<>();

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant updatedAt;

    @PrePersist
    public void onPrePersist() {
        if (this.code == null) {
            // UPDATED: Using the utility for safer, cleaner generation
            // "Cit-" is 4 chars, leaving 12 chars for randomness (Total 16)
            this.code = CodeGeneratorUtil.generate("Scr", 12);
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
