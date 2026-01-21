package movies.moviesservice.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheckPermissionRequest {
    private String userId;
    private String permissionCode;
    private String scopeRefCode;
}