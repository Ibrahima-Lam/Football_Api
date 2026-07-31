package com.fscore.app.controller;

import com.fscore.app.dto.request.StageRequest;
import com.fscore.app.dto.response.StageResponse;
import com.fscore.app.dto.response.PageResponse;
import com.fscore.app.entity.Stage;
import com.fscore.app.entity.*;
import com.fscore.app.service.StageService;
import com.fscore.app.service.FilterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.net.URI;

@RestController
@RequestMapping("/api/stages")
public class StageController {

    private final StageService service;
    private final FilterService filterService;

    public StageController(StageService service, FilterService filterService) {
        this.service = service;
        this.filterService = filterService;
    }

    @GetMapping
    public ResponseEntity<PageResponse> findAll(Pageable pageable, @RequestParam Map<String, String> params) {
        Page<Stage> page = filterService.find("stageRepository", pageable, params);
        return ResponseEntity.ok(PageResponse.fromPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StageResponse> findById(@PathVariable String id) {
        Stage entity = service.findById(id)
            .orElseThrow(() -> new com.fscore.app.exception.ResourceNotFoundException("Stage not found with id: " + id));
        return ResponseEntity.ok(mapToResponse(entity));
    }

    @PostMapping
    public ResponseEntity<StageResponse> save(@Valid @RequestBody StageRequest request) {
        Stage entity = mapToEntity(request);
        Stage saved = service.save(entity);
        return ResponseEntity.created(URI.create("/api/stages/" + saved.getId()))
            .body(mapToResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StageResponse> update(@PathVariable String id, @Valid @RequestBody StageRequest request) {
        Stage entity = mapToEntity(request);
        Stage updated = service.update(id, entity);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private StageResponse mapToResponse(Stage entity) {
        StageResponse response = StageResponse.builder()
            .id(entity.getId())
            .name(entity.getName())
            .orderNo(entity.getOrderNo())
            .seasonId(entity.getSeason() != null ? entity.getSeason().getId() : null)
            .type(entity.getType())
            .build();
        return response;
    }

    private Stage mapToEntity(StageRequest request) {
        Stage stage = Stage.builder()
            .season(request.getSeasonId() != null ? Season.builder().id(request.getSeasonId()).build() : null)
            .name(request.getName())
            .orderNo(request.getOrderNo())
            .type(request.getType())
            .build();
        return stage;
    }
}
