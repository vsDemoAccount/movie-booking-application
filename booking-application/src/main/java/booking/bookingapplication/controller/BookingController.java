package booking.bookingapplication.controller;

import booking.bookingapplication.dtos.BookingDTO;
import booking.bookingapplication.service.BookingService;
import booking.bookingapplication.utils.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Unified Booking Controller
 * Handles all booking operations through a single DTO
 * DEVELOPMENT MODE: Using mock user codes (no JWT authentication)
 */
@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Validated
@Tag(name = "Booking API", description = "Complete booking lifecycle management")
public class BookingController {

    private final BookingService bookingService;

    // Mock user code for development (replace with JWT extraction in production)
    private static final String MOCK_USER_CODE = "USR-DEV-001";

    /**
     * PHASE 1: Create new booking (Reserve seats)
     * POST /api/bookings
     */
    @PostMapping
    @Operation(summary = "Create booking", description = "Reserve seats and create pending booking")
    public ResponseEntity<ApiResponse<BookingDTO>> createBooking(
            @Validated(BookingDTO.CreateBooking.class) @RequestBody BookingDTO request) {

        // TODO: Extract from JWT in production: String userCode = jwt.getClaimAsString("sub");
        BookingDTO response = bookingService.createBooking(request, MOCK_USER_CODE);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Booking created successfully", response));
    }

    /**
     * PHASE 2: Initiate payment
     * POST /api/bookings/payment
     */
    @PostMapping("/payment")
    @Operation(summary = "Initiate payment", description = "Start payment process for booking")
    public ResponseEntity<ApiResponse<BookingDTO>> initiatePayment(
            @Validated(BookingDTO.InitiatePayment.class) @RequestBody BookingDTO request) {

        BookingDTO response = bookingService.initiatePayment(request);
        return ResponseEntity.ok(ApiResponse.ok("Payment initiated", response));
    }

    /**
     * PHASE 3: Payment webhook (Gateway callback)
     * POST /api/bookings/payment/webhook
     */
    @PostMapping("/payment/webhook")
    @Operation(summary = "Payment webhook", description = "Handle payment gateway callback")
    public ResponseEntity<ApiResponse<Void>> paymentWebhook(
            @RequestBody BookingDTO request) {

        bookingService.processPaymentWebhook(request);
        return ResponseEntity.ok(ApiResponse.ok("Webhook processed", null));
    }

    /**
     * Get booking details
     * GET /api/bookings/{code}
     */
    @GetMapping("/{code}")
    @Operation(summary = "Get booking", description = "Retrieve booking details")
    public ResponseEntity<ApiResponse<BookingDTO>> getBooking(@PathVariable String code) {

        // TODO: Extract from JWT in production
        BookingDTO booking = bookingService.getBooking(code, MOCK_USER_CODE);

        return ResponseEntity.ok(ApiResponse.ok("Booking retrieved", booking));
    }

    /**
     * Get user's bookings
     * GET /api/bookings
     */
    @GetMapping
    @Operation(summary = "Get my bookings", description = "Retrieve all user bookings")
    public ResponseEntity<ApiResponse<List<BookingDTO>>> getMyBookings() {

        // TODO: Extract from JWT in production
        List<BookingDTO> bookings = bookingService.getUserBookings(MOCK_USER_CODE);

        return ResponseEntity.ok(ApiResponse.ok("Bookings retrieved", bookings));
    }

    /**
     * Cancel booking
     * DELETE /api/bookings/{code}
     */
    @DeleteMapping("/{code}")
    @Operation(summary = "Cancel booking", description = "Cancel a booking")
    public ResponseEntity<ApiResponse<Void>> cancelBooking(@PathVariable String code) {

        // TODO: Extract from JWT in production
        bookingService.cancelBooking(code, MOCK_USER_CODE);

        return ResponseEntity.ok(ApiResponse.ok("Booking cancelled", null));
    }
}
