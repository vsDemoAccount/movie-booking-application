package movies.userservice.entity.UserRole;

import jakarta.persistence.*;
import lombok.*;
import movies.userservice.entity.User.User;

import java.time.Instant;

@Entity
@Table(
        name = "user_roles",
        uniqueConstraints = @UniqueConstraint(columnNames = {
                "user_id", "role_code", "scope_ref_code"
        })
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 32)
    private String roleCode; // e.g., THEATRE_ADMIN

    // NULL = System-wide role.
    // "Thea-123" = Role applies only to this specific theatre.
    @Column(length = 16)
    private String scopeRefCode;

    @Column(nullable = false)
    private Instant assignedAt;

    @PrePersist
    void onCreate() {
        this.assignedAt = Instant.now();
    }
}