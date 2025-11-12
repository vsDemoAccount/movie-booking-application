package movies.moviesservice.Mappers.MovieMapper;


import movies.moviesservice.dtos.*;
import movies.moviesservice.dtos.MovieCastDTO.MovieCastDto;
import movies.moviesservice.entity.*;

import movies.moviesservice.entity.movieCast.MovieCast;
import movies.moviesservice.entity.genre.Genre;
import movies.moviesservice.entity.language.Language;
import movies.moviesservice.entity.CastRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MovieMapper {

    @Mapping(target = "languages", source = "languages", qualifiedByName = "languagesToStringSet")
    @Mapping(target = "genres", source = "genres", qualifiedByName = "genresToStringSet")
    @Mapping(target = "franchise", source = "franchise", qualifiedByName = "franchiseToName")
    @Mapping(target = "cast", source = "cast", qualifiedByName = "castToDto")
    MovieDTO toDTO(Movie movie);

    @Mapping(target = "languages", ignore = true)
    @Mapping(target = "genres", ignore = true)
    @Mapping(target = "franchise", ignore = true)
    @Mapping(target = "cast", ignore = true)
    Movie toEntity(MovieDTO dto);

    @Named("languagesToStringSet")
    default Set<String> languagesToStringSet(Set<Language> languages) {
        if (languages == null) return null;
        return languages.stream().map(Language::getName).collect(Collectors.toSet());
    }

    @Named("genresToStringSet")
    default Set<String> genresToStringSet(Set<Genre> genres) {
        if (genres == null) return null;
        return genres.stream().map(Genre::getName).collect(Collectors.toSet());
    }

    @Named("franchiseToName")
    default String franchiseToName(movies.moviesservice.entity.Franchise.Franchise f) {
        return f == null ? null : f.getName();
    }

    @Named("castToDto")
    default Set<MovieCastDto> castToDto(Set<MovieCast> cast) {
        if (cast == null) return null;
        return cast.stream().map(c -> MovieCastDto.builder()
                .personCode(c.getPerson() != null ? c.getPerson().getCode() : null)
                .personName(c.getPerson() != null ? c.getPerson().getName() : null)
                .role(convertToCastRole(c.getRole()))
                .characterName(c.getCharacterName())
                .build()).collect(Collectors.toSet());
    }

    // accept enum directly (identity) to match MovieCast.getRole() which returns CastRole
    default CastRole convertToCastRole(CastRole role) {
        return role;

    }
}

