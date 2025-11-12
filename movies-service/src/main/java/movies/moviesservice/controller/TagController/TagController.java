package movies.moviesservice.controller.TagController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import movies.moviesservice.dtos.TagDto.TagDto;
import movies.moviesservice.services.TagService.TagService;
import movies.moviesservice.utils.ApiResponse;
import movies.moviesservice.validations.onCreate.Create;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
@Tag(name = "Tag Management", description = "APIs for managing movie tags")
public class TagController {

    private final TagService tagService;

    @PostMapping
    @Operation(summary = "Create a new tag")
    public ResponseEntity<ApiResponse<TagDto>> createTag(
            @Validated(Create.class) @RequestBody TagDto tagDto) {
        TagDto createdTag = tagService.createTag(tagDto);
        ApiResponse<TagDto> response = ApiResponse.<TagDto>builder()
                .message("Tag created successfully")
                .data(createdTag)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{code}")
    @Operation(summary = "Get tag by code")
    public ResponseEntity<ApiResponse<TagDto>> getTagByCode(@PathVariable String code) {
        TagDto tag = tagService.getTagByCode(code);
        ApiResponse<TagDto> response = ApiResponse.<TagDto>builder()
                .message("Tag retrieved successfully")
                .data(tag)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all tags")
    public ResponseEntity<ApiResponse<List<TagDto>>> getAllTags() {
        List<TagDto> tags = tagService.getAllTags();
        ApiResponse<List<TagDto>> response = ApiResponse.<List<TagDto>>builder()
                .message("Tags retrieved successfully")
                .data(tags)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{code}")
    @Operation(summary = "Update tag")
    public ResponseEntity<ApiResponse<TagDto>> updateTag(
            @PathVariable String code,
            @Validated(Create.class) @RequestBody TagDto tagDto) {
        TagDto updatedTag = tagService.updateTag(code, tagDto);
        ApiResponse<TagDto> response = ApiResponse.<TagDto>builder()
                .message("Tag updated successfully")
                .data(updatedTag)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{code}")
    @Operation(summary = "Delete tag")
    public ResponseEntity<ApiResponse<Void>> deleteTag(@PathVariable String code) {
        tagService.deleteTag(code);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .message("Tag deleted successfully")
                .build();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}