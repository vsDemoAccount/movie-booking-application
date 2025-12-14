package movies.theatreservice.controller.SeatTypeController;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import movies.theatreservice.dtos.SeatTypeDTO.SeatTypeDTO;
import movies.theatreservice.serviceImpl.SeatTypeService.SeatTypeService;
import movies.theatreservice.utils.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seat-types")
@RequiredArgsConstructor
public class SeatTypeController {

    private final SeatTypeService seatTypeService;

    @PostMapping
    public ResponseEntity<ApiResponse<SeatTypeDTO>> createSeatType(@Valid @RequestBody SeatTypeDTO seatTypeDTO) {
        SeatTypeDTO createdSeatType = seatTypeService.createSeatType(seatTypeDTO);
        return new ResponseEntity<>(ApiResponse.ok("Seat Type created successfully", createdSeatType), HttpStatus.CREATED);
    }

    @GetMapping("/{code}")
    public ResponseEntity<ApiResponse<SeatTypeDTO>> getSeatType(@PathVariable String code) {
        SeatTypeDTO seatType = seatTypeService.getSeatTypeByCode(code);
        return ResponseEntity.ok(ApiResponse.ok("Seat Type fetched successfully", seatType));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SeatTypeDTO>>> getAllSeatTypes() {
        List<SeatTypeDTO> seatTypes = seatTypeService.getAllSeatTypes();
        return ResponseEntity.ok(ApiResponse.ok("Seat Types fetched successfully", seatTypes));
    }

    @PutMapping("/{code}")
    public ResponseEntity<ApiResponse<SeatTypeDTO>> updateSeatType(
            @PathVariable String code,
            @Valid @RequestBody SeatTypeDTO seatTypeDTO) {
        SeatTypeDTO updatedSeatType = seatTypeService.updateSeatType(code, seatTypeDTO);
        return ResponseEntity.ok(ApiResponse.ok("Seat Type updated successfully", updatedSeatType));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<ApiResponse<Void>> deleteSeatType(@PathVariable String code) {
        seatTypeService.deleteSeatType(code);
        return ResponseEntity.ok(ApiResponse.ok("Seat Type deleted successfully", null));
    }
}