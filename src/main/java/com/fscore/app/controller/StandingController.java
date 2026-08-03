package com.fscore.app.controller;

import com.fscore.app.dto.request.StandingCalculateRequest;
import com.fscore.app.dto.request.StandingRequest;
import com.fscore.app.dto.response.StandingResponse;
import com.fscore.app.dto.response.PageResponse;
import com.fscore.app.entity.Standing;
import com.fscore.app.entity.*;
import com.fscore.app.service.StandingService;
import com.fscore.app.service.FilterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.net.URI;

@RestController
@RequestMapping("/api/standings")
public class StandingController {

    private final StandingService service;
    private final FilterService filterService;

    public StandingController(StandingService service, FilterService filterService) {
        this.service = service;
        this.filterService = filterService;
    }

    @GetMapping
    public ResponseEntity<PageResponse> findAll(Pageable pageable, @RequestParam Map<String, String> params) {
        Page<Standing> page = filterService.find("standingRepository", pageable, params);
        return ResponseEntity.ok(PageResponse.fromPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StandingResponse> findById(@PathVariable String id) {
        Standing entity = service.findById(id)
            .orElseThrow(() -> new com.fscore.app.exception.ResourceNotFoundException("Standing not found with id: " + id));
        return ResponseEntity.ok(mapToResponse(entity));
    }

    @PostMapping
    public ResponseEntity<StandingResponse> save(@Valid @RequestBody StandingRequest request) {
        Standing entity = mapToEntity(request);
        Standing saved = service.save(entity);
        return ResponseEntity.created(URI.create("/api/standings/" + saved.getId()))
            .body(mapToResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StandingResponse> update(@PathVariable String id, @Valid @RequestBody StandingRequest request) {
        Standing entity = mapToEntity(request);
        Standing updated = service.update(id, entity);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/calculate")
    public ResponseEntity<java.util.List<StandingResponse>> calculate(@RequestBody StandingCalculateRequest request) {
        return ResponseEntity.ok(service.calculate(request.getGroupId()));
    }

    @PostMapping("/save")
    public ResponseEntity<java.util.List<StandingResponse>> saveCalculated(@RequestBody StandingCalculateRequest request) {
        return ResponseEntity.ok(service.saveCalculated(request.getGroupId()));
    }

    private StandingResponse mapToResponse(Standing entity) {
        StandingResponse response = StandingResponse.builder()
            .awayDraws(entity.getAwayDraws())
            .awayLosses(entity.getAwayLosses())
            .awayWins(entity.getAwayWins())
            .draws(entity.getDraws())
            .form(entity.getForm())
            .goalDifference(entity.getGoalDifference())
            .goalsAgainst(entity.getGoalsAgainst())
            .goalsFor(entity.getGoalsFor())
            .groupId(entity.getGroup() != null ? entity.getGroup().getId() : null)
            .homeDraws(entity.getHomeDraws())
            .homeLosses(entity.getHomeLosses())
            .homeWins(entity.getHomeWins())
            .id(entity.getId())
            .losses(entity.getLosses())
            .played(entity.getPlayed())
            .points(entity.getPoints())
            .rankPosition(entity.getRankPosition())
            .seasonId(entity.getSeason() != null ? entity.getSeason().getId() : null)
            .stageId(entity.getStage() != null ? entity.getStage().getId() : null)
            .teamId(entity.getTeam() != null ? entity.getTeam().getId() : null)
            .wins(entity.getWins())
            .build();
        return response;
    }

    private Standing mapToEntity(StandingRequest request) {
        Standing standing = Standing.builder()
            .group(request.getGroupId() != null ? Group.builder().id(request.getGroupId()).build() : null)
            .season(request.getSeasonId() != null ? Season.builder().id(request.getSeasonId()).build() : null)
            .stage(request.getStageId() != null ? Stage.builder().id(request.getStageId()).build() : null)
            .team(request.getTeamId() != null ? Team.builder().id(request.getTeamId()).build() : null)
            .awayDraws(orZero(request.getAwayDraws()))
            .awayLosses(orZero(request.getAwayLosses()))
            .awayWins(orZero(request.getAwayWins()))
            .draws(orZero(request.getDraws()))
            .form(request.getForm())
            .goalDifference(orZero(request.getGoalDifference()))
            .goalsAgainst(orZero(request.getGoalsAgainst()))
            .goalsFor(orZero(request.getGoalsFor()))
            .homeDraws(orZero(request.getHomeDraws()))
            .homeLosses(orZero(request.getHomeLosses()))
            .homeWins(orZero(request.getHomeWins()))
            .losses(orZero(request.getLosses()))
            .played(orZero(request.getPlayed()))
            .points(orZero(request.getPoints()))
            .rankPosition(orZero(request.getRankPosition()))
            .wins(orZero(request.getWins()))
            .build();
        return standing;
    }

    private static int orZero(Integer value) {
        return value != null ? value : 0;
    }
}
