package com.fscore.app.controller;

import com.fscore.app.dto.request.MediaRequest;
import com.fscore.app.dto.response.MediaResponse;
import com.fscore.app.dto.response.PageResponse;
import com.fscore.app.entity.Media;
import com.fscore.app.entity.*;
import com.fscore.app.service.MediaService;
import com.fscore.app.service.FilterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.net.URI;

@RestController
@RequestMapping("/api/medias")
public class MediaController {

    private final MediaService service;
    private final FilterService filterService;

    public MediaController(MediaService service, FilterService filterService) {
        this.service = service;
        this.filterService = filterService;
    }

    @GetMapping
    public ResponseEntity<PageResponse> findAll(Pageable pageable, @RequestParam Map<String, String> params) {
        Page<Media> page = filterService.find("mediaRepository", pageable, params);
        return ResponseEntity.ok(PageResponse.fromPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MediaResponse> findById(@PathVariable String id) {
        Media entity = service.findById(id)
            .orElseThrow(() -> new com.fscore.app.exception.ResourceNotFoundException("Media not found with id: " + id));
        return ResponseEntity.ok(mapToResponse(entity));
    }

    @PostMapping
    public ResponseEntity<MediaResponse> save(@Valid @RequestBody MediaRequest request) {
        Media entity = mapToEntity(request);
        Media saved = service.save(entity);
        return ResponseEntity.created(URI.create("/api/medias/" + saved.getId()))
            .body(mapToResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MediaResponse> update(@PathVariable String id, @Valid @RequestBody MediaRequest request) {
        Media entity = mapToEntity(request);
        Media updated = service.update(id, entity);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private MediaResponse mapToResponse(Media entity) {
        MediaResponse response = MediaResponse.builder()
            .description(entity.getDescription())
            .duration(entity.getDuration())
            .entityId(entity.getEntityId())
            .entityType(entity.getEntityType())
            .id(entity.getId())
            .language(entity.getLanguage())
            .mediaType(entity.getMediaType())
            .thumbnailUrl(entity.getThumbnailUrl())
            .title(entity.getTitle())
            .url(entity.getUrl())
            .build();
        return response;
    }

    private Media mapToEntity(MediaRequest request) {
        Media media = Media.builder()
            .description(request.getDescription())
            .duration(request.getDuration())
            .entityId(request.getEntityId())
            .entityType(request.getEntityType())
            .language(request.getLanguage())
            .mediaType(request.getMediaType())
            .thumbnailUrl(request.getThumbnailUrl())
            .title(request.getTitle())
            .url(request.getUrl())
            .build();
        return media;
    }
}
