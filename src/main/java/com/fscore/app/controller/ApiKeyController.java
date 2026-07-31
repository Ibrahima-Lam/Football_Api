package com.fscore.app.controller;

import com.fscore.app.dto.request.ApiKeyRequest;
import com.fscore.app.dto.response.ApiKeyResponse;
import com.fscore.app.dto.response.CreatedApiKeyResponse;
import com.fscore.app.dto.response.PageResponse;
import com.fscore.app.entity.ApiKey;
import com.fscore.app.entity.ApiUser;
import com.fscore.app.service.ApiKeyService;
import com.fscore.app.service.FilterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/api-keys")
public class ApiKeyController {

    private final ApiKeyService service;
    private final FilterService filterService;

    public ApiKeyController(ApiKeyService service, FilterService filterService) {
        this.service = service;
        this.filterService = filterService;
    }

    @GetMapping
    public ResponseEntity<PageResponse> findAll(Pageable pageable, @RequestParam Map<String, String> params) {
        Page<ApiKey> page = filterService.find("apiKeyRepository", pageable, params);
        Page<ApiKeyResponse> mapped = page.map(this::mapToResponse);
        return ResponseEntity.ok(PageResponse.fromPage(mapped));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiKeyResponse> findById(@PathVariable String id) {
        ApiKey entity = service.findById(id)
            .orElseThrow(() -> new com.fscore.app.exception.ResourceNotFoundException("ApiKey not found with id: " + id));
        return ResponseEntity.ok(mapToResponse(entity));
    }

    @PostMapping
    public ResponseEntity<CreatedApiKeyResponse> create(@Valid @RequestBody ApiKeyRequest request) {
        if (request.getUserId() == null) {
            request.setUserId(currentUserId());
        }
        ApiKeyService.GeneratedKey generated = service.generate(request);
        CreatedApiKeyResponse response = CreatedApiKeyResponse.builder()
            .id(generated.apiKey().getId())
            .userId(generated.apiKey().getUser() != null ? generated.apiKey().getUser().getId() : null)
            .name(generated.apiKey().getName())
            .key(generated.secret())
            .expiresAt(generated.apiKey().getExpiresAt())
            .build();
        return ResponseEntity.created(URI.create("/api/api-keys/" + generated.apiKey().getId()))
            .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiKeyResponse> update(@PathVariable String id, @Valid @RequestBody ApiKeyRequest request) {
        ApiKey existing = service.findById(id)
            .orElseThrow(() -> new com.fscore.app.exception.ResourceNotFoundException("ApiKey not found with id: " + id));
        existing.setName(request.getName());
        existing.setActive(request.getActive() != null ? request.getActive() : existing.getActive());
        if (request.getExpiresAt() != null) {
            existing.setExpiresAt(request.getExpiresAt());
        }
        ApiKey saved = service.save(existing);
        return ResponseEntity.ok(mapToResponse(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private ApiKeyResponse mapToResponse(ApiKey entity) {
        return ApiKeyResponse.builder()
            .id(entity.getId())
            .userId(entity.getUser() != null ? entity.getUser().getId() : null)
            .name(entity.getName())
            .maskedKey(service.mask(entity.getKeyHash()))
            .active(entity.getActive())
            .expiresAt(entity.getExpiresAt())
            .lastUsedAt(entity.getLastUsedAt())
            .createdAt(entity.getCreatedAt())
            .build();
    }

    private String currentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof ApiUser user) {
            return user.getId();
        }
        return null;
    }
}
