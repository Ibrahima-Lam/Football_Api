package com.fscore.app.controller;

import com.fscore.app.dto.request.MatchStatisticsPlayerRequest;
import com.fscore.app.dto.response.MatchStatisticsPlayerResponse;
import com.fscore.app.dto.response.PageResponse;
import com.fscore.app.entity.MatchStatisticsPlayer;
import com.fscore.app.entity.*;
import com.fscore.app.service.MatchStatisticsPlayerService;
import com.fscore.app.service.FilterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.net.URI;

@RestController
@RequestMapping("/api/matchstatisticsplayers")
public class MatchStatisticsPlayerController {

    private final MatchStatisticsPlayerService service;
    private final FilterService filterService;

    public MatchStatisticsPlayerController(MatchStatisticsPlayerService service, FilterService filterService) {
        this.service = service;
        this.filterService = filterService;
    }

    @GetMapping
    public ResponseEntity<PageResponse> findAll(Pageable pageable, @RequestParam Map<String, String> params) {
        Page<MatchStatisticsPlayer> page = filterService.find("matchStatisticsPlayerRepository", pageable, params);
        return ResponseEntity.ok(PageResponse.fromPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatchStatisticsPlayerResponse> findById(@PathVariable String id) {
        MatchStatisticsPlayer entity = service.findById(id)
            .orElseThrow(() -> new com.fscore.app.exception.ResourceNotFoundException("MatchStatisticsPlayer not found with id: " + id));
        return ResponseEntity.ok(mapToResponse(entity));
    }

    @PostMapping
    public ResponseEntity<MatchStatisticsPlayerResponse> save(@Valid @RequestBody MatchStatisticsPlayerRequest request) {
        MatchStatisticsPlayer entity = mapToEntity(request);
        MatchStatisticsPlayer saved = service.save(entity);
        return ResponseEntity.created(URI.create("/api/matchstatisticsplayers/" + saved.getId()))
            .body(mapToResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MatchStatisticsPlayerResponse> update(@PathVariable String id, @Valid @RequestBody MatchStatisticsPlayerRequest request) {
        MatchStatisticsPlayer entity = mapToEntity(request);
        MatchStatisticsPlayer updated = service.update(id, entity);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private MatchStatisticsPlayerResponse mapToResponse(MatchStatisticsPlayer entity) {
        MatchStatisticsPlayerResponse response = MatchStatisticsPlayerResponse.builder()
            .assists(entity.getAssists())
            .clearances(entity.getClearances())
            .crosses(entity.getCrosses())
            .dribblesAttempted(entity.getDribblesAttempted())
            .dribblesSucceeded(entity.getDribblesSucceeded())
            .foulsCommitted(entity.getFoulsCommitted())
            .foulsDrawn(entity.getFoulsDrawn())
            .goals(entity.getGoals())
            .goalsConceded(entity.getGoalsConceded())
            .id(entity.getId())
            .interceptions(entity.getInterceptions())
            .keyPasses(entity.getKeyPasses())
            .longBalls(entity.getLongBalls())
            .matchId(entity.getMatch() != null ? entity.getMatch().getId() : null)
            .minutesPlayed(entity.getMinutesPlayed())
            .passes(entity.getPasses())
            .passesAccurate(entity.getPassesAccurate())
            .playerId(entity.getPlayer() != null ? entity.getPlayer().getId() : null)
            .redCards(entity.getRedCards())
            .saves(entity.getSaves())
            .shots(entity.getShots())
            .shotsOnTarget(entity.getShotsOnTarget())
            .tackles(entity.getTackles())
            .teamId(entity.getTeam() != null ? entity.getTeam().getId() : null)
            .yellowCards(entity.getYellowCards())
            .build();
        return response;
    }

    private MatchStatisticsPlayer mapToEntity(MatchStatisticsPlayerRequest request) {
        MatchStatisticsPlayer matchStatisticsPlayer = MatchStatisticsPlayer.builder()
            .match(request.getMatchId() != null ? Match.builder().id(request.getMatchId()).build() : null)
            .player(request.getPlayerId() != null ? Player.builder().id(request.getPlayerId()).build() : null)
            .team(request.getTeamId() != null ? Team.builder().id(request.getTeamId()).build() : null)
            .assists(request.getAssists())
            .clearances(request.getClearances())
            .crosses(request.getCrosses())
            .dribblesAttempted(request.getDribblesAttempted())
            .dribblesSucceeded(request.getDribblesSucceeded())
            .foulsCommitted(request.getFoulsCommitted())
            .foulsDrawn(request.getFoulsDrawn())
            .goals(request.getGoals())
            .goalsConceded(request.getGoalsConceded())
            .interceptions(request.getInterceptions())
            .keyPasses(request.getKeyPasses())
            .longBalls(request.getLongBalls())
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
        return matchStatisticsPlayer;
    }
}
