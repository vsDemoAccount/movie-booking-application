package movies.theatreservice.entity.Theatre;


import jakarta.persistence.*;
import lombok.*;
import movies.theatreservice.entity.Screen.Screen;
import movies.theatreservice.entity.city.City;
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
@Table(name = "theatres")
public class Theatre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 16, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(length = 2000)
    private String description; // E.g., "IMAX available, Parking available"

    @ManyToOne(optional = false)
    @JoinColumn(name = "city_id")
    private City city;

    @OneToMany(mappedBy = "theatre", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Screen> screens = new HashSet<>();

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant updatedAt;

    @PrePersist
    public void onPrePersist() {
        if (this.code == null) {
            // UPDATED: Using the utility for safer, cleaner generation
            // "Cit-" is 4 chars, leaving 12 chars for randomness (Total 16)
            this.code = CodeGeneratorUtil.generate("Thea", 11);
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