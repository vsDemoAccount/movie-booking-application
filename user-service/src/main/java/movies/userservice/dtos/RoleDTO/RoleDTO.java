package movies.userservice.dtos.RoleDTO;

import lombok.Data;
import java.util.Set;

@Data
public class RoleDTO {
    private String code;
    private String description;
    private boolean systemRole;
    private Set<String> permissions;
    private boolean active;
}