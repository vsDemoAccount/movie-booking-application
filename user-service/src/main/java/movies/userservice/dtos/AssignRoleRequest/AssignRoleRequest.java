package movies.userservice.dtos.AssignRoleRequest;

import lombok.Data;

@Data
public class AssignRoleRequest {
    private String roleCode;      // e.g., "THEATRE_ADMIN"
    private String scopeRefCode;  // e.g., "Thea-123" (Optional, null for global roles)
}