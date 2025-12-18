package movies.theatreservice.serviceLogic.ShowServiceImpl;


import lombok.RequiredArgsConstructor;
import movies.theatreservice.dtos.LockSeatsRequestDTO.LockSeatsRequestDTO;
import movies.theatreservice.dtos.ShowDTO.ShowDTO;
import movies.theatreservice.entity.CatalogMovie_kfk.CatalogMovie;
import movies.theatreservice.entity.Screen.Screen;
import movies.theatreservice.entity.Seat.Seat;
import movies.theatreservice.entity.SeatType.SeatType;
import movies.theatreservice.entity.Show.Show;
import movies.theatreservice.entity.ShowSeatPrice.ShowSeatPrice;
import movies.theatreservice.entity.ShowSeatStatus.ShowSeatStatus;
import movies.theatreservice.enums.BookingStatus;
import movies.theatreservice.enums.ShowStatus;
import movies.theatreservice.exceptions.DuplicateRecordException;
import movies.theatreservice.exceptions.ResourceNotFoundException;
import movies.theatreservice.mappers.ShowMapper.ShowMapper;
import movies.theatreservice.repository.CatalogMovieRepository.CatalogMovieRepository;
import movies.theatreservice.repository.ScreenRepository.ScreenRepository;
import movies.theatreservice.repository.SeatRepository.SeatRepository;
import movies.theatreservice.repository.SeatTypeRepository.SeatTypeRepository;
import movies.theatreservice.repository.ShowRepository.ShowRepository;
import movies.theatreservice.repository.ShowSeatStatusRepository.ShowSeatStatusRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShowServiceImpl {

    private final ShowRepository showRepository;
    private final ScreenRepository screenRepository;
    private final CatalogMovieRepository catalogMovieRepository;
    private final SeatTypeRepository seatTypeRepository;
    private final ShowMapper showMapper;
    private final ShowSeatStatusRepository showSeatStatusRepository;
    private final SeatRepository seatRepository;

    public List<String> getBookedSeatCodes(String showCode) {
        return showSeatStatusRepository.findByShow_Code(showCode).stream()
                .map(status -> status.getSeat().getCode())
                .collect(Collectors.toList());
    }

    @Transactional
    public void lockSeats(LockSeatsRequestDTO request) {
        // A. Validate Show by CODE
        Show show = showRepository.findByCode(request.getShowCode())
                .orElseThrow(() -> new ResourceNotFoundException("Show", "code", request.getShowCode()));

        // B. Resolve Seat Codes to Entity Objects
        List<Seat> seats = new ArrayList<>();
        for (String code : request.getSeatCodes()) {
            Seat seat = seatRepository.findByCode(code)
                    .orElseThrow(() -> new ResourceNotFoundException("Seat", "code", code));
            seats.add(seat);
        }

        List<Long> seatIds = seats.stream().map(Seat::getId).collect(Collectors.toList());

        // C. Check Concurrency (Using Show Code)
        long occupiedCount = showSeatStatusRepository.countOccupiedSeats(show.getCode(), seatIds);
        if (occupiedCount > 0) {
            throw new DuplicateRecordException("One or more selected seats are already booked or locked.");
        }

        // D. Create Locks
        List<ShowSeatStatus> locks = seats.stream().map(seat -> ShowSeatStatus.builder()
                .show(show)
                .seat(seat)
                .status(BookingStatus.LOCKED)
                .bookingRefId(request.getBookingRefId())
                .lockedAt(Instant.now())
                .build()
        ).collect(Collectors.toList());

        try {
            showSeatStatusRepository.saveAll(locks);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateRecordException("Race Condition: Someone just booked these seats ahead of you. Please select different seats.");
        }
    }

    @Transactional
    public ShowDTO createShow(ShowDTO showDTO) {

        Screen screen = screenRepository.findByCode(showDTO.getScreenCode())
                .orElseThrow(() -> new ResourceNotFoundException("Screen", "code", showDTO.getScreenCode()));

        CatalogMovie movie = catalogMovieRepository.findById(showDTO.getMovieCode())
                .orElseThrow(() -> new ResourceNotFoundException("Movie Catalog", "code", showDTO.getMovieCode()));

        Instant endTime = showDTO.getStartTime().plusSeconds(movie.getDurationMinutes() * 60L);

        boolean hasOverlap = showRepository.existsOverlappingShow(screen.getId(), showDTO.getStartTime(), endTime);
        if (hasOverlap) {
            throw new DuplicateRecordException("Screen is busy during this time.");
        }

        Show show = Show.builder()
                .screen(screen)
                .movie(movie)
                .startTime(showDTO.getStartTime())
                .endTime(endTime)
                .build();

        for (ShowDTO.ShowPriceDTO priceConfig : showDTO.getPrices()) {
            SeatType seatType = seatTypeRepository.findByCode(priceConfig.getSeatTypeCode())
                    .orElseThrow(() -> new ResourceNotFoundException("SeatType", "code", priceConfig.getSeatTypeCode()));

            ShowSeatPrice price = ShowSeatPrice.builder()
                    .show(show)
                    .seatType(seatType)
                    .price(priceConfig.getPrice())
                    .createdAt(Instant.now())
                    .build();

            show.getPrices().add(price);
        }

        Show savedShow = showRepository.save(show);
        return showMapper.toDTO(savedShow);
    }

    @Transactional
    public void cancelShow(String showCode) {
        Show show = showRepository.findByCode(showCode)
                .orElseThrow(() -> new ResourceNotFoundException("Show", "code", showCode));

        if (show.getStatus() == ShowStatus.CANCELLED) {
            throw new RuntimeException("Show is already cancelled.");
        }

        if (show.getStatus() == ShowStatus.COMPLETED) {
            throw new RuntimeException("Cannot cancel a show that has already completed.");
        }

        // 1. Mark as Cancelled
        show.setStatus(ShowStatus.CANCELLED);
        showRepository.save(show);
    }
}