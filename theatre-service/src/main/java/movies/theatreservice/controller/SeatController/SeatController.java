package movies.theatreservice.controller.SeatController;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import movies.theatreservice.dtos.SeatDTO.SeatDTO;
import movies.theatreservice.dtos.SeatLayoutDTO.SeatLayoutDTO; // New DTO
import movies.theatreservice.serviceImpl.SeatService.SeatService;
import movies.theatreservice.utils.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;

    // Create Single Seat
    @PostMapping
    public ResponseEntity<ApiResponse<SeatDTO>> createSeat(@Valid @RequestBody SeatDTO seatDTO) {
        SeatDTO createdSeat = seatService.createSeat(seatDTO);
        return new ResponseEntity<>(ApiResponse.ok("Seat created successfully", createdSeat), HttpStatus.CREATED);
    }

    @PostMapping("/layout")
    public ResponseEntity<ApiResponse<List<SeatDTO>>> createSeatLayout(@RequestBody SeatLayoutDTO layoutDTO) {
        List<SeatDTO> seats = seatService.createSeatLayout(layoutDTO);
        return new ResponseEntity<>(ApiResponse.ok("Seat layout generated successfully", seats), HttpStatus.CREATED);
    }

    @GetMapping("/{code}")
    public ResponseEntity<ApiResponse<SeatDTO>> getSeat(@PathVariable String code) {
        SeatDTO seat = seatService.getSeatByCode(code);
        return ResponseEntity.ok(ApiResponse.ok("Seat fetched successfully", seat));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SeatDTO>>> getAllSeatsForScreen(
            @RequestParam(required = true) String screenCode) {
        List<SeatDTO> seats = seatService.getAllSeats(screenCode);
        return ResponseEntity.ok(ApiResponse.ok("Seats fetched successfully", seats));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<ApiResponse<Void>> deleteSeat(@PathVariable String code) {
        seatService.deleteSeat(code);
        return ResponseEntity.ok(ApiResponse.ok("Seat deleted successfully", null));
    }
}