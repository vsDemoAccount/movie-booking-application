package booking.bookingapplication.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "booking_audit_logs",
        indexes = @Index(name = "idx_audit_booking", columnList = "bookingCode"))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String bookingCode;

    @Column(nullable = false, length = 50)
    private String action; // "CREATED", "PAYMENT_RECEIVED", "EXPIRED"

    @Column(length = 255)
    private String message;

    @Column(length = 50)
    private String actorUserCode; // Who did it?

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void onCreate() {
        this.createdAt = Instant.now();
    }
}
