package movies.theatreservice.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "success", "message", "error", "data", "timestamp" })
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private Object error;
    private T data;
    private LocalDateTime timestamp;

    public static <T> ApiResponse<T> ok(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> error(String message, Object errorData) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .error(errorData)
                .timestamp(LocalDateTime.now())
                .build();
    }
}