package movies.theatreservice.serviceImpl.ScreenService;


import movies.theatreservice.dtos.ScreenDTO.ScreenDTO;

import java.util.List;

public interface ScreenService {
    ScreenDTO createScreen(ScreenDTO screenDTO);
    ScreenDTO getScreenByCode(String code);
    List<ScreenDTO> getAllScreens(String theatreCode);
    ScreenDTO updateScreen(String code, ScreenDTO screenDTO);
    void deleteScreen(String code);
}