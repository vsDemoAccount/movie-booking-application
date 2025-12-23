package movies.userservice.service.RoleService;

import movies.userservice.dtos.AssignRoleRequest.AssignRoleRequest;
import movies.userservice.dtos.RoleDTO.RoleDTO;
import movies.userservice.dtos.UserRoleDTO.UserRoleDTO;

import java.util.List;
import java.util.Set;

public interface RoleService {

    RoleDTO createRole(RoleDTO dto);

    RoleDTO updateRole(String roleCode, RoleDTO dto);

    void disableRole(String roleCode);

    void assignRoleToUser(String userCode, AssignRoleRequest request);

    void revokeRoleFromUser(String userCode, AssignRoleRequest request);

    Set<UserRoleDTO> getUserRoles(String userCode);

    List<RoleDTO> getAllRoles();

    List<String> getAllPermissions();
}
