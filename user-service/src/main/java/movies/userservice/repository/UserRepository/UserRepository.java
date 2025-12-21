package movies.userservice.repository.UserRepository;

import movies.userservice.entity.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Used by Frontend/Booking Service to get user details
    Optional<User> findByCode(String code);

    // Used by Kafka Consumer to sync Keycloak events
    Optional<User> findByKeycloakId(String keycloakId);

    // Fail-safe lookup
    Optional<User> findByEmail(String email);

    // Validation checks
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}