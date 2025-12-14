package movies.theatreservice.serviceLogic.ShowServiceImpl;


import lombok.RequiredArgsConstructor;
import movies.theatreservice.dtos.ShowDTO.ShowDTO;
import movies.theatreservice.entity.CatalogMovie_kfk.CatalogMovie;
import movies.theatreservice.entity.Screen.Screen;
import movies.theatreservice.entity.SeatType.SeatType;
import movies.theatreservice.entity.Show.Show;
import movies.theatreservice.entity.ShowSeatPrice.ShowSeatPrice;
import movies.theatreservice.exceptions.DuplicateRecordException;
import movies.theatreservice.exceptions.ResourceNotFoundException;
import movies.theatreservice.mappers.ShowMapper.ShowMapper;
import movies.theatreservice.repository.CatalogMovieRepository.CatalogMovieRepository;
import movies.theatreservice.repository.ScreenRepository.ScreenRepository;
import movies.theatreservice.repository.SeatTypeRepository.SeatTypeRepository;
import movies.theatreservice.repository.ShowRepository.ShowRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShowServiceImpl {

    private final ShowRepository showRepository;
    private final ScreenRepository screenRepository;
    private final CatalogMovieRepository catalogMovieRepository;
    private final SeatTypeRepository seatTypeRepository;
    private final ShowMapper showMapper;

    @Transactional
    public ShowDTO createShow(ShowDTO showDTO) {
        // 1. Fetch Screen
        Screen screen = screenRepository.findByCode(showDTO.getScreenCode())
                .orElseThrow(() -> new ResourceNotFoundException("Screen", "code", showDTO.getScreenCode()));

        // 2. Fetch Movie from Local Catalog
        CatalogMovie movie = catalogMovieRepository.findById(showDTO.getMovieCode())
                .orElseThrow(() -> new ResourceNotFoundException("Movie Catalog", "code", showDTO.getMovieCode()));

        // 3. Calculate End Time
        Instant endTime = showDTO.getStartTime().plusSeconds(movie.getDurationMinutes() * 60L);

        // 4. Validate Overlaps
        List<Show> conflicts = showRepository.findOverlappingShows(screen.getId(), showDTO.getStartTime(), endTime);
        if (!conflicts.isEmpty()) {
            throw new DuplicateRecordException("Screen is busy during this time. Conflict with Show: " + conflicts.get(0).getCode());
        }

        // 5. Build Show Entity
        Show show = Show.builder()
                .screen(screen)
                .movie(movie)
                .startTime(showDTO.getStartTime())
                .build();

        // 6. Add Prices
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

        // 7. Save & Return
        Show savedShow = showRepository.save(show);
        return showMapper.toDTO(savedShow);
    }
}