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

    // POST /api/v1/users (Manual Registration)
    @PostMapping
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@Valid @RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.createUser(userDTO);
        return new ResponseEntity<>(
                ApiResponse.ok("User registered successfully", createdUser),
                HttpStatus.CREATED
        );
    }

    // GET /api/v1/users/usr-xxxx
    @GetMapping("/{code}")
    public ResponseEntity<ApiResponse<UserDTO>> getUser(@PathVariable String code) {
        UserDTO user = userService.getUserByCode(code);
        return ResponseEntity.ok(ApiResponse.ok("User retrieved", user));
    }

    // PUT /api/v1/users/usr-xxxx (Update Profile)
    @PutMapping("/{code}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(
            @PathVariable String code,
            @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(code, userDTO);
        return ResponseEntity.ok(ApiResponse.ok("User updated successfully", updatedUser));
    }
}
