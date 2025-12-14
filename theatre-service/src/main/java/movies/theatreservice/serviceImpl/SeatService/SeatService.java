package movies.theatreservice.serviceImpl.SeatService;

import movies.theatreservice.dtos.SeatDTO.SeatDTO;

import java.util.List;

public interface SeatService {
    SeatDTO createSeat(SeatDTO seatDTO);
    SeatDTO getSeatByCode(String code);
    List<SeatDTO> getAllSeats(String screenCode); // Must filter by screen!
    void deleteSeat(String code);
}