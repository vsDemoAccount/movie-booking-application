package movies.theatreservice.controller.TheatreController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import movies.theatreservice.dtos.TheatreDTO.TheatreDTO;
import movies.theatreservice.serviceImpl.TheatreService.TheatreService;
import movies.theatreservice.utils.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/theatres")
@RequiredArgsConstructor
public class TheatreController {

    private final TheatreService theatreService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TheatreDTO>>> getAllTheatres() {
        List<TheatreDTO> theatres = theatreService.getAllTheatres(null);
        return ResponseEntity.ok(ApiResponse.ok("Theatres fetched successfully", theatres));
    }


    @PostMapping
    public ResponseEntity<ApiResponse<TheatreDTO>> createTheatre(@Valid @RequestBody TheatreDTO theatreDTO) {
        TheatreDTO createdTheatre = theatreService.createTheatre(theatreDTO);
        return new ResponseEntity<>(ApiResponse.ok("Theatre created successfully", createdTheatre), HttpStatus.CREATED);
    }

    @GetMapping("/{code}")
    public ResponseEntity<ApiResponse<TheatreDTO>> getTheatre(@PathVariable String code) {
        TheatreDTO theatre = theatreService.getTheatreByCode(code);
        return ResponseEntity.ok(ApiResponse.ok("Theatre fetched successfully", theatre));
    }

    @PutMapping("/{code}")
    public ResponseEntity<ApiResponse<TheatreDTO>> updateTheatre(
            @PathVariable String code,
            @Valid @RequestBody TheatreDTO theatreDTO) {
        TheatreDTO updatedTheatre = theatreService.updateTheatre(code, theatreDTO);
        return ResponseEntity.ok(ApiResponse.ok("Theatre updated successfully", updatedTheatre));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<ApiResponse<Void>> deleteTheatre(@PathVariable String code) {
        theatreService.deleteTheatre(code);
        return ResponseEntity.ok(ApiResponse.ok("Theatre deleted successfully", null));
    }
}
