package booking.bookingapplication.entity;

import booking.bookingapplication.enums.BookingStatus;
import booking.bookingapplication.utils.CodeGeneratorUtil;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "bookings",
        indexes = {
                @Index(name = "idx_booking_code", columnList = "code", unique = true),
                @Index(name = "idx_booking_user", columnList = "userCode"),
                @Index(name = "idx_booking_status", columnList = "status") // Helpful for cleanup jobs
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Public ID: "bok-123456"
    @Column(nullable = false, unique = true, length = 30)
    private String code;

    @Column(nullable = false, length = 50)
    private String userCode; // "usr-uuid"

    @Column(nullable = false, length = 50)
    private String showCode;

    @Column(nullable = false, length = 20)
    private String theatreCode;

    @Column(nullable = false, length = 20)
    private String screenCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BookingStatus status;

    // --- MONEY (Using BigDecimal is excellent) ---
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotalAmount;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal taxAmount;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;

    // --- TIMINGS ---
    private Instant expiresAt; // Important for "Release Seat" scheduler

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    // --- CONCURRENCY PROTECTION ---
    @Version
    private Long version;

    // --- RELATIONS ---
    @Builder.Default
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BookingSeat> seats = new HashSet<>();

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;

        if (this.status == null) this.status = BookingStatus.PENDING_PAYMENT;
        if (this.code == null) this.code = CodeGeneratorUtil.generate("bok", 12);
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
