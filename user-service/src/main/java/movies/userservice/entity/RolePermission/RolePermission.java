package movies.userservice.entity.RolePermission;

import jakarta.persistence.*;
import lombok.*;
import movies.userservice.entity.Permission.Permission;
import movies.userservice.entity.Role.Role;

@Entity
@Table(
        name = "role_permissions",
        uniqueConstraints = @UniqueConstraint(columnNames = {"role_code", "permission_code"})
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolePermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id")
    private Permission permission;
}
