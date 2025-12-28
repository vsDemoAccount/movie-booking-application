package movies.userservice.serviceimpl.UserProvisioningService;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import movies.userservice.entity.Role.Role;
import movies.userservice.entity.User.User;
import movies.userservice.entity.UserRole.UserRole;
import movies.userservice.enums.UserStatus;
import movies.userservice.repository.RoleRepository.RoleRepository;
import movies.userservice.repository.UserRepository.UserRepository;
import movies.userservice.repository.UserRoleRepository.UserRoleRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.Instant;


@Service
@RequiredArgsConstructor
public class UserProvisioningService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    @Transactional
    public User getOrCreate(Jwt jwt) {

        return userRepository.findByKeycloakId(jwt.getSubject())
                .orElseGet(() -> {

                    User user = new User();
                    user.setKeycloakId(jwt.getSubject());
                    user.setEmail(jwt.getClaimAsString("email"));
                    user.setDisplayName(jwt.getClaimAsString("preferred_username"));
                    user.setStatus(UserStatus.ACTIVE);

                    user = userRepository.save(user);

                    // ASSIGN DEFAULT ROLE
                    Role userRole = roleRepository.findByCode("ROLE_USER")
                            .orElseThrow(() ->
                                    new IllegalStateException("ROLE_USER not seeded"));

                    userRoleRepository.save(
                            UserRole.builder()
                                    .user(user)
                                    .role(userRole)
                                    .assignedAt(Instant.now())
                                    .build()
                    );

                    return user;
                });
    }
}


