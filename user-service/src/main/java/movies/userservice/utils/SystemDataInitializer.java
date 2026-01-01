package movies.userservice.utils;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movies.userservice.entity.Permission.Permission;
import movies.userservice.entity.Role.Role;
import movies.userservice.entity.RolePermission.RolePermission;
import movies.userservice.repository.PermissionRepository.PermissionRepository;
import movies.userservice.repository.RoleRepository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Set;


@Component
@RequiredArgsConstructor
@Slf4j
public class SystemDataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("Checking System Data seeding...");

        // 1. Create Core Permissions
        createPermission("ROLE_MANAGE");      // Required to assign roles
        createPermission("THEATRE_CREATE");   // For Owners
        createPermission("SHOW_MANAGE");      // For Managers

        // 2. Create ROLE_USER (Default)
        createRole("ROLE_USER", "Default user role", false, Set.of());

        // 3. Create ROLE_PLATFORM_ADMIN (Super User)
        // This MUST exist so you can manually assign it to yourself via SQL later
        createRole("ROLE_PLATFORM_ADMIN", "Super Admin", true,
                Set.of("ROLE_MANAGE", "THEATRE_CREATE", "SHOW_MANAGE"));

        // 4. Create Business Roles (Optional seeding)
        createRole("ROLE_THEATRE_OWNER", "Partner", false,
                Set.of("THEATRE_CREATE"));

        log.info("System Data seeding check complete.");
    }

    private void createPermission(String code) {
        if (permissionRepository.findByCode(code).isEmpty()) {
            permissionRepository.save(Permission.builder().code(code).description("Auto-seeded").build());
            log.info("Seeded Permission: {}", code);
        }
    }

    private void createRole(String code, String desc, boolean isSystem, Set<String> permCodes) {
        if (roleRepository.findByCode(code).isEmpty()) {
            Role role = Role.builder()
                    .code(code)
                    .description(desc)
                    .systemRole(isSystem)
                    .active(true)
                    .build();

            // Attach Permissions
            for (String permCode : permCodes) {
                Permission p = permissionRepository.findByCode(permCode).orElseThrow();
                role.getPermissions().add(RolePermission.builder().role(role).permission(p).build());
            }

            roleRepository.save(role);
            log.info("Seeded Role: {}", code);
        }
    }
}