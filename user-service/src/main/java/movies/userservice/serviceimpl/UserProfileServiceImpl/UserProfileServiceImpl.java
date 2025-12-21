package movies.userservice.serviceimpl.UserProfileServiceImpl;

import lombok.RequiredArgsConstructor;
import movies.userservice.dtos.UserProfileDTO.UserProfileDTO;
import movies.userservice.entity.User.User;
import movies.userservice.entity.UserProfile.UserProfile;

import movies.userservice.exception.ResourceNotFoundException;
import movies.userservice.mappers.UserProfileMapper.UserProfileMapper;
import movies.userservice.repository.UserProfileRepository.UserProfileRepository;
import movies.userservice.repository.UserRepository.UserRepository;
import movies.userservice.service.FileStorageService.FileStorageService;
import movies.userservice.service.UserProfileService.UserProfileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;
    private final FileStorageService fileStorageService;

    @Override
    @Transactional
    public UserProfileDTO getProfile(String userCode) {
        User user = getUserOrThrow(userCode);
        return userProfileRepository.findById(user.getId())
                .map(userProfileMapper::toDTO)
                .orElseGet(() -> {
                    // Lazy create empty profile
                    UserProfile newProfile = UserProfile.builder().user(user).build();
                    return userProfileMapper.toDTO(userProfileRepository.save(newProfile));
                });
    }

    @Override
    @Transactional
    public UserProfileDTO updateProfile(String userCode, UserProfileDTO dto) {
        User user = getUserOrThrow(userCode);
        UserProfile profile = userProfileRepository.findById(user.getId())
                .orElse(UserProfile.builder().user(user).build());

        userProfileMapper.updateEntityFromDTO(dto, profile);
        return userProfileMapper.toDTO(userProfileRepository.save(profile));
    }

    @Override
    @Transactional
    public UserProfileDTO uploadAvatar(String userCode, MultipartFile file) {
        User user = getUserOrThrow(userCode);
        UserProfile profile = userProfileRepository.findById(user.getId())
                .orElse(UserProfile.builder().user(user).build());

        // 1. Upload to S3
        String folder = "avatars/" + userCode; // e.g., avatars/usr-123/
        String s3Url = fileStorageService.uploadFile(file, folder);

        // 2. Update Database
        profile.setAvatarUrl(s3Url);

        return userProfileMapper.toDTO(userProfileRepository.save(profile));
    }

    private User getUserOrThrow(String code) {
        return userRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("User", "code", code));
    }
}
