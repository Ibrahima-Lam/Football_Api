package com.fscore.app.controller;

import com.fscore.app.dto.request.TeamCoachRequest;
import com.fscore.app.dto.response.TeamCoachResponse;
import com.fscore.app.dto.response.PageResponse;
import com.fscore.app.entity.TeamCoach;
import com.fscore.app.entity.*;
import com.fscore.app.service.TeamCoachService;
import com.fscore.app.service.FilterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.net.URI;

@RestController
@RequestMapping("/api/teamcoachs")
public class TeamCoachController {

    private final TeamCoachService service;
    private final FilterService filterService;

    public TeamCoachController(TeamCoachService service, FilterService filterService) {
        this.service = service;
        this.filterService = filterService;
    }

    @GetMapping
    public ResponseEntity<PageResponse> findAll(Pageable pageable, @RequestParam Map<String, String> params) {
        Page<TeamCoach> page = filterService.find("teamCoachRepository", pageable, params);
        return ResponseEntity.ok(PageResponse.fromPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamCoachResponse> findById(@PathVariable String id) {
        TeamCoach entity = service.findById(id)
            .orElseThrow(() -> new com.fscore.app.exception.ResourceNotFoundException("TeamCoach not found with id: " + id));
        return ResponseEntity.ok(mapToResponse(entity));
    }

    @PostMapping
    public ResponseEntity<TeamCoachResponse> save(@Valid @RequestBody TeamCoachRequest request) {
        TeamCoach entity = mapToEntity(request);
        TeamCoach saved = service.save(entity);
        return ResponseEntity.created(URI.create("/api/teamcoachs/" + saved.getId()))
            .body(mapToResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeamCoachResponse> update(@PathVariable String id, @Valid @RequestBody TeamCoachRequest request) {
        TeamCoach entity = mapToEntity(request);
        TeamCoach updated = service.update(id, entity);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private TeamCoachResponse mapToResponse(TeamCoach entity) {
        TeamCoachResponse response = TeamCoachResponse.builder()
            .coachId(entity.getCoach() != null ? entity.getCoach().getId() : null)
            .id(entity.getId())
            .interim(entity.getInterim())
            .role(entity.getRole())
            .seasonId(entity.getSeason() != null ? entity.getSeason().getId() : null)
            .teamId(entity.getTeam() != null ? entity.getTeam().getId() : null)
            .build();
        return response;
    }

    private TeamCoach mapToEntity(TeamCoachRequest request) {
        TeamCoach teamCoach = TeamCoach.builder()
            .coach(request.getCoachId() != null ? Coach.builder().id(request.getCoachId()).build() : null)
            .season(request.getSeasonId() != null ? Season.builder().id(request.getSeasonId()).build() : null)
            .team(request.getTeamId() != null ? Team.builder().id(request.getTeamId()).build() : null)
            .interim(request.getInterim())
            .role(request.getRole())
            .build();
        return teamCoach;
    }
}
