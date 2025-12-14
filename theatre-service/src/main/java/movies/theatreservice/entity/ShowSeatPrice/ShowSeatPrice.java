package movies.theatreservice.entity.ShowSeatPrice;

import jakarta.persistence.*;
import lombok.*;
import movies.theatreservice.entity.SeatType.SeatType;
import movies.theatreservice.entity.Show.Show;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "show_seat_prices")
public class ShowSeatPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // We don't expose this via API usually, but good to have internal ID

    @ManyToOne(optional = false)
    @JoinColumn(name = "show_id")
    private Show show;

    @ManyToOne(optional = false)
    @JoinColumn(name = "seat_type_id")
    private SeatType seatType; // E.g., VIP

    @Column(nullable = false)
    private BigDecimal price; // Always use BigDecimal for money

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant createdAt;


}