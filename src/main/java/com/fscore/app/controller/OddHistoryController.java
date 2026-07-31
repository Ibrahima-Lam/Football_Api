package com.fscore.app.controller;

import com.fscore.app.dto.request.OddHistoryRequest;
import com.fscore.app.dto.response.OddHistoryResponse;
import com.fscore.app.dto.response.PageResponse;
import com.fscore.app.entity.OddHistory;
import com.fscore.app.entity.*;
import com.fscore.app.service.OddHistoryService;
import com.fscore.app.service.FilterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.net.URI;

@RestController
@RequestMapping("/api/oddhistorys")
public class OddHistoryController {

    private final OddHistoryService service;
    private final FilterService filterService;

    public OddHistoryController(OddHistoryService service, FilterService filterService) {
        this.service = service;
        this.filterService = filterService;
    }

    @GetMapping
    public ResponseEntity<PageResponse> findAll(Pageable pageable, @RequestParam Map<String, String> params) {
        Page<OddHistory> page = filterService.find("oddHistoryRepository", pageable, params);
        return ResponseEntity.ok(PageResponse.fromPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OddHistoryResponse> findById(@PathVariable String id) {
        OddHistory entity = service.findById(id)
            .orElseThrow(() -> new com.fscore.app.exception.ResourceNotFoundException("OddHistory not found with id: " + id));
        return ResponseEntity.ok(mapToResponse(entity));
    }

    @PostMapping
    public ResponseEntity<OddHistoryResponse> save(@Valid @RequestBody OddHistoryRequest request) {
        OddHistory entity = mapToEntity(request);
        OddHistory saved = service.save(entity);
        return ResponseEntity.created(URI.create("/api/oddhistorys/" + saved.getId()))
            .body(mapToResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OddHistoryResponse> update(@PathVariable String id, @Valid @RequestBody OddHistoryRequest request) {
        OddHistory entity = mapToEntity(request);
        OddHistory updated = service.update(id, entity);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private OddHistoryResponse mapToResponse(OddHistory entity) {
        OddHistoryResponse response = OddHistoryResponse.builder()
            .bookmakerId(entity.getBookmaker() != null ? entity.getBookmaker().getId() : null)
            .id(entity.getId())
            .market(entity.getMarket())
            .matchId(entity.getMatch() != null ? entity.getMatch().getId() : null)
            .selection(entity.getSelection())
            .build();
        return response;
    }

    private OddHistory mapToEntity(OddHistoryRequest request) {
        OddHistory oddHistory = OddHistory.builder()
            .bookmaker(request.getBookmakerId() != null ? Bookmaker.builder().id(request.getBookmakerId()).build() : null)
            .match(request.getMatchId() != null ? Match.builder().id(request.getMatchId()).build() : null)
            .market(request.getMarket())
            .selection(request.getSelection())
            .build();
        return oddHistory;
    }
}
