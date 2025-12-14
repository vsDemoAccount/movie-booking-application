package movies.theatreservice.serviceLogic.CityServiceImpl;

import lombok.RequiredArgsConstructor;
import movies.theatreservice.dtos.CityDTO.CityDTO;
import movies.theatreservice.entity.city.City;
import movies.theatreservice.exceptions.ResourceNotFoundException;
import movies.theatreservice.mappers.CityMapper.CityMapper;
import movies.theatreservice.repository.CityRepository.CityRepository;
import movies.theatreservice.serviceImpl.CityService.CityService;
import movies.theatreservice.utils.CodeGeneratorUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    @Override
    public CityDTO createCity(CityDTO cityDTO) {
        // 1. Check if city name exists
        if (cityRepository.existsByName(cityDTO.getName())) {
            throw new RuntimeException("City with name " + cityDTO.getName() + " already exists");
        }

        // 2. Map to Entity
        City city = cityMapper.toEntity(cityDTO);

        // 3. Save directly
        // The @PrePersist in your Entity handles the code generation.
        // If a collision happens (rare), the Database throws an exception,
        // which will be caught by your GlobalExceptionHandler.
        City savedCity = cityRepository.save(city);

        return cityMapper.toDTO(savedCity);
    }

    @Override
    public CityDTO getCityByCode(String code) {
        City city = cityRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("City", "code", code));

        return cityMapper.toDTO(city);
    }

    @Override
    public List<CityDTO> getAllCities() {
        return cityRepository.findAll().stream()
                .map(cityMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CityDTO updateCity(String code, CityDTO cityDTO) {
        // 1. Check existence first
        City city = cityRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("City", "code", code));

        // 2. Update logic
        city.setName(cityDTO.getName());
        city.setState(cityDTO.getState());

        // 3. Save
        City updatedCity = cityRepository.save(city);

        return cityMapper.toDTO(updatedCity);
    }

    @Override
    public void deleteCity(String code) {
        // 1. Check existence first
        City city = cityRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("City", "code", code));

        cityRepository.delete(city);
    }
}