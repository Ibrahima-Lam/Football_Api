package com.fscore.app.controller;

import com.fscore.app.dto.request.SuspensionRequest;
import com.fscore.app.dto.response.SuspensionResponse;
import com.fscore.app.dto.response.PageResponse;
import com.fscore.app.entity.Suspension;
import com.fscore.app.entity.*;
import com.fscore.app.service.SuspensionService;
import com.fscore.app.service.FilterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.net.URI;

@RestController
@RequestMapping("/api/suspensions")
public class SuspensionController {

    private final SuspensionService service;
    private final FilterService filterService;

    public SuspensionController(SuspensionService service, FilterService filterService) {
        this.service = service;
        this.filterService = filterService;
    }

    @GetMapping
    public ResponseEntity<PageResponse> findAll(Pageable pageable, @RequestParam Map<String, String> params) {
        Page<Suspension> page = filterService.find("suspensionRepository", pageable, params);
        return ResponseEntity.ok(PageResponse.fromPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuspensionResponse> findById(@PathVariable String id) {
        Suspension entity = service.findById(id)
            .orElseThrow(() -> new com.fscore.app.exception.ResourceNotFoundException("Suspension not found with id: " + id));
        return ResponseEntity.ok(mapToResponse(entity));
    }

    @PostMapping
    public ResponseEntity<SuspensionResponse> save(@Valid @RequestBody SuspensionRequest request) {
        Suspension entity = mapToEntity(request);
        Suspension saved = service.save(entity);
        return ResponseEntity.created(URI.create("/api/suspensions/" + saved.getId()))
            .body(mapToResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuspensionResponse> update(@PathVariable String id, @Valid @RequestBody SuspensionRequest request) {
        Suspension entity = mapToEntity(request);
        Suspension updated = service.update(id, entity);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private SuspensionResponse mapToResponse(Suspension entity) {
        SuspensionResponse response = SuspensionResponse.builder()
            .cardType(entity.getCardType())
            .competitionId(entity.getCompetition() != null ? entity.getCompetition().getId() : null)
            .id(entity.getId())
            .matchesBanned(entity.getMatchesBanned())
            .matchesRemaining(entity.getMatchesRemaining())
            .playerId(entity.getPlayer() != null ? entity.getPlayer().getId() : null)
            .reason(entity.getReason())
            .seasonId(entity.getSeason() != null ? entity.getSeason().getId() : null)
            .status(entity.getStatus())
            .teamId(entity.getTeam() != null ? entity.getTeam().getId() : null)
            .build();
        return response;
    }

    private Suspension mapToEntity(SuspensionRequest request) {
        Suspension suspension = Suspension.builder()
            .competition(request.getCompetitionId() != null ? Competition.builder().id(request.getCompetitionId()).build() : null)
            .player(request.getPlayerId() != null ? Player.builder().id(request.getPlayerId()).build() : null)
            .season(request.getSeasonId() != null ? Season.builder().id(request.getSeasonId()).build() : null)
            .team(request.getTeamId() != null ? Team.builder().id(request.getTeamId()).build() : null)
            .cardType(request.getCardType())
            .matchesBanned(request.getMatchesBanned())
            .matchesRemaining(request.getMatchesRemaining())
            .reason(request.getReason())
            .status(request.getStatus())
            .build();
        return suspension;
    }
}
