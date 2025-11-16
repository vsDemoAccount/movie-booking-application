package movies.moviesservice.services.language_service;

import lombok.RequiredArgsConstructor;
import movies.moviesservice.Mappers.languageMapper.languageMapper;
import movies.moviesservice.dtos.languageDTO.languageDTO;
import movies.moviesservice.entity.language.Language;
import movies.moviesservice.exception.ResourceNotFoundException;
import movies.moviesservice.repository.language.LanguageRepository;
import movies.moviesservice.service_Interface.lang_interface.languageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import movies.moviesservice.exception.ValidationException;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class languageServiceImpl implements languageService{

    private final LanguageRepository languageRepository;

    @Override
    @Transactional
    public languageDTO create(languageDTO dto) {
        if (dto == null || dto.getName() == null || dto.getName().isBlank()) {
            throw new ValidationException("Language name must be provided");
        }

        String nameTrim = dto.getName().trim();
        if (languageRepository.existsByNameIgnoreCase(nameTrim)) {
            throw new ValidationException("Language with the same name already exists: " + dto.getName());
        }

        // Let entity @PrePersist generate code; do not set code here
        Language entity = languageMapper.fromDto(dto);
        Language saved = languageRepository.save(entity);
        return languageMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<languageDTO> getAll() {
        return languageRepository.findAll().stream()
                .map(languageMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public languageDTO getByCode(String code) {
        Language lang = languageRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Language not found with code: " + code));
        return languageMapper.toDto(lang);
    }

    @Override
    @Transactional
    public languageDTO updateByCode(String code, languageDTO dto) {
        Language existing = languageRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Language not found with code: " + code));

        if (dto.getName() != null && !dto.getName().trim().equalsIgnoreCase(existing.getName())) {
            if (languageRepository.existsByNameIgnoreCase(dto.getName().trim())) {
                throw new ResourceNotFoundException("Another language already uses name: " + dto.getName());
            }
        }

        languageMapper.updateEntityFromDto(dto, existing);
        Language saved = languageRepository.save(existing);
        return languageMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void deleteByCode(String code) {
        Language existing = languageRepository.findByCode(code)
                .orElseThrow(() -> new ValidationException("Language not found with code: " + code));
        languageRepository.delete(existing);
    }

}
