package com.fscore.app.controller;

import com.fscore.app.dto.request.SponsorLinkRequest;
import com.fscore.app.dto.response.SponsorLinkResponse;
import com.fscore.app.dto.response.PageResponse;
import com.fscore.app.entity.SponsorLink;
import com.fscore.app.entity.*;
import com.fscore.app.service.SponsorLinkService;
import com.fscore.app.service.FilterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.net.URI;

@RestController
@RequestMapping("/api/sponsorlinks")
public class SponsorLinkController {

    private final SponsorLinkService service;
    private final FilterService filterService;

    public SponsorLinkController(SponsorLinkService service, FilterService filterService) {
        this.service = service;
        this.filterService = filterService;
    }

    @GetMapping
    public ResponseEntity<PageResponse> findAll(Pageable pageable, @RequestParam Map<String, String> params) {
        Page<SponsorLink> page = filterService.find("sponsorLinkRepository", pageable, params);
        return ResponseEntity.ok(PageResponse.fromPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SponsorLinkResponse> findById(@PathVariable String id) {
        SponsorLink entity = service.findById(id)
            .orElseThrow(() -> new com.fscore.app.exception.ResourceNotFoundException("SponsorLink not found with id: " + id));
        return ResponseEntity.ok(mapToResponse(entity));
    }

    @PostMapping
    public ResponseEntity<SponsorLinkResponse> save(@Valid @RequestBody SponsorLinkRequest request) {
        SponsorLink entity = mapToEntity(request);
        SponsorLink saved = service.save(entity);
        return ResponseEntity.created(URI.create("/api/sponsorlinks/" + saved.getId()))
            .body(mapToResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SponsorLinkResponse> update(@PathVariable String id, @Valid @RequestBody SponsorLinkRequest request) {
        SponsorLink entity = mapToEntity(request);
        SponsorLink updated = service.update(id, entity);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private SponsorLinkResponse mapToResponse(SponsorLink entity) {
        SponsorLinkResponse response = SponsorLinkResponse.builder()
            .entityId(entity.getEntityId())
            .entityType(entity.getEntityType())
            .id(entity.getId())
            .sponsorId(entity.getSponsor() != null ? entity.getSponsor().getId() : null)
            .build();
        return response;
    }

    private SponsorLink mapToEntity(SponsorLinkRequest request) {
        SponsorLink sponsorLink = SponsorLink.builder()
            .sponsor(request.getSponsorId() != null ? Sponsor.builder().id(request.getSponsorId()).build() : null)
            .entityId(request.getEntityId())
            .entityType(request.getEntityType())
            .build();
        return sponsorLink;
    }
}
