package movies.userservice.serviceimpl.UserServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movies.userservice.dtos.UserDTO.UserDTO;
import movies.userservice.entity.User.User;
import movies.userservice.enums.UserStatus;

// import org.springframework.security.crypto.password.PasswordEncoder; // Uncomment when security is added
import movies.userservice.exception.DuplicateRecordException;
import movies.userservice.exception.ResourceNotFoundException;
import movies.userservice.mappers.UserMapper.UserMapper;
import movies.userservice.repository.UserRepository.UserRepository;
import movies.userservice.service.UserService.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        log.info("Creating new user with email: {}", userDTO.getEmail());

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new DuplicateRecordException("Email already in use");
        }
        if (userDTO.getPhone() != null && userRepository.existsByPhone(userDTO.getPhone())) {
            throw new DuplicateRecordException("Phone number already in use");
        }

        User user = userMapper.toEntity(userDTO);



        user.setStatus(UserStatus.ACTIVE);

        User savedUser = userRepository.save(user);
        log.info("User created successfully. Code: {}", savedUser.getCode());

        return userMapper.toDTO(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserByCode(String code) {
        return userRepository.findByCode(code)
                .map(userMapper::toDTO)
                .orElseThrow(() -> new  ResourceNotFoundException("User", "code", code));
    }

    @Override
    @Transactional
    public UserDTO updateUser(String code, UserDTO userDTO) {
        User user = userRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("User", "code", code));

        // Logic to prevent duplicate phone updates
        if (userDTO.getPhone() != null && !userDTO.getPhone().equals(user.getPhone())) {
            if(userRepository.existsByPhone(userDTO.getPhone())) {
                throw new DuplicateRecordException("Phone number already in use");
            }
        }

        userMapper.updateEntityFromDTO(userDTO, user);
        return userMapper.toDTO(userRepository.save(user));
    }
}