package movies.theatreservice.mappers.ScreenMapper;

import movies.theatreservice.dtos.ScreenDTO.ScreenDTO;
import movies.theatreservice.entity.Screen.Screen;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class ScreenMapper {

    public ScreenDTO toDTO(Screen screen) {
        return ScreenDTO.builder()
                .code(screen.getCode())
                .name(screen.getName())
                .theatreCode(screen.getTheatre().getCode())
                .features(screen.getFeatures())
                .createdAt(screen.getCreatedAt())
                .updatedAt(screen.getUpdatedAt())
                .build();
    }

    public Screen toEntity(ScreenDTO dto) {
        return Screen.builder()
                .name(dto.getName())
                .features(dto.getFeatures() != null ? dto.getFeatures() : new HashSet<>())
                // Theatre is set in the Service
                .build();
    }
}