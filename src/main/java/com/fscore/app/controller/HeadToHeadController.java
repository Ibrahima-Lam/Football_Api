package com.fscore.app.controller;

import com.fscore.app.dto.request.HeadToHeadRequest;
import com.fscore.app.dto.response.HeadToHeadResponse;
import com.fscore.app.dto.response.PageResponse;
import com.fscore.app.entity.HeadToHead;
import com.fscore.app.entity.*;
import com.fscore.app.service.HeadToHeadService;
import com.fscore.app.service.FilterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.net.URI;

@RestController
@RequestMapping("/api/head-to-head")
public class HeadToHeadController {

    private final HeadToHeadService service;
    private final FilterService filterService;

    public HeadToHeadController(HeadToHeadService service, FilterService filterService) {
        this.service = service;
        this.filterService = filterService;
    }

    @GetMapping
    public ResponseEntity<PageResponse> findAll(Pageable pageable, @RequestParam Map<String, String> params) {
        Page<HeadToHead> page = filterService.find("headToHeadRepository", pageable, params);
        return ResponseEntity.ok(PageResponse.fromPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HeadToHeadResponse> findById(@PathVariable String id) {
        HeadToHead entity = service.findById(id)
            .orElseThrow(() -> new com.fscore.app.exception.ResourceNotFoundException("HeadToHead not found with id: " + id));
        return ResponseEntity.ok(mapToResponse(entity));
    }

    @PostMapping
    public ResponseEntity<HeadToHeadResponse> save(@Valid @RequestBody HeadToHeadRequest request) {
        HeadToHead entity = mapToEntity(request);
        HeadToHead saved = service.save(entity);
        return ResponseEntity.created(URI.create("/api/head-to-head/" + saved.getId()))
            .body(mapToResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HeadToHeadResponse> update(@PathVariable String id, @Valid @RequestBody HeadToHeadRequest request) {
        HeadToHead entity = mapToEntity(request);
        HeadToHead updated = service.update(id, entity);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private HeadToHeadResponse mapToResponse(HeadToHead entity) {
        HeadToHeadResponse response = HeadToHeadResponse.builder()
            .draws(entity.getDraws())
            .id(entity.getId())
            .team1Goals(entity.getTeam1Goals())
            .team1Id(entity.getTeam1() != null ? entity.getTeam1().getId() : null)
            .team1Wins(entity.getTeam1Wins())
            .team2Goals(entity.getTeam2Goals())
            .team2Id(entity.getTeam2() != null ? entity.getTeam2().getId() : null)
            .team2Wins(entity.getTeam2Wins())
            .totalMatches(entity.getTotalMatches())
            .build();
        return response;
    }

    private HeadToHead mapToEntity(HeadToHeadRequest request) {
        HeadToHead headToHead = HeadToHead.builder()
            .team1(request.getTeam1Id() != null ? Team.builder().id(request.getTeam1Id()).build() : null)
            .team2(request.getTeam2Id() != null ? Team.builder().id(request.getTeam2Id()).build() : null)
            .draws(request.getDraws())
            .team1Goals(request.getTeam1Goals())
            .team1Wins(request.getTeam1Wins())
            .team2Goals(request.getTeam2Goals())
            .team2Wins(request.getTeam2Wins())
            .totalMatches(request.getTotalMatches())
            .build();
        return headToHead;
    }
}
