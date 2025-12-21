package movies.userservice.service.UserService;


import movies.userservice.dtos.UserDTO.UserDTO;

public interface UserService {

    UserDTO createUser(UserDTO userDTO);

    UserDTO getUserByCode(String code);

    UserDTO updateUser(String code, UserDTO userDTO);
}