package movies.theatreservice.serviceLogic.SeatServiceImpl;


import lombok.RequiredArgsConstructor;

import movies.theatreservice.dtos.SeatDTO.SeatDTO;
import movies.theatreservice.entity.Screen.Screen;
import movies.theatreservice.entity.Seat.Seat;
import movies.theatreservice.entity.SeatType.SeatType;
import movies.theatreservice.exceptions.DuplicateRecordException;
import movies.theatreservice.exceptions.ResourceNotFoundException;
import movies.theatreservice.mappers.SeatMapper.SeatMapper;
import movies.theatreservice.repository.ScreenRepository.ScreenRepository;
import movies.theatreservice.repository.SeatRepository.SeatRepository;
import movies.theatreservice.repository.SeatTypeRepository.SeatTypeRepository;
import movies.theatreservice.serviceImpl.SeatService.SeatService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;
    private final ScreenRepository screenRepository;
    private final SeatTypeRepository seatTypeRepository;
    private final SeatMapper seatMapper;

    @Override
    public SeatDTO createSeat(SeatDTO seatDTO) {
        // 1. Check if Seat already exists in this position (e.g., A-1 in Screen 1)
        if (seatRepository.existsByScreen_CodeAndRowNameAndSeatNumber(
                seatDTO.getScreenCode(), seatDTO.getRowName(), seatDTO.getSeatNumber())) {
            throw new DuplicateRecordException("Seat " + seatDTO.getRowName() + "-" + seatDTO.getSeatNumber() +
                    " already exists in this screen.");
        }

        // 2. Validate Screen
        Screen screen = screenRepository.findByCode(seatDTO.getScreenCode())
                .orElseThrow(() -> new ResourceNotFoundException("Screen", "code", seatDTO.getScreenCode()));

        // 3. Validate SeatType
        SeatType seatType = seatTypeRepository.findByCode(seatDTO.getSeatTypeCode())
                .orElseThrow(() -> new ResourceNotFoundException("SeatType", "code", seatDTO.getSeatTypeCode()));

        // 4. Create Entity
        Seat seat = seatMapper.toEntity(seatDTO);
        seat.setScreen(screen);
        seat.setSeatType(seatType);

        Seat savedSeat = seatRepository.save(seat);
        return seatMapper.toDTO(savedSeat);
    }

    @Override
    public SeatDTO getSeatByCode(String code) {
        Seat seat = seatRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Seat", "code", code));
        return seatMapper.toDTO(seat);
    }

    @Override
    public List<SeatDTO> getAllSeats(String screenCode) {
        // We force filtering by Screen Code because getting ALL seats in the DB is dangerous/useless
        if (screenCode == null || screenCode.isEmpty()) {
            throw new RuntimeException("Screen Code is required to fetch seats");
        }

        return seatRepository.findByScreen_Code(screenCode).stream()
                .map(seatMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteSeat(String code) {
        Seat seat = seatRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Seat", "code", code));
        seatRepository.delete(seat);
    }
}