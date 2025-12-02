package movies.theatreservice.entity.Seat;

import jakarta.persistence.*;
import lombok.*;
import movies.theatreservice.entity.Screen.Screen;
import movies.theatreservice.entity.SeatType.SeatType;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "seats")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 16, unique = true)
    private String code;

    @ManyToOne(optional = false)
    @JoinColumn(name = "screen_id")
    private Screen screen;

    @ManyToOne(optional = false)
    @JoinColumn(name = "seat_type_id")
    private SeatType seatType;

    @Column(nullable = false)
    private String rowName; // E.g., "A", "B"

    @Column(nullable = false)
    private int seatNumber; // E.g., 1, 2

    // Frontend Grid Mapping - Essential for drawing the seat map UI
    // Example: Row A might be Grid Row 0, Seat 1 might be Grid Col 5 (to account for aisle gap)
    private int gridRow;
    private int gridCol;

    @Column(name = "is_active")
    @Builder.Default
    private boolean isActive = true; // For damaged seats

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant updatedAt;

    @PrePersist
    public void onPrePersist() {
        if (this.code == null) {
            this.code = "set-" + UUID.randomUUID().toString().replace("-", "").substring(0, 10);
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