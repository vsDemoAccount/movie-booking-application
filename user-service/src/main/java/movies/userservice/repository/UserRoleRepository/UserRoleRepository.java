package movies.userservice.repository.UserRoleRepository;


import movies.userservice.entity.Role.Role;
import movies.userservice.entity.User.User;
import movies.userservice.entity.UserRole.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    boolean existsByUserAndRoleAndScopeRefCode(
            User user, Role role, String scopeRefCode
    );

    void deleteByUserAndRoleAndScopeRefCode(
            User user, Role role, String scopeRefCode
    );

    List<UserRole> findByUser(User user);
}