package movies.theatreservice.controller.ShowController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import movies.theatreservice.dtos.LockSeatsRequestDTO.LockSeatsRequestDTO;
import movies.theatreservice.dtos.ShowDTO.ShowDTO;
import movies.theatreservice.serviceLogic.ShowServiceImpl.ShowServiceImpl;
import movies.theatreservice.utils.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shows")
@RequiredArgsConstructor
public class ShowController {

    private final ShowServiceImpl showService;

    @PostMapping
    public ResponseEntity<ApiResponse<ShowDTO>> createShow(@Valid @RequestBody ShowDTO showDTO) {
        ShowDTO createdShow = showService.createShow(showDTO);
        return new ResponseEntity<>(ApiResponse.ok("Show created successfully", createdShow), HttpStatus.CREATED);
    }

    @GetMapping("/{showCode}/seats")
    public ResponseEntity<ApiResponse<List<String>>> getOccupiedSeats(@PathVariable String showCode) {
        List<String> bookedSeats = showService.getBookedSeatCodes(showCode);
        return ResponseEntity.ok(ApiResponse.ok("Fetched occupied seats", bookedSeats));
    }

    @PostMapping("/lock")
    public ResponseEntity<ApiResponse<Void>> lockSeats(@RequestBody LockSeatsRequestDTO request) {
        showService.lockSeats(request);
        return ResponseEntity.ok(ApiResponse.ok("Seats locked successfully", null));
    }

    @DeleteMapping("/{showCode}")
    public ResponseEntity<ApiResponse<Void>> cancelShow(@PathVariable String showCode) {
        showService.cancelShow(showCode);
        return ResponseEntity.ok(ApiResponse.ok("Show cancelled successfully", null));
    }
}