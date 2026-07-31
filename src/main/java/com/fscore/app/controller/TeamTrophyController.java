package com.fscore.app.controller;

import com.fscore.app.dto.request.TeamTrophyRequest;
import com.fscore.app.dto.response.TeamTrophyResponse;
import com.fscore.app.dto.response.PageResponse;
import com.fscore.app.entity.TeamTrophy;
import com.fscore.app.entity.*;
import com.fscore.app.service.TeamTrophyService;
import com.fscore.app.service.FilterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.net.URI;

@RestController
@RequestMapping("/api/teamtrophys")
public class TeamTrophyController {

    private final TeamTrophyService service;
    private final FilterService filterService;

    public TeamTrophyController(TeamTrophyService service, FilterService filterService) {
        this.service = service;
        this.filterService = filterService;
    }

    @GetMapping
    public ResponseEntity<PageResponse> findAll(Pageable pageable, @RequestParam Map<String, String> params) {
        Page<TeamTrophy> page = filterService.find("teamTrophyRepository", pageable, params);
        return ResponseEntity.ok(PageResponse.fromPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamTrophyResponse> findById(@PathVariable String id) {
        TeamTrophy entity = service.findById(id)
            .orElseThrow(() -> new com.fscore.app.exception.ResourceNotFoundException("TeamTrophy not found with id: " + id));
        return ResponseEntity.ok(mapToResponse(entity));
    }

    @PostMapping
    public ResponseEntity<TeamTrophyResponse> save(@Valid @RequestBody TeamTrophyRequest request) {
        TeamTrophy entity = mapToEntity(request);
        TeamTrophy saved = service.save(entity);
        return ResponseEntity.created(URI.create("/api/teamtrophys/" + saved.getId()))
            .body(mapToResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeamTrophyResponse> update(@PathVariable String id, @Valid @RequestBody TeamTrophyRequest request) {
        TeamTrophy entity = mapToEntity(request);
        TeamTrophy updated = service.update(id, entity);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private TeamTrophyResponse mapToResponse(TeamTrophy entity) {
        TeamTrophyResponse response = TeamTrophyResponse.builder()
            .id(entity.getId())
            .seasonId(entity.getSeason() != null ? entity.getSeason().getId() : null)
            .teamId(entity.getTeam() != null ? entity.getTeam().getId() : null)
            .trophyId(entity.getTrophy() != null ? entity.getTrophy().getId() : null)
            .build();
        return response;
    }

    private TeamTrophy mapToEntity(TeamTrophyRequest request) {
        TeamTrophy teamTrophy = TeamTrophy.builder()
            .season(request.getSeasonId() != null ? Season.builder().id(request.getSeasonId()).build() : null)
            .team(request.getTeamId() != null ? Team.builder().id(request.getTeamId()).build() : null)
            .trophy(request.getTrophyId() != null ? Trophy.builder().id(request.getTrophyId()).build() : null)
            .build();
        return teamTrophy;
    }
}
