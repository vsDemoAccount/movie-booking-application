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
@RequiredArgsConstructor
public class GenreController {

    private final GenreServiceImpl service;

    @PostMapping
        GenreDto response = service.create(dto);
    }

    @GetMapping
    }
}
