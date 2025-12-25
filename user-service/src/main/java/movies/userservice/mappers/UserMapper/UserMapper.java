package movies.userservice.mappers.UserMapper;


import movies.userservice.dtos.UserDTO.UserDTO;
import movies.userservice.entity.User.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    // Entity -> DTO
    UserDTO toDTO(User user);

    // DTO -> Entity (Create)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "keycloakId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    User toEntity(UserDTO dto);

    // DTO -> Entity (Update)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "email", ignore = true) // Usually email changes require special flow
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(UserDTO dto, @MappingTarget User user);
}