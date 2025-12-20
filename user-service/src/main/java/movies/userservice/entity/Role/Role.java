package movies.userservice.entity.Role;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(nullable = false, length = 32, unique = true)
    private String code;
    // USER, THEATRE_ADMIN, THEATRE_STAFF, PLATFORM_ADMIN

    private String description;

    @Column(nullable = false)
    private boolean systemRole;
}
