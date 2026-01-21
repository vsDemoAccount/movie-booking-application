package movies.userservice.dtos.CheckPermissionRequest;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CheckPermissionRequest {

    @NotBlank(message = "User ID/Code is required")
    private String userId;        // e.g., "usr-123456"

    @NotBlank(message = "Permission code is required")
    private String permissionCode; // e.g., "SHOW_DELETE"

    // Optional: If null, checks for global permission.
    // If provided, checks for specific theatre scope.
    private String scopeRefCode;   // e.g., "th-mumbai-01"
}