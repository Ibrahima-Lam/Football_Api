package com.fscore.app.controller;

import com.fscore.app.dto.request.ConfederationRequest;
import com.fscore.app.dto.response.ConfederationResponse;
import com.fscore.app.dto.response.PageResponse;
import com.fscore.app.entity.Confederation;
import com.fscore.app.entity.*;
import com.fscore.app.service.ConfederationService;
import com.fscore.app.service.FilterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.net.URI;

@RestController
@RequestMapping("/api/confederations")
public class ConfederationController {

    private final ConfederationService service;
    private final FilterService filterService;

    public ConfederationController(ConfederationService service, FilterService filterService) {
        this.service = service;
        this.filterService = filterService;
    }

    @GetMapping
    public ResponseEntity<PageResponse> findAll(Pageable pageable, @RequestParam Map<String, String> params) {
        Page<Confederation> page = filterService.find("confederationRepository", pageable, params);
        return ResponseEntity.ok(PageResponse.fromPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConfederationResponse> findById(@PathVariable String id) {
        Confederation entity = service.findById(id)
            .orElseThrow(() -> new com.fscore.app.exception.ResourceNotFoundException("Confederation not found with id: " + id));
        return ResponseEntity.ok(mapToResponse(entity));
    }

    @PostMapping
    public ResponseEntity<ConfederationResponse> save(@Valid @RequestBody ConfederationRequest request) {
        Confederation entity = mapToEntity(request);
        Confederation saved = service.save(entity);
        return ResponseEntity.created(URI.create("/api/confederations/" + saved.getId()))
            .body(mapToResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConfederationResponse> update(@PathVariable String id, @Valid @RequestBody ConfederationRequest request) {
        Confederation entity = mapToEntity(request);
        Confederation updated = service.update(id, entity);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private ConfederationResponse mapToResponse(Confederation entity) {
        ConfederationResponse response = ConfederationResponse.builder()
            .acronym(entity.getAcronym())
            .continentId(entity.getContinent() != null ? entity.getContinent().getId() : null)
            .founded(entity.getFounded())
            .headquarters(entity.getHeadquarters())
            .id(entity.getId())
            .logo(entity.getLogo())
            .name(entity.getName())
            .website(entity.getWebsite())
            .build();
        return response;
    }

    private Confederation mapToEntity(ConfederationRequest request) {
        Confederation confederation = Confederation.builder()
            .continent(request.getContinentId() != null ? Continent.builder().id(request.getContinentId()).build() : null)
            .acronym(request.getAcronym())
            .founded(request.getFounded())
            .headquarters(request.getHeadquarters())
            .logo(request.getLogo())
            .name(request.getName())
            .website(request.getWebsite())
            .build();
        return confederation;
    }
}
