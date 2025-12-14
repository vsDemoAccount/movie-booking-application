package movies.theatreservice.mappers.SeatTypeMapper;


import movies.theatreservice.dtos.SeatTypeDTO.SeatTypeDTO;
import movies.theatreservice.entity.SeatType.SeatType;
import org.springframework.stereotype.Component;

@Component
public class SeatTypeMapper {

    public SeatTypeDTO toDTO(SeatType seatType) {
        return SeatTypeDTO.builder()
                .code(seatType.getCode())
                .name(seatType.getName())
                .createdAt(seatType.getCreatedAt())
                .updatedAt(seatType.getUpdatedAt())
                .build();
    }

    public SeatType toEntity(SeatTypeDTO dto) {
        return SeatType.builder()
                .name(dto.getName())
                .build();
    }
}