package com.fscore.app.controller;

import com.fscore.app.dto.request.RoundRequest;
import com.fscore.app.dto.response.RoundResponse;
import com.fscore.app.dto.response.PageResponse;
import com.fscore.app.entity.Round;
import com.fscore.app.entity.*;
import com.fscore.app.service.RoundService;
import com.fscore.app.service.FilterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.net.URI;

@RestController
@RequestMapping("/api/rounds")
public class RoundController {

    private final RoundService service;
    private final FilterService filterService;

    public RoundController(RoundService service, FilterService filterService) {
        this.service = service;
        this.filterService = filterService;
    }

    @GetMapping
    public ResponseEntity<PageResponse> findAll(Pageable pageable, @RequestParam Map<String, String> params) {
        Page<Round> page = filterService.find("roundRepository", pageable, params);
        return ResponseEntity.ok(PageResponse.fromPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoundResponse> findById(@PathVariable String id) {
        Round entity = service.findById(id)
            .orElseThrow(() -> new com.fscore.app.exception.ResourceNotFoundException("Round not found with id: " + id));
        return ResponseEntity.ok(mapToResponse(entity));
    }

    @PostMapping
    public ResponseEntity<RoundResponse> save(@Valid @RequestBody RoundRequest request) {
        Round entity = mapToEntity(request);
        Round saved = service.save(entity);
        return ResponseEntity.created(URI.create("/api/rounds/" + saved.getId()))
            .body(mapToResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoundResponse> update(@PathVariable String id, @Valid @RequestBody RoundRequest request) {
        Round entity = mapToEntity(request);
        Round updated = service.update(id, entity);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private RoundResponse mapToResponse(Round entity) {
        RoundResponse response = RoundResponse.builder()
            .current(entity.getCurrent())
            .id(entity.getId())
            .name(entity.getName())
            .number(entity.getNumber())
            .slug(entity.getSlug())
            .stageId(entity.getStage() != null ? entity.getStage().getId() : null)
            .status(entity.getStatus())
            .type(entity.getType())
            .build();
        return response;
    }

    private Round mapToEntity(RoundRequest request) {
        Round round = Round.builder()
            .stage(request.getStageId() != null ? Stage.builder().id(request.getStageId()).build() : null)
            .current(request.getCurrent())
            .name(request.getName())
            .number(request.getNumber())
            .slug(request.getSlug())
            .status(request.getStatus())
            .type(request.getType())
            .build();
        return round;
    }
}
