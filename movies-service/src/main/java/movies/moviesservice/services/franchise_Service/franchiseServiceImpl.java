package movies.moviesservice.services.franchise_Service;


import lombok.RequiredArgsConstructor;
import movies.moviesservice.Mappers.franchiseMapper.franchiseMapper;
import movies.moviesservice.dtos.franchiseDTO.franchiseDTO;
import movies.moviesservice.entity.Franchise.Franchise;
import movies.moviesservice.exception.ResourceNotFoundException;
import movies.moviesservice.exception.ValidationException;
import movies.moviesservice.repository.franchiseRepository.franchiseRepository;
import movies.moviesservice.service_Interface.franchiseService.franchiseService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class franchiseServiceImpl implements franchiseService {

    private final franchiseRepository repository;

    @Override
    public franchiseDTO create(franchiseDTO dto) {
        Franchise entity = franchiseMapper.fromDto(dto);
        // ensure id not forced on create
        entity.setId(null);
        // optionally generate or set code here if needed before save
        Franchise saved = repository.save(entity);
        return franchiseMapper.toDto(saved);
    }

    @Override
    public List<franchiseDTO> getAll() {
        return repository.findAll().stream()
                .map(franchiseMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public franchiseDTO getByCode(String code) {
        return repository.findByCode(code)
                .map(franchiseMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Franchise not found"));
    }

    @Override
    public franchiseDTO updateByCode(String code, franchiseDTO dto) {
        Franchise entity = repository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException( "Franchise not found"));
        franchiseMapper.updateEntityFromDto(dto, entity);
        entity.setCode(code);
        Franchise saved = repository.save(entity);
        return franchiseMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void deleteByCode(String code) {
        if (!repository.existsByCode(code)) {
            throw new ValidationException("Franchise not found");
        }
        repository.deleteByCode(code);
    }
}
