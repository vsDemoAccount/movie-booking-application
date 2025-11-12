package movies.moviesservice.Mappers.languageMapper;

import movies.moviesservice.dtos.languageDTO.languageDTO;
import movies.moviesservice.entity.language.Language;

import movies.moviesservice.entity.language.Language;

public class languageMapper {
    public static languageDTO toDto(Language entity) {
        if (entity == null) return null;
        return languageDTO.builder()
                .name(entity.getName())
                .code(entity.getCode())
                .build();
    }

    public static Language fromDto(languageDTO dto) {
        if (dto == null) return null;
        return Language.builder()
                .name(dto.getName())
                .code(dto.getCode()) // usually null on create; preserved if provided for other flows
                .build();
    }

    public static void updateEntityFromDto(languageDTO dto, Language entity) {
        if (dto.getName() != null) entity.setName(dto.getName());
        if (dto.getCode() != null) entity.setCode(dto.getCode()); // avoid changing code unless intended
    }
}


