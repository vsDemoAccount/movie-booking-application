package movies.userservice.entity.Role;

import jakarta.persistence.*;
import lombok.*;
import movies.userservice.entity.RolePermission.RolePermission;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // internal

    @Column(nullable = false, unique = true, length = 32)
    private String code; // USER, THEATRE_ADMIN

    private String description;

    @Column(nullable = false)
    private boolean systemRole;

    @Column(nullable = false)
    private boolean active = true;

    @Builder.Default
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RolePermission> permissions = new HashSet<>();

}
