package booking.bookingapplication.utils;


import booking.bookingapplication.exception.DuplicateRecordException;
import booking.bookingapplication.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Handle Resource Not Found (The 404 you are looking for)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        // This converts the Java Exception into your custom JSON
        return new ResponseEntity<>(
                ApiResponse.error(ex.getMessage(), null),
                HttpStatus.NOT_FOUND
        );
    }

    // 2. Handle Generic Errors (Fallback)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGlobalException(Exception ex) {
        return new ResponseEntity<>(
                ApiResponse.error(ex.getMessage(), null),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(DuplicateRecordException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicateRecord(DuplicateRecordException ex) {
        // 409 Conflict is the correct status for double-booking
        return new ResponseEntity<>(ApiResponse.error(ex.getMessage(), 409), HttpStatus.CONFLICT);
    }
}
