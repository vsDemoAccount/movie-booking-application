package movies.moviesservice.service_Interface.GenreService;

import movies.moviesservice.dtos.GenreDto.GenreDto;
import java.util.List;

public interface GenreService {
    GenreDto create(GenreDto dto);
    List<GenreDto> getAllCodes();
}