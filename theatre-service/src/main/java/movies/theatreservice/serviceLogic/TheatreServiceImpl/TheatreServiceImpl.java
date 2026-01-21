package movies.theatreservice.serviceLogic.TheatreServiceImpl;

import lombok.RequiredArgsConstructor;

import movies.theatreservice.client.UserClient;
import movies.theatreservice.dtos.CheckPermissionRequest;
import movies.theatreservice.dtos.ScreenDTO.ScreenDTO;
import movies.theatreservice.dtos.TheatreDTO.TheatreDTO;
import movies.theatreservice.entity.Theatre.Theatre;
import movies.theatreservice.entity.city.City;
import movies.theatreservice.exceptions.ResourceNotFoundException;
import movies.theatreservice.mappers.TheatreMapper.TheatreMapper;
import movies.theatreservice.repository.CityRepository.CityRepository;
import movies.theatreservice.repository.TheatreRepository.TheatreRepository;
import movies.theatreservice.security.SecurityUtils;
import movies.theatreservice.serviceImpl.TheatreService.TheatreService;
import movies.theatreservice.utils.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TheatreServiceImpl implements TheatreService {

    private final TheatreRepository theatreRepository;
    private final CityRepository cityRepository;
    private final TheatreMapper theatreMapper;
    private final UserClient userClient;


    @Override
    public void addScreen(String theatreId, ScreenDTO screenDTO) throws AccessDeniedException {
        // 1. Get Current User ID (from SecurityContext/Token)
        String currentUserId = SecurityUtils.getUserId();

        // 2. Call User Service synchronously
        CheckPermissionRequest req = CheckPermissionRequest.builder()
                .userId(currentUserId)
                .permissionCode("THEATRE_MANAGE") // The permission required
                .scopeRefCode(theatreId)          // The specific theatre
                .build();
        ResponseEntity<ApiResponse<Boolean>> response = userClient.checkPermission(req);

        if (response.getBody() == null || !Boolean.TRUE.equals(response.getBody().getData())) {
            throw new AccessDeniedException("You do not have permission to manage this theatre.");
        }
    }

    @Override
    public TheatreDTO createTheatre(TheatreDTO theatreDTO) {
        // 1. Validate City Exists
        City city = cityRepository.findByCode(theatreDTO.getCityCode())
                .orElseThrow(() -> new ResourceNotFoundException("City", "code", theatreDTO.getCityCode()));

        Theatre theatre = theatreMapper.toEntity(theatreDTO);
        theatre.setCity(city);

        Theatre savedTheatre = theatreRepository.save(theatre);
        return theatreMapper.toDTO(savedTheatre);
    }

    @Override
    public TheatreDTO getTheatreByCode(String code) {
        Theatre theatre = theatreRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre", "code", code));
        return theatreMapper.toDTO(theatre);
    }

    @Override
    public List<TheatreDTO> getAllTheatres(String cityCode) {
        List<Theatre> theatres;
        // If cityCode is present, filter by it. Otherwise, return all.
        if (cityCode != null && !cityCode.isEmpty()) {
            theatres = theatreRepository.findByCity_Code(cityCode);
        } else {
            theatres = theatreRepository.findAll();
        }

        return theatres.stream()
                .map(theatreMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional // Ensures data consistency when updating multiple fields
    public TheatreDTO updateTheatre(String code, TheatreDTO theatreDTO) {
        // 1. Find existing theatre
        Theatre theatre = theatreRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre", "code", code));

        // 2. Check if City is being changed
        // We compare the existing city code with the new one coming in the DTO
        if (!theatre.getCity().getCode().equals(theatreDTO.getCityCode())) {
            City newCity = cityRepository.findByCode(theatreDTO.getCityCode())
                    .orElseThrow(() -> new ResourceNotFoundException("City", "code", theatreDTO.getCityCode()));
            theatre.setCity(newCity);
        }

        // 3. Update standard fields
        theatre.setName(theatreDTO.getName());
        theatre.setAddress(theatreDTO.getAddress());
        theatre.setDescription(theatreDTO.getDescription());

        // 4. Save
        Theatre updatedTheatre = theatreRepository.save(theatre);
        return theatreMapper.toDTO(updatedTheatre);
    }

    @Override
    public void deleteTheatre(String code) {
        Theatre theatre = theatreRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre", "code", code));
        theatreRepository.delete(theatre);
    }
}