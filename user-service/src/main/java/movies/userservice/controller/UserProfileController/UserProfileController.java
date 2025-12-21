package movies.userservice.controller.UserProfileController;


import lombok.RequiredArgsConstructor;
import movies.userservice.dtos.UserProfileDTO.UserProfileDTO;
import movies.userservice.serviceimpl.UserProfileServiceImpl.UserProfileServiceImpl;
import movies.userservice.utils.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/users/{code}/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileServiceImpl userProfileService;

    // GET Profile
    @GetMapping
    public ResponseEntity<ApiResponse<UserProfileDTO>> getProfile(@PathVariable String code) {
        return ResponseEntity.ok(ApiResponse.ok("Profile retrieved", userProfileService.getProfile(code)));
    }

    // PUT Update Settings (City, Locale)
    @PutMapping
    public ResponseEntity<ApiResponse<UserProfileDTO>> updateProfile(
            @PathVariable String code,
            @RequestBody UserProfileDTO dto) {
        return ResponseEntity.ok(ApiResponse.ok("Profile updated", userProfileService.updateProfile(code, dto)));
    }

    // POST Upload Avatar
    @PostMapping(value = "/avatar", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<UserProfileDTO>> uploadAvatar(
            @PathVariable String code,
            @RequestParam("file") MultipartFile file) {

        UserProfileDTO updatedProfile = userProfileService.uploadAvatar(code, file);
        return ResponseEntity.ok(ApiResponse.ok("Avatar uploaded successfully", updatedProfile));
    }
}