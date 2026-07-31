package com.fscore.app.controller;

import com.fscore.app.dto.request.MatchFormationRequest;
import com.fscore.app.dto.response.MatchFormationResponse;
import com.fscore.app.dto.response.PageResponse;
import com.fscore.app.entity.MatchFormation;
import com.fscore.app.entity.*;
import com.fscore.app.service.MatchFormationService;
import com.fscore.app.service.FilterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.net.URI;

@RestController
@RequestMapping("/api/matchformations")
public class MatchFormationController {

    private final MatchFormationService service;
    private final FilterService filterService;

    public MatchFormationController(MatchFormationService service, FilterService filterService) {
        this.service = service;
        this.filterService = filterService;
    }

    @GetMapping
    public ResponseEntity<PageResponse> findAll(Pageable pageable, @RequestParam Map<String, String> params) {
        Page<MatchFormation> page = filterService.find("matchFormationRepository", pageable, params);
        return ResponseEntity.ok(PageResponse.fromPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatchFormationResponse> findById(@PathVariable String id) {
        MatchFormation entity = service.findById(id)
            .orElseThrow(() -> new com.fscore.app.exception.ResourceNotFoundException("MatchFormation not found with id: " + id));
        return ResponseEntity.ok(mapToResponse(entity));
    }

    @PostMapping
    public ResponseEntity<MatchFormationResponse> save(@Valid @RequestBody MatchFormationRequest request) {
        MatchFormation entity = mapToEntity(request);
        MatchFormation saved = service.save(entity);
        return ResponseEntity.created(URI.create("/api/matchformations/" + saved.getId()))
            .body(mapToResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MatchFormationResponse> update(@PathVariable String id, @Valid @RequestBody MatchFormationRequest request) {
        MatchFormation entity = mapToEntity(request);
        MatchFormation updated = service.update(id, entity);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private MatchFormationResponse mapToResponse(MatchFormation entity) {
        MatchFormationResponse response = MatchFormationResponse.builder()
            .formation(entity.getFormation())
            .id(entity.getId())
            .matchId(entity.getMatch() != null ? entity.getMatch().getId() : null)
            .teamId(entity.getTeam() != null ? entity.getTeam().getId() : null)
            .build();
        return response;
    }

    private MatchFormation mapToEntity(MatchFormationRequest request) {
        MatchFormation matchFormation = MatchFormation.builder()
            .match(request.getMatchId() != null ? Match.builder().id(request.getMatchId()).build() : null)
            .team(request.getTeamId() != null ? Team.builder().id(request.getTeamId()).build() : null)
            .formation(request.getFormation())
            .build();
        return matchFormation;
    }
}
