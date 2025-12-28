package movies.userservice.controller.UserController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import movies.userservice.dtos.UserDTO.UserDTO;
import movies.userservice.service.UserService.UserService;
import movies.userservice.utils.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 🔑 Triggers lazy provisioning
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDTO>> me() {
        return ResponseEntity.ok(
                ApiResponse.ok("Current user", userService.getCurrentUser())
        );
    }

    // 🔒 Update own profile only
    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserDTO>> updateMe(
            @RequestBody UserDTO dto) {

        return ResponseEntity.ok(
                ApiResponse.ok("User updated", userService.updateCurrentUser(dto))
        );
    }
}
