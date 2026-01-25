package booking.bookingapplication.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "booking_seats",
        uniqueConstraints = @UniqueConstraint(columnNames = {"booking_id", "seatCode"}),
        indexes = {
                @Index(name = "idx_bookingseat_seat", columnList = "seatCode")
        })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @Column(nullable = false, length = 50)
    private String seatCode;  // Can be "A1" or a UUID, kept it flexible

    @Column(nullable = false, length = 10)
    private String rowLabel;  // "A", "B"

    @Column(nullable = false)
    private Integer seatNumber;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal seatPrice;
}