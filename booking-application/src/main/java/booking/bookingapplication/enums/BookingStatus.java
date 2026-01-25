package booking.bookingapplication.enums;

public enum BookingStatus {
    PENDING_PAYMENT,   // Booking created, seats locked, waiting for money
    CONFIRMED,         // Payment success, ticket issued
    CANCELLED,         // User or Admin cancelled
    EXPIRED,           // Payment time limit reached (locks released)
    FAILED             // Payment failed explicitly
}