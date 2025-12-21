package movies.userservice.mappers.UserProfileMapper;

import movies.userservice.dtos.UserProfileDTO.UserProfileDTO;
import movies.userservice.entity.UserProfile.UserProfile;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserProfileMapper {

    UserProfileDTO toDTO(UserProfile entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(UserProfileDTO dto, @MappingTarget UserProfile entity);
}
