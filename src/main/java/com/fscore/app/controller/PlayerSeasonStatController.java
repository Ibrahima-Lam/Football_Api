package com.fscore.app.controller;

import com.fscore.app.dto.request.PlayerSeasonStatRequest;
import com.fscore.app.dto.response.PlayerSeasonStatResponse;
import com.fscore.app.dto.response.PageResponse;
import com.fscore.app.entity.PlayerSeasonStat;
import com.fscore.app.entity.*;
import com.fscore.app.service.PlayerSeasonStatService;
import com.fscore.app.service.FilterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.net.URI;

@RestController
@RequestMapping("/api/playerseasonstats")
public class PlayerSeasonStatController {

    private final PlayerSeasonStatService service;
    private final FilterService filterService;

    public PlayerSeasonStatController(PlayerSeasonStatService service, FilterService filterService) {
        this.service = service;
        this.filterService = filterService;
    }

    @GetMapping
    public ResponseEntity<PageResponse> findAll(Pageable pageable, @RequestParam Map<String, String> params) {
        Page<PlayerSeasonStat> page = filterService.find("playerSeasonStatRepository", pageable, params);
        return ResponseEntity.ok(PageResponse.fromPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerSeasonStatResponse> findById(@PathVariable String id) {
        PlayerSeasonStat entity = service.findById(id)
            .orElseThrow(() -> new com.fscore.app.exception.ResourceNotFoundException("PlayerSeasonStat not found with id: " + id));
        return ResponseEntity.ok(mapToResponse(entity));
    }

    @PostMapping
    public ResponseEntity<PlayerSeasonStatResponse> save(@Valid @RequestBody PlayerSeasonStatRequest request) {
        PlayerSeasonStat entity = mapToEntity(request);
        PlayerSeasonStat saved = service.save(entity);
        return ResponseEntity.created(URI.create("/api/playerseasonstats/" + saved.getId()))
            .body(mapToResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlayerSeasonStatResponse> update(@PathVariable String id, @Valid @RequestBody PlayerSeasonStatRequest request) {
        PlayerSeasonStat entity = mapToEntity(request);
        PlayerSeasonStat updated = service.update(id, entity);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private PlayerSeasonStatResponse mapToResponse(PlayerSeasonStat entity) {
        PlayerSeasonStatResponse response = PlayerSeasonStatResponse.builder()
            .appearances(entity.getAppearances())
            .appearancesAsStarter(entity.getAppearancesAsStarter())
            .assists(entity.getAssists())
            .cleanSheets(entity.getCleanSheets())
            .competitionId(entity.getCompetition() != null ? entity.getCompetition().getId() : null)
            .dribblesAttempted(entity.getDribblesAttempted())
            .dribblesSucceeded(entity.getDribblesSucceeded())
            .foulsCommitted(entity.getFoulsCommitted())
            .foulsDrawn(entity.getFoulsDrawn())
            .goals(entity.getGoals())
            .goalsConceded(entity.getGoalsConceded())
            .id(entity.getId())
            .interceptions(entity.getInterceptions())
            .keyPasses(entity.getKeyPasses())
            .minutesPlayed(entity.getMinutesPlayed())
            .passes(entity.getPasses())
            .passesAccurate(entity.getPassesAccurate())
            .playerId(entity.getPlayer() != null ? entity.getPlayer().getId() : null)
            .redCards(entity.getRedCards())
            .saves(entity.getSaves())
            .seasonId(entity.getSeason() != null ? entity.getSeason().getId() : null)
            .shots(entity.getShots())
            .shotsOnTarget(entity.getShotsOnTarget())
            .tackles(entity.getTackles())
            .teamId(entity.getTeam() != null ? entity.getTeam().getId() : null)
            .yellowCards(entity.getYellowCards())
            .build();
        return response;
    }

    private PlayerSeasonStat mapToEntity(PlayerSeasonStatRequest request) {
        PlayerSeasonStat playerSeasonStat = PlayerSeasonStat.builder()
            .competition(request.getCompetitionId() != null ? Competition.builder().id(request.getCompetitionId()).build() : null)
            .player(request.getPlayerId() != null ? Player.builder().id(request.getPlayerId()).build() : null)
            .season(request.getSeasonId() != null ? Season.builder().id(request.getSeasonId()).build() : null)
            .team(request.getTeamId() != null ? Team.builder().id(request.getTeamId()).build() : null)
            .appearances(request.getAppearances())
            .appearancesAsStarter(request.getAppearancesAsStarter())
            .assists(request.getAssists())
            .cleanSheets(request.getCleanSheets())
            .dribblesAttempted(request.getDribblesAttempted())
            .dribblesSucceeded(request.getDribblesSucceeded())
            .foulsCommitted(request.getFoulsCommitted())
            .foulsDrawn(request.getFoulsDrawn())
            .goals(request.getGoals())
            .goalsConceded(request.getGoalsConceded())
            .interceptions(request.getInterceptions())
            .keyPasses(request.getKeyPasses())
            .minutesPlayed(request.getMinutesPlayed())
            .passes(request.getPasses())
            .passesAccurate(request.getPassesAccurate())
            .redCards(request.getRedCards())
            .saves(request.getSaves())
            .shots(request.getShots())
            .shotsOnTarget(request.getShotsOnTarget())
            .tackles(request.getTackles())
            .yellowCards(request.getYellowCards())
            .build();
        return playerSeasonStat;
    }
}
