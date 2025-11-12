package movies.moviesservice.Mappers.TagMapper;


import movies.moviesservice.dtos.TagDto.TagDto;
import movies.moviesservice.entity.Tag.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TagMapper {
    TagDto toDto(Tag tag);

    @Mapping(target = "movies", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Tag toEntity(TagDto tagDto);

    List<TagDto> toDTOList(List<Tag> tags);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "movies", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDTO(TagDto tagDto, @MappingTarget Tag tag);
}