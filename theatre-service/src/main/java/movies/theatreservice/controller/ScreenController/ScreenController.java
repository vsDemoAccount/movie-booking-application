package movies.theatreservice.controller.ScreenController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import movies.theatreservice.dtos.ScreenDTO.ScreenDTO;
import movies.theatreservice.serviceImpl.ScreenService.ScreenService;
import movies.theatreservice.utils.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/screens")
@RequiredArgsConstructor
public class ScreenController {

    private final ScreenService screenService;

    @PostMapping
    public ResponseEntity<ApiResponse<ScreenDTO>> createScreen(@Valid @RequestBody ScreenDTO screenDTO) {
        ScreenDTO createdScreen = screenService.createScreen(screenDTO);
        return new ResponseEntity<>(ApiResponse.ok("Screen created successfully", createdScreen), HttpStatus.CREATED);
    }

    @GetMapping("/{code}")
    public ResponseEntity<ApiResponse<ScreenDTO>> getScreen(@PathVariable String code) {
        ScreenDTO screen = screenService.getScreenByCode(code);
        return ResponseEntity.ok(ApiResponse.ok("Screen fetched successfully", screen));
    }

    // Supports: /api/screens?theatreCode=Thea-123
    @GetMapping
    public ResponseEntity<ApiResponse<List<ScreenDTO>>> getAllScreens() {
        List<ScreenDTO> screens = screenService.getAllScreens(null);
        return ResponseEntity.ok(ApiResponse.ok("Screens fetched successfully", screens));
    }

    @PutMapping("/{code}")
    public ResponseEntity<ApiResponse<ScreenDTO>> updateScreen(
            @PathVariable String code,
            @Valid @RequestBody ScreenDTO screenDTO) {
        ScreenDTO updatedScreen = screenService.updateScreen(code, screenDTO);
        return ResponseEntity.ok(ApiResponse.ok("Screen updated successfully", updatedScreen));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<ApiResponse<Void>> deleteScreen(@PathVariable String code) {
        screenService.deleteScreen(code);
        return ResponseEntity.ok(ApiResponse.ok("Screen deleted successfully", null));
    }
}