package movies.theatreservice.mappers.ShowMapper;

import movies.theatreservice.dtos.ShowDTO.ShowDTO;
import movies.theatreservice.entity.Show.Show;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ShowMapper {

    public ShowDTO toDTO(Show show) {
        // Map the prices list
        List<ShowDTO.ShowPriceDTO> priceDTOs = show.getPrices().stream()
                .map(p -> ShowDTO.ShowPriceDTO.builder()
                        .seatTypeCode(p.getSeatType().getCode())
                        .seatTypeName(p.getSeatType().getName())
                        .price(p.getPrice())
                        .build())
                .collect(Collectors.toList());

        return ShowDTO.builder()
                .code(show.getCode())
                .screenCode(show.getScreen().getCode())
                .movieCode(show.getMovie().getCode())
                .movieTitle(show.getMovie().getTitle()) // Fetched from Catalog
                .posterUrl(show.getMovie().getPosterUrl())
                .durationMinutes(show.getMovie().getDurationMinutes())
                .startTime(show.getStartTime())
                .endTime(show.getEndTime()) // Calculated in Entity
                .prices(priceDTOs)
                .createdAt(show.getCreatedAt())
                .build();
    }

    // Note: We don't typically have a simple 'toEntity' for Shows because
    // creating a Show requires complex lookups (Screen, CatalogMovie) which happen in the Service.
}