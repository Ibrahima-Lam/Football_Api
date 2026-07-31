package com.fscore.app.controller;

import com.fscore.app.dto.request.TeamSeasonParticipationRequest;
import com.fscore.app.dto.response.TeamSeasonParticipationResponse;
import com.fscore.app.dto.response.PageResponse;
import com.fscore.app.entity.TeamSeasonParticipation;
import com.fscore.app.entity.*;
import com.fscore.app.service.TeamSeasonParticipationService;
import com.fscore.app.service.FilterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.net.URI;

@RestController
@RequestMapping("/api/teamseasonparticipations")
public class TeamSeasonParticipationController {

    private final TeamSeasonParticipationService service;
    private final FilterService filterService;

    public TeamSeasonParticipationController(TeamSeasonParticipationService service, FilterService filterService) {
        this.service = service;
        this.filterService = filterService;
    }

    @GetMapping
    public ResponseEntity<PageResponse> findAll(Pageable pageable, @RequestParam Map<String, String> params) {
        Page<TeamSeasonParticipation> page = filterService.find("teamSeasonParticipationRepository", pageable, params);
        return ResponseEntity.ok(PageResponse.fromPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamSeasonParticipationResponse> findById(@PathVariable String id) {
        TeamSeasonParticipation entity = service.findById(id)
            .orElseThrow(() -> new com.fscore.app.exception.ResourceNotFoundException("TeamSeasonParticipation not found with id: " + id));
        return ResponseEntity.ok(mapToResponse(entity));
    }

    @PostMapping
    public ResponseEntity<TeamSeasonParticipationResponse> save(@Valid @RequestBody TeamSeasonParticipationRequest request) {
        TeamSeasonParticipation entity = mapToEntity(request);
        TeamSeasonParticipation saved = service.save(entity);
        return ResponseEntity.created(URI.create("/api/teamseasonparticipations/" + saved.getId()))
            .body(mapToResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeamSeasonParticipationResponse> update(@PathVariable String id, @Valid @RequestBody TeamSeasonParticipationRequest request) {
        TeamSeasonParticipation entity = mapToEntity(request);
        TeamSeasonParticipation updated = service.update(id, entity);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private TeamSeasonParticipationResponse mapToResponse(TeamSeasonParticipation entity) {
        TeamSeasonParticipationResponse response = TeamSeasonParticipationResponse.builder()
            .entryFromCompetitionId(entity.getEntryFromCompetitionId())
            .entryType(entity.getEntryType())
            .finalRank(entity.getFinalRank())
            .id(entity.getId())
            .outcome(entity.getOutcome())
            .seasonId(entity.getSeason() != null ? entity.getSeason().getId() : null)
            .teamId(entity.getTeam() != null ? entity.getTeam().getId() : null)
            .withdrawalDate(entity.getWithdrawalDate() != null ? entity.getWithdrawalDate().toString() : null)
            .withdrawn(entity.getWithdrawn())
            .build();
        return response;
    }

    private TeamSeasonParticipation mapToEntity(TeamSeasonParticipationRequest request) {
        TeamSeasonParticipation teamSeasonParticipation = TeamSeasonParticipation.builder()
            .season(request.getSeasonId() != null ? Season.builder().id(request.getSeasonId()).build() : null)
            .team(request.getTeamId() != null ? Team.builder().id(request.getTeamId()).build() : null)
            .entryFromCompetitionId(request.getEntryFromCompetitionId())
            .entryType(request.getEntryType())
            .finalRank(request.getFinalRank())
            .outcome(request.getOutcome())
            .withdrawalDate(request.getWithdrawalDate() != null ? java.time.LocalDate.parse(request.getWithdrawalDate()) : null)
            .withdrawn(request.getWithdrawn() != null ? request.getWithdrawn() : false)
            .build();
        return teamSeasonParticipation;
    }
}
