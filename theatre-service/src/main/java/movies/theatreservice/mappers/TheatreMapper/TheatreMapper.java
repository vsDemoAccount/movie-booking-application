package movies.theatreservice.mappers.TheatreMapper;

import movies.theatreservice.dtos.TheatreDTO.TheatreDTO;
import movies.theatreservice.entity.Theatre.Theatre;
import org.springframework.stereotype.Component;

@Component
public class TheatreMapper {

    public TheatreDTO toDTO(Theatre theatre) {
        return TheatreDTO.builder()
                .code(theatre.getCode())
                .name(theatre.getName())
                .address(theatre.getAddress())
                .description(theatre.getDescription())
                .cityCode(theatre.getCity().getCode()) // Extract City Code
                .createdAt(theatre.getCreatedAt())
                .updatedAt(theatre.getUpdatedAt())
                .build();
    }

    public Theatre toEntity(TheatreDTO dto) {
        return Theatre.builder()
                .name(dto.getName())
                .address(dto.getAddress())
                .description(dto.getDescription())
                // Note: We don't set City here; the Service does that
                .build();
    }
}