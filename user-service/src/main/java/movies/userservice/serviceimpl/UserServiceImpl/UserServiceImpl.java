package movies.userservice.serviceimpl.UserServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movies.userservice.dtos.UserDTO.UserDTO;
import movies.userservice.entity.User.User;
import movies.userservice.mappers.UserMapper.UserMapper;
import movies.userservice.repository.UserRepository.UserRepository;
import movies.userservice.service.UserService.UserService;
import movies.userservice.serviceimpl.UserProvisioningService.UserProvisioningService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserProvisioningService provisioningService;
    private final UserMapper userMapper;
    private final UserRepository userRepository; // Added this to save the user

    @Override
    @Transactional
    public UserDTO getCurrentUser() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        User user = provisioningService.getOrCreate(jwt);
        return userMapper.toDTO(user);
    }

    @Override
    @Transactional
    public UserDTO updateCurrentUser(UserDTO dto) {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        // 1. Get the user (Syncs from Keycloak if needed)
        User user = provisioningService.getOrCreate(jwt);

        dto.setEmail(null);
        dto.setCode(null);

        userMapper.updateEntityFromDTO(dto, user);

        return userMapper.toDTO(userRepository.save(user));
    }
}