package movies.moviesservice.controller.franchiseController;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import movies.moviesservice.dtos.franchiseDTO.franchiseDTO;
import movies.moviesservice.services.franchise_Service.franchiseServiceImpl;
import movies.moviesservice.utils.ApiResponse;
import movies.moviesservice.validations.onCreate.Create;
import movies.moviesservice.validations.onUpdate.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/franchises")
@RequiredArgsConstructor
public class franchiseController {

    private final franchiseServiceImpl franchiseService;

    @PostMapping
    public ResponseEntity<ApiResponse<franchiseDTO>> create(@Validated(Create.class) @RequestBody franchiseDTO dto) {
        franchiseDTO saved = franchiseService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Franchise created", saved));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<franchiseDTO>>> getAll() {
        List<franchiseDTO> list = franchiseService.getAll();
        return ResponseEntity.ok(ApiResponse.ok("Franchises retrieved", list));
    }

    @GetMapping("/{code}")
    public ResponseEntity<ApiResponse<franchiseDTO>> getById(@PathVariable String code) {
        franchiseDTO dto = franchiseService.getByCode(code);
        return ResponseEntity.ok(ApiResponse.ok("Franchise retrieved", dto));
    }

    @PutMapping("/{code}")
    public ResponseEntity<ApiResponse<franchiseDTO>> update(@PathVariable String code,
                                                            @Validated(Update.class) @RequestBody franchiseDTO dto) {
        franchiseDTO updated = franchiseService.updateByCode(code, dto);
        return ResponseEntity.ok(ApiResponse.ok("Franchise updated", updated));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String code ){
        franchiseService.deleteByCode(code);
        return ResponseEntity.ok(ApiResponse.ok("Franchise deleted", null));
    }
}

