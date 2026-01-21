package movies.moviesservice.controller.movies_controller;

import jakarta.persistence.EntityNotFoundException;
import movies.moviesservice.dtos.MovieDTO;
import movies.moviesservice.services.movies_service.MoviesService;
import movies.moviesservice.utils.ApiResponse;
import movies.moviesservice.validations.onCreate.Create;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/api/movies/movies")
public class MoviesController {

    private final MoviesService moviesService;

    @Autowired
    public MoviesController(MoviesService moviesService) {
        this.moviesService = moviesService;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<MovieDTO>> createMovie(
            @Validated(Create.class) @RequestBody MovieDTO movieDto) {
        try {
            MovieDTO savedMovie = moviesService.createMovie(movieDto);
            ApiResponse<MovieDTO> response = ApiResponse.ok("Movie created successfully", savedMovie);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException | EntityNotFoundException e) {
            ApiResponse<MovieDTO> response = ApiResponse.error(e.getMessage(), null);
            return ResponseEntity.badRequest().body(response);
        } catch (AccessDeniedException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{code}")
    public ResponseEntity<ApiResponse<MovieDTO>> getMovieByCode(@PathVariable String code) {
        try {
            MovieDTO movie = moviesService.getMovieByCode(code);
            ApiResponse<MovieDTO> response = ApiResponse.ok("Movie retrieved successfully", movie);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            ApiResponse<MovieDTO> response = ApiResponse.error(e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PatchMapping("/{code}")
    public ResponseEntity<ApiResponse<MovieDTO>> updateMovie(
            @PathVariable String code,
            @RequestBody MovieDTO movieDto) {
        try {
            MovieDTO updatedMovie = moviesService.updateMovie(code, movieDto);
            ApiResponse<MovieDTO> response = ApiResponse.ok("Movie updated successfully", updatedMovie);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            ApiResponse<MovieDTO> response = ApiResponse.error(e.getMessage(), null);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<ApiResponse<Void>> deleteMovie(@PathVariable String code) {
        try {
            moviesService.deleteMovie(code);
            ApiResponse<Void> response = ApiResponse.ok("Movie deleted successfully", null);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            ApiResponse<Void> response = ApiResponse.error(e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<MovieDTO>>> getAllMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MovieDTO> moviesPage = moviesService.getAllMovies(pageable);
        ApiResponse<Page<MovieDTO>> response = ApiResponse.ok("Movies retrieved successfully", moviesPage);
        return ResponseEntity.ok(response);
    }
}
