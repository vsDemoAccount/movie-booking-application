package booking.bookingapplication.dtos;

import booking.bookingapplication.enums.BookingStatus;
import booking.bookingapplication.enums.PaymentMethod;
import booking.bookingapplication.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Unified DTO for all Booking operations
 * Used for both Request and Response to minimize DTO proliferation
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingDTO {

    // ==================== BOOKING FIELDS ====================
    private String bookingCode;
    private String userCode;

    @NotBlank(message = "Show code is required", groups = CreateBooking.class)
    private String showCode;

    private String theatreCode;
    private String screenCode;

    private BookingStatus bookingStatus;

    // Price breakdown
    private BigDecimal subtotal;
    private BigDecimal tax;
    private BigDecimal total;

    // Seat selection
    @NotEmpty(message = "At least one seat required", groups = CreateBooking.class)
    @Size(max = 10, message = "Maximum 10 seats per booking", groups = CreateBooking.class)
    private List<String> seatCodes;

    private List<SeatInfo> seats;

    // Timing
    private Instant expiresAt;
    private Instant createdAt;

    // ==================== PAYMENT FIELDS ====================
    private String paymentCode;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;

    @DecimalMin(value = "0.01", message = "Amount must be positive", groups = InitiatePayment.class)
    private BigDecimal amount;

    private String upiVpa;
    private String gatewayOrderId;
    private String providerTxnId;
    private String failureReason;

    // ==================== RESPONSE METADATA ====================
    private String message;
    private String qrCode; // For confirmed bookings

    // ==================== NESTED SEAT INFO ====================
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SeatInfo {
        private String seatCode;
        private String rowLabel;
        private Integer seatNumber;
        private BigDecimal price;
    }

    // ==================== VALIDATION GROUPS ====================
    public interface CreateBooking {}
    public interface InitiatePayment {}
    public interface PaymentWebhook {}
}

