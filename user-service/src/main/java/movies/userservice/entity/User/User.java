package movies.userservice.entity.User;

import jakarta.persistence.*;
import lombok.*;
import movies.userservice.enums.UserStatus;
import movies.userservice.utils.CodeGeneratorUtil;

import java.time.Instant;

@Entity
@Table(name = "users",
        indexes = @Index(name = "idx_keycloak_id", columnList = "keycloakId"))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Internal DB ID

    // This is the Public ID (usr-xxxx) you send to Booking/Theatre Service
    @Column(nullable = false, length = 16, unique = true)
    private String code;

    // THE CRITICAL LINK TO KEYCLOAK
//    @Column(nullable = false, unique = true)
//    private String keycloakId; // UUID from Keycloak

    // NULLABLE for now. Will be populated when we integrate Keycloak.
    @Column(unique = true)
    private String keycloakId;

    @Column(nullable = false, unique = true)
    private String email;

    private boolean phoneVerified;

    private String displayName;

    // Temporary: For manual login until Keycloak is ready
    @Column(nullable = false)
    private String passwordHash;

//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private UserStatus status; // ACTIVE, SUSPENDED (Synced from Keycloak)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private UserStatus status = UserStatus.ACTIVE;

    private boolean emailVerified; // Synced from Keycloak

    @Column(unique = true)
    private String phone;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist
    void onCreate() {
        if (this.code == null) {
            this.code =  CodeGeneratorUtil.generate("use", 12);
        }
        Instant now = Instant.now();
        if (this.createdAt == null) this.createdAt = now;
        this.updatedAt = now;
        if (this.status == null) this.status = UserStatus.ACTIVE;
    }

    @PreUpdate
    public void onPreUpdate() {
        this.updatedAt = Instant.now();
    }
}
