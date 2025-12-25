package movies.userservice.entity.Permission;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "permissions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 64)
    private String code;

    private String description;
}


//Enforce ROLE_ASSIGN permission in backend
//
//Return 403 for unauthorized role changes
//
// (Optional) Duplicate check at API Gateway
//
//Add audit logs for role changes
//
//If you want, next I can:
//
//Show the minimal code change to close this hole
//
//Explain how Keycloak roles map to your permissions
//
//Design a safe admin-only role management flow