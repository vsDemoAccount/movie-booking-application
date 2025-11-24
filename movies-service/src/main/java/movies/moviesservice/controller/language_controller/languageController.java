package movies.moviesservice.controller.language_controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import movies.moviesservice.dtos.languageDTO.languageDTO;
import movies.moviesservice.services.language_service.languageServiceImpl;
import movies.moviesservice.utils.ApiResponse;
import movies.moviesservice.validations.onCreate.Create;
import movies.moviesservice.validations.onUpdate.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.*;

        import java.util.List;

@RestController
@RequestMapping("/api/movies/admin/languages")
@RequiredArgsConstructor
public class languageController {

    private final languageServiceImpl languageService;

    @PostMapping
    public ResponseEntity<ApiResponse<languageDTO>> create(@Validated(Create.class) @RequestBody languageDTO dto) {
        languageDTO saved = languageService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Language created", saved));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<languageDTO>>> getAll() {
        List<languageDTO> list = languageService.getAll();
        return ResponseEntity.ok(ApiResponse.ok("Languages retrieved", list));
    }

    @GetMapping("/{code}")
    public ResponseEntity<ApiResponse<languageDTO>> getByCode(@PathVariable String code) {
        languageDTO dto = languageService.getByCode(code);
        return ResponseEntity.ok(ApiResponse.ok("Language retrieved", dto));
    }

    @PutMapping("/{code}")
    public ResponseEntity<ApiResponse<languageDTO>> update(@PathVariable String code,
                                                           @Validated(Create.class) @RequestBody languageDTO dto) {
        languageDTO updated = languageService.updateByCode(code, dto);
        return ResponseEntity.ok(ApiResponse.ok("Language updated", updated));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String code) {
        languageService.deleteByCode(code);
        return ResponseEntity.ok(ApiResponse.ok("Language deleted", null));
    }
}


