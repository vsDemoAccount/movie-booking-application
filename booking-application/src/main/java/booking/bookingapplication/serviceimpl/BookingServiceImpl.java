package booking.bookingapplication.serviceimpl;

import booking.bookingapplication.dtos.BookingDTO;
import booking.bookingapplication.entity.Booking;
import booking.bookingapplication.entity.BookingAuditLog;
import booking.bookingapplication.entity.BookingSeat;
import booking.bookingapplication.entity.Payment;
import booking.bookingapplication.enums.BookingStatus;
import booking.bookingapplication.enums.PaymentStatus;
import booking.bookingapplication.exception.DuplicateRecordException;
import booking.bookingapplication.exception.ResourceNotFoundException;
import booking.bookingapplication.repository.BookingAuditLogRepository;
import booking.bookingapplication.repository.BookingRepository;
import booking.bookingapplication.repository.PaymentRepository;
import booking.bookingapplication.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Booking Service Implementation
 * Handles the complete booking flow: Reserve -> Pay -> Confirm -> Cleanup
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final BookingAuditLogRepository auditLogRepository;

    // Business constants
    private static final int BOOKING_EXPIRY_MINUTES = 10;
    private static final BigDecimal TAX_RATE = new BigDecimal("0.18"); // 18% GST
    private static final BigDecimal DEFAULT_SEAT_PRICE = new BigDecimal("200.00"); // Mock price

    /**
     * PHASE 1: CREATE BOOKING - Reserve seats and create pending order
     *
     * Business Logic:
     * 1. Validate user doesn't have duplicate pending booking
     * 2. Calculate pricing (subtotal, tax, total)
     * 3. Create booking with PENDING_PAYMENT status
     * 4. Set 10-minute expiry timer
     * 5. Save seat snapshot with prices
     * 6. Log the action
     */
    @Override
    @Transactional
    public BookingDTO createBooking(BookingDTO request, String userCode) {
        log.info("Creating booking for user={}, show={}, seats={}", userCode, request.getShowCode(), request.getSeatCodes());

        // Step 1: Check for duplicate pending booking
        boolean hasPending = bookingRepository.existsByUserCodeAndShowCodeAndStatus(
                userCode, request.getShowCode(), BookingStatus.PENDING_PAYMENT
        );
        if (hasPending) {
            throw new DuplicateRecordException("You already have a pending booking for this show");
        }

        // Step 2: Calculate pricing
        int seatCount = request.getSeatCodes().size();
        BigDecimal subtotal = DEFAULT_SEAT_PRICE.multiply(new BigDecimal(seatCount));
        BigDecimal tax = subtotal.multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotal.add(tax);

        // Step 3: Create booking entity
        Booking booking = Booking.builder()
                .userCode(userCode)
                .showCode(request.getShowCode())
                .theatreCode("THR-MOCK") // Mock data for now
                .screenCode("SCR-MOCK")
                .status(BookingStatus.PENDING_PAYMENT)
                .subtotalAmount(subtotal)
                .taxAmount(tax)
                .totalAmount(total)
                .expiresAt(Instant.now().plus(BOOKING_EXPIRY_MINUTES, ChronoUnit.MINUTES))
                .seats(new HashSet<>())
                .build();

        // Step 4: Add seat details (snapshot pricing)
        Set<BookingSeat> bookingSeats = createSeatSnapshots(request.getSeatCodes(), booking);
        booking.setSeats(bookingSeats);

        // Step 5: Save to database
        Booking saved = bookingRepository.save(booking);

        // Step 6: Audit log
        logAction(saved.getCode(), "CREATED", "Booking created with " + seatCount + " seats", userCode);

        log.info("Booking created: code={}, total={}", saved.getCode(), total);

        // Step 7: Return response
        return BookingDTO.builder()
                .bookingCode(saved.getCode())
                .showCode(saved.getShowCode())
                .theatreCode(saved.getTheatreCode())
                .screenCode(saved.getScreenCode())
                .bookingStatus(saved.getStatus())
                .subtotal(subtotal)
                .tax(tax)
                .total(total)
                .expiresAt(saved.getExpiresAt())
                .seatCodes(request.getSeatCodes())
                .message("Booking created successfully. Complete payment within 10 minutes.")
                .build();
    }

    /**
     * PHASE 2: INITIATE PAYMENT - Record payment attempt
     *
     * Business Logic:
     * 1. Validate booking exists and is PENDING_PAYMENT
     * 2. Check booking hasn't expired
     * 3. Validate amount matches
     * 4. Create payment record with INITIATED status
     * 5. Generate mock gateway order ID
     */
    @Override
    @Transactional
    public BookingDTO initiatePayment(BookingDTO request) {
        log.info("Initiating payment for booking={}", request.getBookingCode());

        // Step 1: Fetch booking
        Booking booking = bookingRepository.findByCode(request.getBookingCode())
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "code", request.getBookingCode()));

        // Step 2: Validate status
        if (booking.getStatus() != BookingStatus.PENDING_PAYMENT) {
            throw new IllegalStateException("Booking is not in PENDING_PAYMENT status: " + booking.getStatus());
        }

        // Step 3: Check expiry
        if (booking.getExpiresAt().isBefore(Instant.now())) {
            booking.setStatus(BookingStatus.EXPIRED);
            bookingRepository.save(booking);
            throw new IllegalStateException("Booking has expired");
        }

        // Step 4: Validate amount
        if (request.getAmount().compareTo(booking.getTotalAmount()) != 0) {
            throw new IllegalArgumentException(
                    String.format("Amount mismatch. Expected: %.2f, Got: %.2f",
                            booking.getTotalAmount(), request.getAmount())
            );
        }

        // Step 5: Create payment record
        String gatewayOrderId = "GW-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Payment payment = Payment.builder()
                .bookingCode(booking.getCode())
                .method(request.getPaymentMethod())
                .status(PaymentStatus.INITIATED)
                .amount(request.getAmount())
                .upiVpa(request.getUpiVpa())
                .provider("MOCK_GATEWAY")
                .gatewayOrderId(gatewayOrderId)
                .build();

        Payment saved = paymentRepository.save(payment);

        // Step 6: Audit log
        logAction(booking.getCode(), "PAYMENT_INITIATED",
                "Payment initiated via " + request.getPaymentMethod(), booking.getUserCode());

        log.info("Payment initiated: paymentCode={}, gatewayOrderId={}", saved.getCode(), gatewayOrderId);

        return BookingDTO.builder()
                .paymentCode(saved.getCode())
                .gatewayOrderId(gatewayOrderId)
                .amount(saved.getAmount())
                .paymentStatus(saved.getStatus())
                .message("Payment initiated. Complete on gateway page.")
                .build();
    }

    /**
     * PHASE 3: PAYMENT WEBHOOK - Process gateway callback
     *
     * Business Logic:
     * 1. Find payment by code
     * 2. Prevent duplicate processing (idempotency)
     * 3. Update payment status
     * 4. If SUCCESS: Mark booking as CONFIRMED
     * 5. If FAILED: Mark booking as FAILED
     * 6. Log the outcome
     */
    @Override
    @Transactional
    public void processPaymentWebhook(BookingDTO request) {
        log.info("Processing webhook: paymentCode={}, status={}", request.getPaymentCode(), request.getPaymentStatus());

        // Step 1: Find payment
        Payment payment = paymentRepository.findByCode(request.getPaymentCode())
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "code", request.getPaymentCode()));

        // Step 2: Idempotency check
        if (payment.getStatus() != PaymentStatus.INITIATED) {
            log.warn("Payment already processed: code={}, status={}", payment.getCode(), payment.getStatus());
            return;
        }

        // Step 3: Update payment
        payment.setStatus(request.getPaymentStatus());
        payment.setProviderTxnId(request.getProviderTxnId());
        payment.setFailureReason(request.getFailureReason());
        paymentRepository.save(payment);

        // Step 4: Update booking
        Booking booking = bookingRepository.findByCode(payment.getBookingCode())
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "code", payment.getBookingCode()));

        if (request.getPaymentStatus() == PaymentStatus.SUCCESS) {
            // Payment successful
            booking.setStatus(BookingStatus.CONFIRMED);
            bookingRepository.save(booking);

            logAction(booking.getCode(), "CONFIRMED",
                    "Payment successful. TxnId: " + request.getProviderTxnId(), booking.getUserCode());

            log.info("Booking confirmed: code={}", booking.getCode());

        } else if (request.getPaymentStatus() == PaymentStatus.FAILED) {
            // Payment failed
            booking.setStatus(BookingStatus.FAILED);
            bookingRepository.save(booking);

            logAction(booking.getCode(), "PAYMENT_FAILED",
                    "Payment failed: " + request.getFailureReason(), booking.getUserCode());

            log.warn("Payment failed for booking: {}", booking.getCode());
        }
    }

    /**
     * Get booking details with all information
     */
    @Override
    @Transactional(readOnly = true)
    public BookingDTO getBooking(String bookingCode, String userCode) {
        Booking booking = bookingRepository.findByCode(bookingCode)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "code", bookingCode));

        // Authorization check
        if (!booking.getUserCode().equals(userCode)) {
            throw new IllegalStateException("Access denied");
        }

        return mapToDTO(booking);
    }

    /**
     * Get all bookings for a user
     */
    @Override
    @Transactional(readOnly = true)
    public List<BookingDTO> getUserBookings(String userCode) {
        List<Booking> bookings = bookingRepository.findByUserCodeOrderByCreatedAtDesc(userCode);
        return bookings.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    /**
     * Cancel a booking
     */
    @Override
    @Transactional
    public void cancelBooking(String bookingCode, String userCode) {
        Booking booking = bookingRepository.findByCode(bookingCode)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "code", bookingCode));

        if (!booking.getUserCode().equals(userCode)) {
            throw new IllegalStateException("Access denied");
        }

        if (booking.getStatus() != BookingStatus.PENDING_PAYMENT &&
            booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new IllegalStateException("Cannot cancel booking with status: " + booking.getStatus());
        }

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        logAction(bookingCode, "CANCELLED", "User cancelled booking", userCode);
        log.info("Booking cancelled: {}", bookingCode);
    }

    /**
     * PHASE 4: AUTO-EXPIRY - Cleanup expired bookings
     * Called by scheduler every minute
     */
    @Override
    @Transactional
    public int expireOldBookings() {
        List<Booking> expired = bookingRepository.findExpiredBookings(
                BookingStatus.PENDING_PAYMENT, Instant.now()
        );

        for (Booking booking : expired) {
            booking.setStatus(BookingStatus.EXPIRED);
            bookingRepository.save(booking);

            logAction(booking.getCode(), "EXPIRED", "Booking expired due to timeout", "SYSTEM");
            log.info("Expired booking: {}", booking.getCode());
        }

        return expired.size();
    }

    // ==================== HELPER METHODS ====================

    /**
     * Create seat snapshots for booking
     */
    private Set<BookingSeat> createSeatSnapshots(List<String> seatCodes, Booking booking) {
        Set<BookingSeat> seats = new HashSet<>();
        for (int i = 0; i < seatCodes.size(); i++) {
            String seatCode = seatCodes.get(i);
            seats.add(BookingSeat.builder()
                    .booking(booking)
                    .seatCode(seatCode)
                    .rowLabel(extractRow(seatCode))
                    .seatNumber(extractNumber(seatCode))
                    .seatPrice(DEFAULT_SEAT_PRICE)
                    .build());
        }
        return seats;
    }

    /**
     * Map entity to DTO
     */
    private BookingDTO mapToDTO(Booking booking) {
        List<BookingDTO.SeatInfo> seatInfos = booking.getSeats().stream()
                .map(seat -> BookingDTO.SeatInfo.builder()
                        .seatCode(seat.getSeatCode())
                        .rowLabel(seat.getRowLabel())
                        .seatNumber(seat.getSeatNumber())
                        .price(seat.getSeatPrice())
                        .build())
                .collect(Collectors.toList());

        String qr = booking.getStatus() == BookingStatus.CONFIRMED ? booking.getCode() : null;

        return BookingDTO.builder()
                .bookingCode(booking.getCode())
                .userCode(booking.getUserCode())
                .showCode(booking.getShowCode())
                .theatreCode(booking.getTheatreCode())
                .screenCode(booking.getScreenCode())
                .bookingStatus(booking.getStatus())
                .subtotal(booking.getSubtotalAmount())
                .tax(booking.getTaxAmount())
                .total(booking.getTotalAmount())
                .seats(seatInfos)
                .expiresAt(booking.getExpiresAt())
                .createdAt(booking.getCreatedAt())
                .qrCode(qr)
                .build();
    }

    /**
     * Log booking action
     */
    private void logAction(String bookingCode, String action, String message, String actorUserCode) {
        try {
            BookingAuditLog log = BookingAuditLog.builder()
                    .bookingCode(bookingCode)
                    .action(action)
                    .message(message)
                    .actorUserCode(actorUserCode)
                    .build();
            auditLogRepository.save(log);
        } catch (Exception e) {
            log.error("Failed to create audit log", e);
        }
    }

    /**
     * Extract row from seat code (e.g., "A1" -> "A")
     */
    private String extractRow(String seatCode) {
        return seatCode.replaceAll("[0-9]", "");
    }

    /**
     * Extract number from seat code (e.g., "A1" -> 1)
     */
    private Integer extractNumber(String seatCode) {
        String num = seatCode.replaceAll("[^0-9]", "");
        return num.isEmpty() ? 0 : Integer.parseInt(num);
    }
}

