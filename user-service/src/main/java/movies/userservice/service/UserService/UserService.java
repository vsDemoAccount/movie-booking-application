package movies.userservice.service.UserService;


import movies.userservice.dtos.UserDTO.UserDTO;

public interface UserService {

    UserDTO getCurrentUser();
    UserDTO updateCurrentUser(UserDTO dto);
}