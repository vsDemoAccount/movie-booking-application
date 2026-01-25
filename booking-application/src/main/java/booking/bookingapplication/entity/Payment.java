package booking.bookingapplication.entity;


import booking.bookingapplication.enums.PaymentMethod;
import booking.bookingapplication.enums.PaymentStatus;
import booking.bookingapplication.utils.CodeGeneratorUtil;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "payments",
        indexes = {
                @Index(name = "idx_payment_booking", columnList = "bookingCode"),
                @Index(name = "idx_payment_status", columnList = "status"),
                @Index(name = "idx_payment_provider_txn", columnList = "providerTxnId")
        })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Public ID: "pay-xxxxx"
    @Column(nullable = false, unique = true, length = 30)
    private String code;

    // Reference to Booking (Loose Coupling)
    @Column(nullable = false, length = 30)
    private String bookingCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    // --- UPI SPECIFIC ---
    @Column(length = 100)
    private String upiVpa;

    @Column(length = 50)
    private String provider; // "RAZORPAY", "PHONEPE"

    @Column(length = 100)
    private String providerTxnId; // The Bank Reference Number (UTR)

    @Column(length = 100)
    private String gatewayOrderId;

    @Column(length = 500)
    private String failureReason;

    // --- TIMESTAMPS ---
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @Version // Optimistic Lock for duplicate webhooks
    private Long version;

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;

        if (this.status == null) this.status = PaymentStatus.INITIATED;
        if (this.method == null) this.method = PaymentMethod.UPI;
        if (this.code == null) this.code = CodeGeneratorUtil.generate("pay", 12);
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
