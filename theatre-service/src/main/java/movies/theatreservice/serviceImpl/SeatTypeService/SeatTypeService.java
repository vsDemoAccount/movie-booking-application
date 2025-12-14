package movies.theatreservice.serviceImpl.SeatTypeService;

import movies.theatreservice.dtos.SeatTypeDTO.SeatTypeDTO;

import java.util.List;

public interface SeatTypeService {
    SeatTypeDTO createSeatType(SeatTypeDTO seatTypeDTO);
    SeatTypeDTO getSeatTypeByCode(String code);
    List<SeatTypeDTO> getAllSeatTypes();
    SeatTypeDTO updateSeatType(String code, SeatTypeDTO seatTypeDTO);
    void deleteSeatType(String code);
}