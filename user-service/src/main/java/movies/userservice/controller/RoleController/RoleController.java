package movies.userservice.controller.RoleController;


import lombok.RequiredArgsConstructor;
import movies.userservice.dtos.AssignRoleRequest.AssignRoleRequest;
import movies.userservice.dtos.RoleDTO.RoleDTO;
import movies.userservice.dtos.UserRoleDTO.UserRoleDTO;
import movies.userservice.service.RoleService.RoleService;
import movies.userservice.utils.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;


@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<ApiResponse<RoleDTO>> create(@RequestBody RoleDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Role created", roleService.createRole(dto)));
    }

    @PutMapping("/{roleCode}")
    public ResponseEntity<ApiResponse<RoleDTO>> update(
            @PathVariable String roleCode,
            @RequestBody RoleDTO dto) {

        return ResponseEntity.ok(
                ApiResponse.ok("Role updated", roleService.updateRole(roleCode, dto)));
    }

    @DeleteMapping("/{roleCode}")
    public ResponseEntity<ApiResponse<Void>> disable(@PathVariable String roleCode) {
        roleService.disableRole(roleCode);
        return ResponseEntity.ok(ApiResponse.ok("Role disabled", null));
    }

    @PostMapping("/users/{userCode}")
    public ResponseEntity<ApiResponse<Void>> assign(
            @PathVariable String userCode,
            @RequestBody AssignRoleRequest req) {

        roleService.assignRoleToUser(userCode, req);
        return ResponseEntity.ok(ApiResponse.ok("Role assigned", null));
    }

    @DeleteMapping("/users/{userCode}")
    public ResponseEntity<ApiResponse<Void>> revoke(
            @PathVariable String userCode,
            @RequestBody AssignRoleRequest req) {

        roleService.revokeRoleFromUser(userCode, req);
        return ResponseEntity.ok(ApiResponse.ok("Role revoked", null));
    }

    @GetMapping("/users/{userCode}")
    public ResponseEntity<ApiResponse<Set<UserRoleDTO>>> userRoles(
            @PathVariable String userCode) {

        return ResponseEntity.ok(
                ApiResponse.ok("User roles", roleService.getUserRoles(userCode)));
    }
}
