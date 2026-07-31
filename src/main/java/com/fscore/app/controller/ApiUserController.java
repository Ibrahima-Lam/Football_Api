package com.fscore.app.controller;

import com.fscore.app.dto.request.ApiUserRequest;
import com.fscore.app.dto.response.ApiUserResponse;
import com.fscore.app.dto.response.PageResponse;
import com.fscore.app.entity.ApiUser;
import com.fscore.app.entity.*;
import com.fscore.app.service.ApiUserService;
import com.fscore.app.service.FilterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.net.URI;

@RestController
@RequestMapping("/api/api-users")
public class ApiUserController {

    private final ApiUserService service;
    private final FilterService filterService;

    public ApiUserController(ApiUserService service, FilterService filterService) {
        this.service = service;
        this.filterService = filterService;
    }

    @GetMapping
    public ResponseEntity<PageResponse> findAll(Pageable pageable, @RequestParam Map<String, String> params) {
        Page<ApiUser> page = filterService.find("apiUserRepository", pageable, params);
        return ResponseEntity.ok(PageResponse.fromPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiUserResponse> findById(@PathVariable String id) {
        ApiUser entity = service.findById(id)
            .orElseThrow(() -> new com.fscore.app.exception.ResourceNotFoundException("ApiUser not found with id: " + id));
        return ResponseEntity.ok(mapToResponse(entity));
    }

    @PostMapping
    public ResponseEntity<ApiUserResponse> save(@Valid @RequestBody ApiUserRequest request) {
        ApiUser entity = mapToEntity(request);
        ApiUser saved = service.save(entity);
        return ResponseEntity.created(URI.create("/api/api-users/" + saved.getId()))
            .body(mapToResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiUserResponse> update(@PathVariable String id, @Valid @RequestBody ApiUserRequest request) {
        ApiUser existing = service.findById(id)
            .orElseThrow(() -> new com.fscore.app.exception.ResourceNotFoundException("ApiUser not found with id: " + id));
        ApiUser entity = mapToEntity(request);
        entity.setPasswordHash(existing.getPasswordHash());
        ApiUser updated = service.update(id, entity);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private ApiUserResponse mapToResponse(ApiUser entity) {
        ApiUserResponse response = ApiUserResponse.builder()
            .active(entity.getActive())
            .email(entity.getEmail())
            .id(entity.getId())
            .name(entity.getName())
            .plan(entity.getPlan())
            .build();
        return response;
    }

    private ApiUser mapToEntity(ApiUserRequest request) {
        ApiUser apiUser = ApiUser.builder()
            .active(request.getActive())
            .email(request.getEmail())
            .name(request.getName())
            .plan(request.getPlan())
            .build();
        return apiUser;
    }
}
