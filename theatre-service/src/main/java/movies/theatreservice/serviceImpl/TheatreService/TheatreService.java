package movies.theatreservice.serviceImpl.TheatreService;

import movies.theatreservice.dtos.TheatreDTO.TheatreDTO;

import java.util.List;

public interface TheatreService {
    TheatreDTO createTheatre(TheatreDTO theatreDTO);
    TheatreDTO getTheatreByCode(String code);
    List<TheatreDTO> getAllTheatres(String cityCode);

    // Added these two for full CRUD completion
    TheatreDTO updateTheatre(String code, TheatreDTO theatreDTO);
    void deleteTheatre(String code);
}
