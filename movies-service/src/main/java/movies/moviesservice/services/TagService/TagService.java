package movies.moviesservice.services.TagService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movies.moviesservice.Mappers.TagMapper.TagMapper;
import movies.moviesservice.dtos.TagDto.TagDto;
import movies.moviesservice.entity.Tag.Tag;
import movies.moviesservice.exception.ValidationException;
import movies.moviesservice.repository.TagRepository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import movies.moviesservice.exception.ResourceNotFoundException;


import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    public TagDto createTag(TagDto tagDto) {
        log.info("Creating new tag with name: {}", tagDto.getName());

        if (tagRepository.existsByName(tagDto.getName())) {
            throw new ValidationException("Tag with name '" + tagDto.getName() + "' already exists");
        }

        Tag tag = tagMapper.toEntity(tagDto);
        Tag savedTag = tagRepository.save(tag);

        log.info("Tag created successfully with code: {}", savedTag.getCode());
        return tagMapper.toDto(savedTag);
    }

    @Transactional(readOnly = true)
    public TagDto getTagByCode(String code) {
        log.info("Fetching tag with code: {}", code);
        Tag tag = tagRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found with code: " + code));
        return tagMapper.toDto(tag);
    }
    @Transactional(readOnly = true)
    public List<TagDto> getAllTags() {
        log.info("Fetching all tags");
        List<Tag> tags = tagRepository.findAll();
        return tagMapper.toDTOList(tags);
    }

    public TagDto updateTag(String code, TagDto tagDto) {
        log.info("Updating tag with code: {}", code);

        Tag existingTag = tagRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found with code: " + code));

        if (tagDto.getName() != null && !tagDto.getName().equals(existingTag.getName())) {
            if (tagRepository.existsByName(tagDto.getName())) {
                throw new ValidationException("Tag with name '" + tagDto.getName() + "' already exists");
            }
        }

        tagMapper.updateEntityFromDTO(tagDto, existingTag);
        Tag updatedTag = tagRepository.save(existingTag);

        log.info("Tag updated successfully with code: {}", updatedTag.getCode());
        return tagMapper.toDto(updatedTag);
    }

    public void deleteTag(String code) {
        log.info("Deleting tag with code: {}", code);

        Tag tag = tagRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found with code: " + code));

        tagRepository.delete(tag);
        log.info("Tag deleted successfully with code: {}", code);
    }
}