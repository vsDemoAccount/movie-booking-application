package movies.userservice.repository.UserRoleRepository;


import movies.userservice.entity.Role.Role;
import movies.userservice.entity.User.User;
import movies.userservice.entity.UserRole.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    boolean existsByUserAndRoleAndScopeRefCode(
            User user, Role role, String scopeRefCode
    );

    @Query("SELECT ur FROM UserRole ur WHERE ur.user = :user AND ur.role.code = :roleCode AND (:scopeRefCode IS NULL OR ur.scopeRefCode = :scopeRefCode)")
    Optional<UserRole> findByUserAndRoleCodeAndScopeRefCode(User user, String roleCode, String scopeRefCode);

    List<UserRole> findByUser(User user);
}