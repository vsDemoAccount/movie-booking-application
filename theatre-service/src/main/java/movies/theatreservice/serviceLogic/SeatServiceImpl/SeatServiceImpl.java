package movies.theatreservice.serviceLogic.SeatServiceImpl;

import lombok.RequiredArgsConstructor;
import movies.theatreservice.dtos.SeatDTO.SeatDTO;
import movies.theatreservice.dtos.SeatLayoutDTO.SeatLayoutDTO;
import movies.theatreservice.entity.Screen.Screen;
import movies.theatreservice.entity.Seat.Seat;
import movies.theatreservice.entity.SeatType.SeatType;
import movies.theatreservice.exceptions.DuplicateRecordException;
import movies.theatreservice.exceptions.ResourceNotFoundException;
import movies.theatreservice.mappers.SeatMapper.SeatMapper;
import movies.theatreservice.repository.ScreenRepository.ScreenRepository;
import movies.theatreservice.repository.SeatRepository.SeatRepository;
import movies.theatreservice.repository.SeatTypeRepository.SeatTypeRepository;
import movies.theatreservice.repository.ShowSeatStatusRepository.ShowSeatStatusRepository; // NEW IMPORT
import movies.theatreservice.serviceImpl.SeatService.SeatService;
import movies.theatreservice.utils.CodeGeneratorUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;
    private final ScreenRepository screenRepository;
    private final SeatTypeRepository seatTypeRepository;
    private final ShowSeatStatusRepository showSeatStatusRepository; // Required for validation
    private final SeatMapper seatMapper;

    @Override
    public SeatDTO createSeat(SeatDTO seatDTO) {
        if (seatRepository.existsByScreen_CodeAndRowNameAndSeatNumber(
                seatDTO.getScreenCode(), seatDTO.getRowName(), seatDTO.getSeatNumber())) {
            throw new DuplicateRecordException("Seat " + seatDTO.getRowName() + "-" + seatDTO.getSeatNumber() +
                    " already exists in this screen.");
        }

        Screen screen = screenRepository.findByCode(seatDTO.getScreenCode())
                .orElseThrow(() -> new ResourceNotFoundException("Screen", "code", seatDTO.getScreenCode()));

        SeatType seatType = seatTypeRepository.findByCode(seatDTO.getSeatTypeCode())
                .orElseThrow(() -> new ResourceNotFoundException("SeatType", "code", seatDTO.getSeatTypeCode()));

        Seat seat = seatMapper.toEntity(seatDTO);
        seat.setScreen(screen);
        seat.setSeatType(seatType);

        Seat savedSeat = seatRepository.save(seat);
        return seatMapper.toDTO(savedSeat);
    }

    @Override
    @Transactional
    public List<SeatDTO> createSeatLayout(SeatLayoutDTO layoutDTO) {
        Screen screen = screenRepository.findByCode(layoutDTO.getScreenCode())
                .orElseThrow(() -> new ResourceNotFoundException("Screen", "code", layoutDTO.getScreenCode()));

        List<Seat> newSeats = new ArrayList<>();
        char currentRow = 'A';

        for (int r = 0; r < layoutDTO.getTotalRows(); r++) {
            String rowName = String.valueOf(currentRow);

            String typeCode = "STANDARD";
            if (layoutDTO.getRowTypeMapping() != null) {
                typeCode = layoutDTO.getRowTypeMapping().getOrDefault(rowName, "STANDARD");
            }

            final String finalTypeCode = typeCode;

            SeatType seatType = seatTypeRepository.findByCode(finalTypeCode)
                    .orElseThrow(() -> new ResourceNotFoundException("SeatType", "code", finalTypeCode));

            for (int c = 1; c <= layoutDTO.getColumnsPerRow(); c++) {

                if (layoutDTO.getAisleColumns() != null && layoutDTO.getAisleColumns().contains(c)) {
                    continue;
                }

                if(!seatRepository.existsByScreen_CodeAndRowNameAndSeatNumber(screen.getCode(), rowName, c)){
                    Seat seat = Seat.builder()
                            .screen(screen)
                            .rowName(rowName)
                            .seatNumber(c)
                            .seatType(seatType)
                            .code(CodeGeneratorUtil.generate("Sea", 12))
                            .gridRow(r)
                            .gridCol(c)
                            .isActive(true)
                            .build();
                    newSeats.add(seat);
                }
            }
            currentRow++;
        }

        List<Seat> savedSeats = seatRepository.saveAll(newSeats);
        return savedSeats.stream().map(seatMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public SeatDTO getSeatByCode(String code) {
        Seat seat = seatRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Seat", "code", code));
        return seatMapper.toDTO(seat);
    }

    @Override
    public List<SeatDTO> getAllSeats(String screenCode) {
        if (screenCode == null || screenCode.isEmpty()) {
            throw new RuntimeException("Screen Code is required");
        }
        return seatRepository.findByScreen_Code(screenCode).stream()
                .map(seatMapper::toDTO)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public void deleteSeat(String code) {
        // 1. Find the seat
        Seat seat = seatRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Seat", "code", code));

      boolean hasFutureBookings = showSeatStatusRepository.existsBySeat_IdAndShow_StartTimeAfter(
                seat.getId(), Instant.now()
        );

        if (hasFutureBookings) {
            throw new DuplicateRecordException("Cannot deactivate seat. It is booked for a future show.");
        }

       seat.setActive(false);

         seatRepository.save(seat);
    }
}