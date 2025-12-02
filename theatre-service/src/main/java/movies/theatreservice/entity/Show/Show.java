package movies.theatreservice.entity.Show;

import jakarta.persistence.*;
import lombok.*;
import movies.theatreservice.entity.Screen.Screen;
import movies.theatreservice.entity.ShowSeatPrice.ShowSeatPrice;

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
@Table(name = "shows")
public class Show {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 16, unique = true)
    private String code;

    @ManyToOne(optional = false)
    @JoinColumn(name = "screen_id")
    private Screen screen;

    // We store the Code from the Movie Service, not the object.
    // This allows loose coupling between microservices.
    @Column(nullable = false)
    private String movieCode;

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant startTime;

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant updatedAt;

    // One Show can have different prices for different seat types
    @OneToMany(mappedBy = "show", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<ShowSeatPrice> prices = new HashSet<>();

    @PrePersist
    public void onPrePersist() {
        if (this.code == null) {
            this.code = "sho-" + UUID.randomUUID().toString().replace("-", "").substring(0, 10);
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