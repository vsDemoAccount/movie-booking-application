package movies.moviesservice.controller.GenreController;



import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import movies.moviesservice.dtos.GenreDto.GenreDto;
import movies.moviesservice.services.GenreServiceImpl.GenreServiceImpl;
import movies.moviesservice.utils.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GenreController {

    private final GenreServiceImpl service;

    @PostMapping
    public ResponseEntity<ApiResponse<GenreDto>> create(@Valid @RequestBody GenreDto dto) {
        GenreDto response = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Genre created", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<GenreDto>>> getAllCodes() {
        List<GenreDto> list = service.getAllCodes();
        return ResponseEntity.ok(ApiResponse.ok("Genres retrieved", list));
    }
}
