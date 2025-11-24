package movies.moviesservice.controller.MovieCastController;

import movies.moviesservice.validations.onCreate.Create;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import movies.moviesservice.dtos.MovieCastDTO.MovieCastDto;
import movies.moviesservice.services.MovieCastService.MovieCastService;

@RestController
@RequestMapping("/api/movies/movie-casts")
public class MovieCastController {
    private final MovieCastService service;

    public MovieCastController(MovieCastService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<MovieCastDto> create(@Validated(Create.class) @RequestBody MovieCastDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/{code}")
    public ResponseEntity<MovieCastDto> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(service.findByCode(code));
    }

    @PutMapping("/{code}")
    public ResponseEntity<MovieCastDto> update(@Validated(Create.class) @PathVariable String code, @RequestBody MovieCastDto dto) {
        return ResponseEntity.ok(service.updateByCode(code, dto));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Void> delete(@PathVariable String code) {
        service.deleteByCode(code);
        return ResponseEntity.noContent().build();
    }
}
