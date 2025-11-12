package movies.moviesservice.controller.GenreController;



import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import movies.moviesservice.dtos.GenreDto.GenreDto;
import movies.moviesservice.services.GenreServiceImpl.GenreServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreServiceImpl service;

    @PostMapping
    public ResponseEntity<GenreDto> create(@Valid @RequestBody GenreDto dto) {
        GenreDto response = service.create(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<GenreDto>> getAllCodes() {
        return ResponseEntity.ok(service.getAllCodes());
    }
}
