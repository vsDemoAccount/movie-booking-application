package movies.moviesservice.Mappers.franchiseMapper;

import movies.moviesservice.dtos.franchiseDTO.franchiseDTO;
import movies.moviesservice.entity.Franchise.Franchise;

public class franchiseMapper {

    public static franchiseDTO toDto(Franchise entity) {
        if (entity == null) return null;
        return franchiseDTO.builder()
                .code(entity.getCode())
                .name(entity.getName())
                .description(entity.getDescription())
                .build();
    }

    public static Franchise fromDto(franchiseDTO dto) {
        if (dto == null) return null;
        return Franchise.builder()
                .code(dto.getCode())
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
    }

    public static void updateEntityFromDto(franchiseDTO dto, Franchise entity) {
        if (dto.getName() != null) entity.setName(dto.getName());
        if (dto.getDescription() != null) entity.setDescription(dto.getDescription());
        // do not change id here
    }
}
