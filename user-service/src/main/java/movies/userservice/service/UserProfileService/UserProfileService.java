package movies.userservice.service.UserProfileService;


import movies.userservice.dtos.UserProfileDTO.UserProfileDTO;
import org.springframework.web.multipart.MultipartFile;

public interface UserProfileService {

    /**
     * Get profile details (including avatar URL and preferences)
     */
    UserProfileDTO getProfile(String userCode);

    /**
     * Update text-based profile fields (City, Locale, Timezone)
     */
    UserProfileDTO updateProfile(String userCode, UserProfileDTO dto);

    /**
     * Upload an avatar image to S3 and update the profile URL
     */
    UserProfileDTO uploadAvatar(String userCode, MultipartFile file);
}
