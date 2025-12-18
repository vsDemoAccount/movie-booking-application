package movies.theatreservice.entity.Show;

import jakarta.persistence.*;
import lombok.*;

import movies.theatreservice.entity.CatalogMovie_kfk.CatalogMovie;
import movies.theatreservice.entity.Screen.Screen;
import movies.theatreservice.entity.ShowSeatPrice.ShowSeatPrice;
import movies.theatreservice.enums.ShowStatus;
import movies.theatreservice.utils.CodeGeneratorUtil;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

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

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "screen_id", nullable = false)
    private Screen screen;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "movie_code", referencedColumnName = "code", nullable = false)
    private CatalogMovie movie;

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant endTime;

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant startTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ShowStatus status = ShowStatus.SCHEDULED;

    // Helper method (depends on 'movie' being present)
    public Instant getEndTime() {
        if (this.movie == null || this.startTime == null) return null;
        return this.startTime.plusSeconds(this.movie.getDurationMinutes() * 60L);
    }

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant updatedAt;

    @OneToMany(mappedBy = "show", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<ShowSeatPrice> prices = new HashSet<>();

    @PrePersist
    public void onPrePersist() {
        if (this.code == null) {
            this.code = CodeGeneratorUtil.generate("Sho", 12);
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