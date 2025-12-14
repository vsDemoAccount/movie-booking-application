package movies.moviesservice.services.GenreServiceImpl;



import lombok.RequiredArgsConstructor;
import movies.moviesservice.dtos.GenreDto.GenreDto;
import movies.moviesservice.entity.genre.Genre;
import movies.moviesservice.Mappers.GenreMapper.GenreMapper;
import movies.moviesservice.repository.GenreRepository.GenreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class GenreServiceImpl {

    private final GenreRepository repository;
    private final GenreMapper mapper;

    public GenreDto create(GenreDto dto) {
        Genre genre = mapper.toEntity(dto);
        Genre saved = repository.save(genre);
        return mapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<GenreDto> getAllCodes() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}

