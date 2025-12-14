package movies.theatreservice.controller.CityController;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import movies.theatreservice.dtos.CityDTO.CityDTO;

import movies.theatreservice.serviceImpl.CityService.CityService;
import movies.theatreservice.utils.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;

    @PostMapping
    public ResponseEntity<ApiResponse<CityDTO>> createCity(@Valid @RequestBody CityDTO cityDTO) {
        CityDTO createdCity = cityService.createCity(cityDTO);
        return new ResponseEntity<>(ApiResponse.ok("City created successfully", createdCity), HttpStatus.CREATED);
    }

    @GetMapping("/{code}")
    public ResponseEntity<ApiResponse<CityDTO>> getCity(@PathVariable String code) {
        CityDTO city = cityService.getCityByCode(code);
        return ResponseEntity.ok(ApiResponse.ok("City fetched successfully", city));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CityDTO>>> getAllCities() {
        List<CityDTO> cities = cityService.getAllCities();
        return ResponseEntity.ok(ApiResponse.ok("Cities fetched successfully", cities));
    }

    @PutMapping("/{code}")
    public ResponseEntity<ApiResponse<CityDTO>> updateCity(
            @PathVariable String code,
            @Valid @RequestBody CityDTO cityDTO) {
        CityDTO updatedCity = cityService.updateCity(code, cityDTO);
        return ResponseEntity.ok(ApiResponse.ok("City updated successfully", updatedCity));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<ApiResponse<Void>> deleteCity(@PathVariable String code) {
        cityService.deleteCity(code);
        return ResponseEntity.ok(ApiResponse.ok("City deleted successfully", null));
    }
}