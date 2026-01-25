package booking.bookingapplication.enums;


public enum PaymentStatus {
    INITIATED,         // Created, sent to gateway
    SUCCESS,           // Money received
    FAILED,            // Money not received/declined
    REFUNDED,          // Money returned to user
    CANCELLED          // User closed the payment page
}
