package movies.moviesservice.controller.PersonController;

import movies.moviesservice.validations.onCreate.Create;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import movies.moviesservice.dtos.PersonDTO.PersonDto;
import movies.moviesservice.services.PersonService.PersonService;

@RestController
@RequestMapping("/api/movies/persons")
public class PersonController {
    private final PersonService service;

    public PersonController(PersonService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PersonDto> create(@Validated(Create.class) @RequestBody PersonDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/{code}")
    public ResponseEntity<PersonDto> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(service.findByCode(code));
    }

    @PutMapping("/{code}")
    public ResponseEntity<PersonDto> update(@Validated(Create.class) @PathVariable String code, @RequestBody PersonDto dto) {
        return ResponseEntity.ok(service.updateByCode(code, dto));
    }
}

