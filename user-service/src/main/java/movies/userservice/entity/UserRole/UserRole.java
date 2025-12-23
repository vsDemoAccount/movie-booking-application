package movies.userservice.entity.UserRole;

import jakarta.persistence.*;
import lombok.*;
import movies.userservice.entity.Role.Role;
import movies.userservice.entity.User.User;

import java.time.Instant;


@Entity
@Table(
        name = "user_roles",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"user_id", "role_id", "scope_ref_code"}
        )
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
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(length = 16)
    private String scopeRefCode;

    @Column(nullable = false)
    private Instant assignedAt;
}
