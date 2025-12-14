package movies.theatreservice.mappers.CityMapper;


import movies.theatreservice.dtos.CityDTO.CityDTO;
import movies.theatreservice.entity.city.City;
import org.springframework.stereotype.Component;

@Component
public class CityMapper {

    // Entity -> DTO
    public CityDTO toDTO(City city) {
        return CityDTO.builder()
                .code(city.getCode())
                .name(city.getName())
                .state(city.getState())
                .createdAt(city.getCreatedAt())
                .updatedAt(city.getUpdatedAt())
                .build();
    }

    // DTO -> Entity (Ignores ID, Code, Timestamps safely)
    public City toEntity(CityDTO dto) {
        return City.builder()
                .name(dto.getName())
                .state(dto.getState())
                .build();
    }
}