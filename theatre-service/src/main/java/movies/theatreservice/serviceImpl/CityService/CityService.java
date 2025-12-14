package movies.theatreservice.serviceImpl.CityService;

import movies.theatreservice.dtos.CityDTO.CityDTO;
import java.util.List;

public interface CityService {
    CityDTO createCity(CityDTO cityDTO);
    CityDTO getCityByCode(String code);
    List<CityDTO> getAllCities();
    CityDTO updateCity(String code, CityDTO cityDTO);
    void deleteCity(String code);
}