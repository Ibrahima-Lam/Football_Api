package com.fscore.app.controller;

import com.fscore.app.dto.request.LineupRequest;
import com.fscore.app.dto.response.LineupResponse;
import com.fscore.app.dto.response.PageResponse;
import com.fscore.app.entity.Lineup;
import com.fscore.app.entity.*;
import com.fscore.app.service.LineupService;
import com.fscore.app.service.FilterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.net.URI;

@RestController
@RequestMapping("/api/lineups")
public class LineupController {

    private final LineupService service;
    private final FilterService filterService;

    public LineupController(LineupService service, FilterService filterService) {
        this.service = service;
        this.filterService = filterService;
    }

    @GetMapping
    public ResponseEntity<PageResponse> findAll(Pageable pageable, @RequestParam Map<String, String> params) {
        Page<Lineup> page = filterService.find("lineupRepository", pageable, params);
        return ResponseEntity.ok(PageResponse.fromPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineupResponse> findById(@PathVariable String id) {
        Lineup entity = service.findById(id)
            .orElseThrow(() -> new com.fscore.app.exception.ResourceNotFoundException("Lineup not found with id: " + id));
        return ResponseEntity.ok(mapToResponse(entity));
    }

    @PostMapping
    public ResponseEntity<LineupResponse> save(@Valid @RequestBody LineupRequest request) {
        Lineup entity = mapToEntity(request);
        Lineup saved = service.save(entity);
        return ResponseEntity.created(URI.create("/api/lineups/" + saved.getId()))
            .body(mapToResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LineupResponse> update(@PathVariable String id, @Valid @RequestBody LineupRequest request) {
        Lineup entity = mapToEntity(request);
        Lineup updated = service.update(id, entity);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private LineupResponse mapToResponse(Lineup entity) {
        LineupResponse response = LineupResponse.builder()
            .captain(entity.getCaptain())
            .formationSlot(entity.getFormationSlot())
            .id(entity.getId())
            .matchId(entity.getMatch() != null ? entity.getMatch().getId() : null)
            .playerId(entity.getPlayer() != null ? entity.getPlayer().getId() : null)
            .position(entity.getPosition())
            .shirtNumber(entity.getShirtNumber())
            .starter(entity.getStarter())
            .teamId(entity.getTeam() != null ? entity.getTeam().getId() : null)
            .build();
        return response;
    }

    private Lineup mapToEntity(LineupRequest request) {
        Lineup lineup = Lineup.builder()
            .match(request.getMatchId() != null ? Match.builder().id(request.getMatchId()).build() : null)
            .player(request.getPlayerId() != null ? Player.builder().id(request.getPlayerId()).build() : null)
            .team(request.getTeamId() != null ? Team.builder().id(request.getTeamId()).build() : null)
            .captain(request.getCaptain())
            .formationSlot(request.getFormationSlot())
            .position(request.getPosition())
            .shirtNumber(request.getShirtNumber())
            .starter(request.getStarter())
            .build();
        return lineup;
    }
}
