package movies.moviesservice.Mappers.MovieMapper;


import movies.moviesservice.dtos.MovieDTO;
import movies.moviesservice.dtos.MovieCastDTO.MovieCastDto;
import movies.moviesservice.entity.Movie;
import movies.moviesservice.entity.Franchise.Franchise;
import movies.moviesservice.entity.Tag.Tag;
import movies.moviesservice.entity.genre.Genre;
import movies.moviesservice.entity.language.Language;
import movies.moviesservice.entity.movieCast.MovieCast;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MovieMapper {

    @Mapping(target = "languageCodes", source = "languages", qualifiedByName = "languagesToCodes")
    @Mapping(target = "genreCodes", source = "genres", qualifiedByName = "genresToCodes")
    @Mapping(target = "tagCodes", source = "tags", qualifiedByName = "tagsToCodes")
    @Mapping(target = "franchiseCode", source = "franchise", qualifiedByName = "franchiseToCode")
//    @Mapping(target = "cast", source = "cast", qualifiedByName = "castToDto")
    MovieDTO toDTO(Movie movie);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "languages", ignore = true)
    @Mapping(target = "genres", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "franchise", ignore = true)
//    @Mapping(target = "cast", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Movie toEntity(MovieDTO dto);

    @Named("languagesToCodes")
    default Set<String> languagesToCodes(Set<Language> languages) {
        if (languages == null) return null;
        return languages.stream().map(Language::getCode).collect(Collectors.toSet());
    }

    @Named("genresToCodes")
    default Set<String> genresToCodes(Set<Genre> genres) {
        if (genres == null) return null;
        return genres.stream().map(Genre::getCode).collect(Collectors.toSet());
    }

    @Named("tagsToCodes")
    default Set<String> tagsToCodes(Set<Tag> tags) {
        if (tags == null) return null;
        return tags.stream().map(Tag::getCode).collect(Collectors.toSet());
    }

    @Named("franchiseToCode")
    default String franchiseToCode(Franchise franchise) {
        return franchise == null ? null : franchise.getCode();
    }

    @Named("castToDto")
    default Set<MovieCastDto> castToDto(Set<MovieCast> cast) {
        if (cast == null) return null;
        return cast.stream().map(c -> MovieCastDto.builder()
                .personCode(c.getPerson() != null ? c.getPerson().getCode() : null)
                .personName(c.getPerson() != null ? c.getPerson().getName() : null)
                .role(c.getRole())
                .characterName(c.getCharacterName())
                .build()).collect(Collectors.toSet());
    }
}

