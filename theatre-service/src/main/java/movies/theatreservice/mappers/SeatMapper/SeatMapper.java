package movies.theatreservice.mappers.SeatMapper;


import movies.theatreservice.dtos.SeatDTO.SeatDTO;
import movies.theatreservice.entity.Seat.Seat;
import org.springframework.stereotype.Component;

@Component
public class SeatMapper {

    public SeatDTO toDTO(Seat seat) {
        return SeatDTO.builder()
                .code(seat.getCode())
                .screenCode(seat.getScreen().getCode())
                .seatTypeCode(seat.getSeatType().getCode())
                .rowName(seat.getRowName())
                .seatNumber(seat.getSeatNumber())
                .gridRow(seat.getGridRow())
                .gridCol(seat.getGridCol())
                .isActive(seat.isActive())
                .createdAt(seat.getCreatedAt())
                .build();
    }

    public Seat toEntity(SeatDTO dto) {
        return Seat.builder()
                .rowName(dto.getRowName())
                .seatNumber(dto.getSeatNumber())
                .gridRow(dto.getGridRow())
                .gridCol(dto.getGridCol())
                .isActive(dto.isActive())
                // Relationships set in Service
                .build();
    }
}