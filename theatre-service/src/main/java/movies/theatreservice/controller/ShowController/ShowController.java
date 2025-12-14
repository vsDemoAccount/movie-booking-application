package movies.theatreservice.controller.ShowController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import movies.theatreservice.dtos.ShowDTO.ShowDTO;
import movies.theatreservice.serviceLogic.ShowServiceImpl.ShowServiceImpl;
import movies.theatreservice.utils.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}