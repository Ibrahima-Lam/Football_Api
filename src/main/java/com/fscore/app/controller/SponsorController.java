package com.fscore.app.controller;

import com.fscore.app.dto.request.SponsorRequest;
import com.fscore.app.dto.response.SponsorResponse;
import com.fscore.app.dto.response.PageResponse;
import com.fscore.app.entity.Sponsor;
import com.fscore.app.entity.*;
import com.fscore.app.service.SponsorService;
import com.fscore.app.service.FilterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.net.URI;

@RestController
@RequestMapping("/api/sponsors")
public class SponsorController {

    private final SponsorService service;
    private final FilterService filterService;

    public SponsorController(SponsorService service, FilterService filterService) {
        this.service = service;
        this.filterService = filterService;
    }

    @GetMapping
    public ResponseEntity<PageResponse> findAll(Pageable pageable, @RequestParam Map<String, String> params) {
        Page<Sponsor> page = filterService.find("sponsorRepository", pageable, params);
        return ResponseEntity.ok(PageResponse.fromPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SponsorResponse> findById(@PathVariable String id) {
        Sponsor entity = service.findById(id)
            .orElseThrow(() -> new com.fscore.app.exception.ResourceNotFoundException("Sponsor not found with id: " + id));
        return ResponseEntity.ok(mapToResponse(entity));
    }

    @PostMapping
    public ResponseEntity<SponsorResponse> save(@Valid @RequestBody SponsorRequest request) {
        Sponsor entity = mapToEntity(request);
        Sponsor saved = service.save(entity);
        return ResponseEntity.created(URI.create("/api/sponsors/" + saved.getId()))
            .body(mapToResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SponsorResponse> update(@PathVariable String id, @Valid @RequestBody SponsorRequest request) {
        Sponsor entity = mapToEntity(request);
        Sponsor updated = service.update(id, entity);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private SponsorResponse mapToResponse(Sponsor entity) {
        SponsorResponse response = SponsorResponse.builder()
            .description(entity.getDescription())
            .id(entity.getId())
            .imageUrl(entity.getImageUrl())
            .nom(entity.getNom())
            .websiteUrl(entity.getWebsiteUrl())
            .build();
        return response;
    }

    private Sponsor mapToEntity(SponsorRequest request) {
        Sponsor sponsor = Sponsor.builder()
            .description(request.getDescription())
            .imageUrl(request.getImageUrl())
            .nom(request.getNom())
            .websiteUrl(request.getWebsiteUrl())
            .build();
        return sponsor;
    }
}
