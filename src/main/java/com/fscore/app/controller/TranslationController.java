package com.fscore.app.controller;

import com.fscore.app.dto.request.TranslationRequest;
import com.fscore.app.dto.response.TranslationResponse;
import com.fscore.app.dto.response.PageResponse;
import com.fscore.app.entity.Translation;
import com.fscore.app.entity.*;
import com.fscore.app.service.TranslationService;
import com.fscore.app.service.FilterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.net.URI;

@RestController
@RequestMapping("/api/translations")
public class TranslationController {

    private final TranslationService service;
    private final FilterService filterService;

    public TranslationController(TranslationService service, FilterService filterService) {
        this.service = service;
        this.filterService = filterService;
    }

    @GetMapping
    public ResponseEntity<PageResponse> findAll(Pageable pageable, @RequestParam Map<String, String> params) {
        Page<Translation> page = filterService.find("translationRepository", pageable, params);
        return ResponseEntity.ok(PageResponse.fromPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TranslationResponse> findById(@PathVariable String id) {
        Translation entity = service.findById(id)
            .orElseThrow(() -> new com.fscore.app.exception.ResourceNotFoundException("Translation not found with id: " + id));
        return ResponseEntity.ok(mapToResponse(entity));
    }

    @PostMapping
    public ResponseEntity<TranslationResponse> save(@Valid @RequestBody TranslationRequest request) {
        Translation entity = mapToEntity(request);
        Translation saved = service.save(entity);
        return ResponseEntity.created(URI.create("/api/translations/" + saved.getId()))
            .body(mapToResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TranslationResponse> update(@PathVariable String id, @Valid @RequestBody TranslationRequest request) {
        Translation entity = mapToEntity(request);
        Translation updated = service.update(id, entity);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private TranslationResponse mapToResponse(Translation entity) {
        TranslationResponse response = TranslationResponse.builder()
            .entityId(entity.getEntityId())
            .entityType(entity.getEntityType())
            .fieldName(entity.getFieldName())
            .id(entity.getId())
            .language(entity.getLanguage())
            .translatedValue(entity.getTranslatedValue())
            .build();
        return response;
    }

    private Translation mapToEntity(TranslationRequest request) {
        Translation translation = Translation.builder()
            .entityId(request.getEntityId())
            .entityType(request.getEntityType())
            .fieldName(request.getFieldName())
            .language(request.getLanguage())
            .translatedValue(request.getTranslatedValue())
            .build();
        return translation;
    }
}
