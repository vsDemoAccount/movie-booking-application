package movies.moviesservice.services.PersonService;


import movies.moviesservice.exception.ResourceNotFoundException;
import movies.moviesservice.exception.ValidationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import movies.moviesservice.repository.PersonRepository.PersonRepository;
import movies.moviesservice.dtos.PersonDTO.PersonDto;
import movies.moviesservice.Mappers.PersonMapper.PersonMapper;
import movies.moviesservice.entity.person.Person;

import java.util.Optional;

@Service
@Transactional
public class PersonService {
    private final PersonRepository personRepository;
    private final PersonMapper mapper;

    public PersonService(PersonRepository personRepository, PersonMapper mapper) {
        this.personRepository = personRepository;
        this.mapper = mapper;
    }

    public PersonDto create(PersonDto dto) {
        if (dto.code != null && personRepository.existsByCode(dto.code)) {
            throw new ValidationException("Person with code already exists");
        }
        Person entity = mapper.toEntity(dto);
        Person saved = personRepository.save(entity);
        return mapper.toDto(saved);
    }

    public PersonDto updateByCode(String code, PersonDto dto) {
        Person existing = personRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found for code: " + code));
        existing.setName(dto.name);
        existing.setBirthDate(dto.birthDate);
        existing.setBio(dto.bio);
        existing.setPhotoUrl(dto.photoUrl);
        Person saved = personRepository.save(existing);
        return mapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public PersonDto findByCode(String code) {
        return personRepository.findByCode(code).map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found for code: " + code));
    }
}

