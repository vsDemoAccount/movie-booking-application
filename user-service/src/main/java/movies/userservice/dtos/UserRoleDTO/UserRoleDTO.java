package movies.userservice.dtos.UserRoleDTO;

import lombok.Data;

import java.util.Set;

@Data
public class UserRoleDTO {
    private String roleCode;
    private String scopeRefCode;
    private Set<String> permissions;
}