package movies.userservice.dtos.UserDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import movies.userservice.enums.UserStatus;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String code;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    private String displayName;
    private String phone;

    // INPUT ONLY: Client sends it, but we NEVER send it back
    @NotBlank(message = "Password is required")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UserStatus status;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean emailVerified;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant createdAt;
}