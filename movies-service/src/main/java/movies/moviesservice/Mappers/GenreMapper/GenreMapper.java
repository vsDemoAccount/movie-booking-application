package movies.moviesservice.Mappers.GenreMapper;

import movies.moviesservice.entity.genre.Genre;
import org.springframework.stereotype.Component;

import movies.moviesservice.dtos.GenreDto.GenreDto;
import movies.moviesservice.entity.genre.Genre;
import org.springframework.stereotype.Component;

@Component
public class GenreMapper {

    public Genre toEntity(GenreDto dto) {
        if (dto == null) return null;
        return Genre.builder()
                .name(dto.getName())
                .build();
    }

    // Return DTO that contains only the code for frontend
    public GenreDto toDto(Genre entity) {
        if (entity == null) return null;
        return GenreDto.builder()
                .name(entity.getName())
                .code(entity.getCode())
                .build();
    }
}
