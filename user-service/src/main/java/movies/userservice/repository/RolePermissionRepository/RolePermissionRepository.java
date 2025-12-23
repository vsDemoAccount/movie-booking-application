package movies.userservice.repository.RolePermissionRepository;

import movies.userservice.entity.Role.Role;
import movies.userservice.entity.RolePermission.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    void deleteByRole(Role role);
}
