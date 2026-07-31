package com.fscore.app.controller;

import com.fscore.app.dto.request.MatchStatisticsTeamRequest;
import com.fscore.app.dto.response.MatchStatisticsTeamResponse;
import com.fscore.app.dto.response.PageResponse;
import com.fscore.app.entity.MatchStatisticsTeam;
import com.fscore.app.entity.*;
import com.fscore.app.service.MatchStatisticsTeamService;
import com.fscore.app.service.FilterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.net.URI;

@RestController
@RequestMapping("/api/matchstatisticsteams")
public class MatchStatisticsTeamController {

    private final MatchStatisticsTeamService service;
    private final FilterService filterService;

    public MatchStatisticsTeamController(MatchStatisticsTeamService service, FilterService filterService) {
        this.service = service;
        this.filterService = filterService;
    }

    @GetMapping
    public ResponseEntity<PageResponse> findAll(Pageable pageable, @RequestParam Map<String, String> params) {
        Page<MatchStatisticsTeam> page = filterService.find("matchStatisticsTeamRepository", pageable, params);
        return ResponseEntity.ok(PageResponse.fromPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatchStatisticsTeamResponse> findById(@PathVariable String id) {
        MatchStatisticsTeam entity = service.findById(id)
            .orElseThrow(() -> new com.fscore.app.exception.ResourceNotFoundException("MatchStatisticsTeam not found with id: " + id));
        return ResponseEntity.ok(mapToResponse(entity));
    }

    @PostMapping
    public ResponseEntity<MatchStatisticsTeamResponse> save(@Valid @RequestBody MatchStatisticsTeamRequest request) {
        MatchStatisticsTeam entity = mapToEntity(request);
        MatchStatisticsTeam saved = service.save(entity);
        return ResponseEntity.created(URI.create("/api/matchstatisticsteams/" + saved.getId()))
            .body(mapToResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MatchStatisticsTeamResponse> update(@PathVariable String id, @Valid @RequestBody MatchStatisticsTeamRequest request) {
        MatchStatisticsTeam entity = mapToEntity(request);
        MatchStatisticsTeam updated = service.update(id, entity);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private MatchStatisticsTeamResponse mapToResponse(MatchStatisticsTeam entity) {
        MatchStatisticsTeamResponse response = MatchStatisticsTeamResponse.builder()
            .clearances(entity.getClearances())
            .corners(entity.getCorners())
            .fouls(entity.getFouls())
            .freeKicks(entity.getFreeKicks())
            .goalKicks(entity.getGoalKicks())
            .id(entity.getId())
            .interceptions(entity.getInterceptions())
            .matchId(entity.getMatch() != null ? entity.getMatch().getId() : null)
            .offsides(entity.getOffsides())
            .passes(entity.getPasses())
            .passesAccurate(entity.getPassesAccurate())
            .redCards(entity.getRedCards())
            .saves(entity.getSaves())
            .shots(entity.getShots())
            .shotsBlocked(entity.getShotsBlocked())
            .shotsOffTarget(entity.getShotsOffTarget())
            .shotsOnTarget(entity.getShotsOnTarget())
            .tackles(entity.getTackles())
            .teamId(entity.getTeam() != null ? entity.getTeam().getId() : null)
            .throwIns(entity.getThrowIns())
            .yellowCards(entity.getYellowCards())
            .yellowRedCards(entity.getYellowRedCards())
            .build();
        return response;
    }

    private MatchStatisticsTeam mapToEntity(MatchStatisticsTeamRequest request) {
        MatchStatisticsTeam matchStatisticsTeam = MatchStatisticsTeam.builder()
            .match(request.getMatchId() != null ? Match.builder().id(request.getMatchId()).build() : null)
            .team(request.getTeamId() != null ? Team.builder().id(request.getTeamId()).build() : null)
            .clearances(request.getClearances())
            .corners(request.getCorners())
            .fouls(request.getFouls())
            .freeKicks(request.getFreeKicks())
            .goalKicks(request.getGoalKicks())
            .interceptions(request.getInterceptions())
            .offsides(request.getOffsides())
            .passes(request.getPasses())
            .passesAccurate(request.getPassesAccurate())
            .redCards(request.getRedCards())
            .saves(request.getSaves())
            .shots(request.getShots())
            .shotsBlocked(request.getShotsBlocked())
            .shotsOffTarget(request.getShotsOffTarget())
            .shotsOnTarget(request.getShotsOnTarget())
            .tackles(request.getTackles())
            .throwIns(request.getThrowIns())
            .yellowCards(request.getYellowCards())
            .yellowRedCards(request.getYellowRedCards())
            .build();
        return matchStatisticsTeam;
    }
}
