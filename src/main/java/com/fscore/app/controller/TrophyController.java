package com.fscore.app.controller;

import com.fscore.app.dto.request.TrophyRequest;
import com.fscore.app.dto.response.TrophyResponse;
import com.fscore.app.dto.response.PageResponse;
import com.fscore.app.entity.Trophy;
import com.fscore.app.entity.*;
import com.fscore.app.service.TrophyService;
import com.fscore.app.service.FilterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.net.URI;

@RestController
@RequestMapping("/api/trophies")
public class TrophyController {

    private final TrophyService service;
    private final FilterService filterService;

    public TrophyController(TrophyService service, FilterService filterService) {
        this.service = service;
        this.filterService = filterService;
    }

    @GetMapping
    public ResponseEntity<PageResponse> findAll(Pageable pageable, @RequestParam Map<String, String> params) {
        Page<Trophy> page = filterService.find("trophyRepository", pageable, params);
        return ResponseEntity.ok(PageResponse.fromPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrophyResponse> findById(@PathVariable String id) {
        Trophy entity = service.findById(id)
            .orElseThrow(() -> new com.fscore.app.exception.ResourceNotFoundException("Trophy not found with id: " + id));
        return ResponseEntity.ok(mapToResponse(entity));
    }

    @PostMapping
    public ResponseEntity<TrophyResponse> save(@Valid @RequestBody TrophyRequest request) {
        Trophy entity = mapToEntity(request);
        Trophy saved = service.save(entity);
        return ResponseEntity.created(URI.create("/api/trophies/" + saved.getId()))
            .body(mapToResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrophyResponse> update(@PathVariable String id, @Valid @RequestBody TrophyRequest request) {
        Trophy entity = mapToEntity(request);
        Trophy updated = service.update(id, entity);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private TrophyResponse mapToResponse(Trophy entity) {
        TrophyResponse response = TrophyResponse.builder()
            .competitionId(entity.getCompetition() != null ? entity.getCompetition().getId() : null)
            .id(entity.getId())
            .logo(entity.getLogo())
            .name(entity.getName())
            .type(entity.getType())
            .build();
        return response;
    }

    private Trophy mapToEntity(TrophyRequest request) {
        Trophy trophy = Trophy.builder()
            .competition(request.getCompetitionId() != null ? Competition.builder().id(request.getCompetitionId()).build() : null)
            .logo(request.getLogo())
            .name(request.getName())
            .type(request.getType())
            .build();
        return trophy;
    }
}
