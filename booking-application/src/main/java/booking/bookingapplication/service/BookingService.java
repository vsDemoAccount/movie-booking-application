package booking.bookingapplication.service;

import booking.bookingapplication.dtos.BookingDTO;

import java.util.List;

/**
 * Unified Booking Service Interface
 * Handles the complete booking lifecycle: Reserve -> Pay -> Confirm
 */
public interface BookingService {

    /**
     * PHASE 1: Create booking and lock seats
     * Input: showCode, seatCodes
     * Output: bookingCode, total amount, expiry time
     */
    BookingDTO createBooking(BookingDTO request, String userCode);

    /**
     * PHASE 2: Initiate payment
     * Input: bookingCode, paymentMethod, amount
     * Output: paymentCode, gatewayOrderId
     */
    BookingDTO initiatePayment(BookingDTO request);

    /**
     * PHASE 3: Handle payment webhook (callback from gateway)
     * Input: paymentCode, status, providerTxnId
     * Updates booking status to CONFIRMED or FAILED
     */
    void processPaymentWebhook(BookingDTO request);

    /**
     * Get booking details by code
     */
    BookingDTO getBooking(String bookingCode, String userCode);

    /**
     * Get all bookings for a user
     */
    List<BookingDTO> getUserBookings(String userCode);

    /**
     * Cancel a booking
     */
    void cancelBooking(String bookingCode, String userCode);

    /**
     * PHASE 4: Auto-expire old bookings (called by scheduler)
     * Returns count of expired bookings
     */
    int expireOldBookings();
}

