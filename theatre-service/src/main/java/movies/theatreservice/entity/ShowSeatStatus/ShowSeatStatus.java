package movies.theatreservice.entity.ShowSeatStatus;

import jakarta.persistence.*;
import lombok.*;
import movies.theatreservice.entity.Seat.Seat;
import movies.theatreservice.entity.Show.Show;
import movies.theatreservice.enums.BookingStatus;

import java.time.Instant;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "show_seat_statuses",
        uniqueConstraints = @UniqueConstraint(columnNames = {"show_id", "seat_id"}))
public class ShowSeatStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "show_id", nullable = false)
    private Show show;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    // Stores the ID from the Booking Service (e.g., "Bk-1234")
    // This lets us know WHO locked the seat.
    @Column(nullable = false)
    private String bookingRefId;

    @Column(nullable = false)
    private Instant lockedAt;
}