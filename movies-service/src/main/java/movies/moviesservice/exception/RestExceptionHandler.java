// java
package movies.moviesservice.exception;

import movies.moviesservice.utils.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class RestExceptionHandler {

    private String shortValidationMessage() {
        return "Validation failed";
    }

    private String extractDetail(String primary) {
        if (primary == null || primary.isBlank() || "Validation failed".equals(primary)) {
            return "Validation failed";
        }
        int idx = primary.indexOf(": ");
        return (idx >= 0) ? primary.substring(idx + 2) : primary;
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(ValidationException ex) {
        String primary = ex.getMessage() == null || ex.getMessage().isBlank() ? "Validation failed" : ex.getMessage();
        ApiResponse<Object> resp = ApiResponse.<Object>builder()
                .success(false)
                .message(extractDetail(primary))          // detailed text
                .error(shortValidationMessage())          // generic short text
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodArgNotValid(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<String> errors = new ArrayList<>(fieldErrors.size());
        for (FieldError f : fieldErrors) {
            errors.add(f.getField() + ": " + f.getDefaultMessage());
        }
        String primary = errors.isEmpty() ? "Validation failed" : errors.get(0);
        ApiResponse<Object> resp = ApiResponse.<Object>builder()
                .success(false)
                .message(extractDetail(primary))          // detailed text without "field: "
                .error(shortValidationMessage())          // generic short text
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleConstraintViolation(ConstraintViolationException ex) {
        List<ConstraintViolation<?>> violations = new ArrayList<>(ex.getConstraintViolations());
        List<String> errors = new ArrayList<>(violations.size());
        for (ConstraintViolation<?> v : violations) {
            errors.add(v.getPropertyPath() + ": " + v.getMessage());
        }
        String primary = errors.isEmpty() ? "Validation failed" : errors.get(0);
        ApiResponse<Object> resp = ApiResponse.<Object>builder()
                .success(false)
                .message(extractDetail(primary))          // detailed text without "field: "
                .error(shortValidationMessage())          // generic short text
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
    }
}
