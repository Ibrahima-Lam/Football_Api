package com.fscore.app.controller;

import com.fscore.app.dto.request.MatchPenaltyShootoutShotRequest;
import com.fscore.app.dto.response.MatchPenaltyShootoutShotResponse;
import com.fscore.app.dto.response.PageResponse;
import com.fscore.app.entity.MatchPenaltyShootoutShot;
import com.fscore.app.entity.*;
import com.fscore.app.service.MatchPenaltyShootoutShotService;
import com.fscore.app.service.FilterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.net.URI;

@RestController
@RequestMapping("/api/matchpenaltyshootoutshots")
public class MatchPenaltyShootoutShotController {

    private final MatchPenaltyShootoutShotService service;
    private final FilterService filterService;

    public MatchPenaltyShootoutShotController(MatchPenaltyShootoutShotService service, FilterService filterService) {
        this.service = service;
        this.filterService = filterService;
    }

    @GetMapping
    public ResponseEntity<PageResponse> findAll(Pageable pageable, @RequestParam Map<String, String> params) {
        Page<MatchPenaltyShootoutShot> page = filterService.find("matchPenaltyShootoutShotRepository", pageable, params);
        return ResponseEntity.ok(PageResponse.fromPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatchPenaltyShootoutShotResponse> findById(@PathVariable String id) {
        MatchPenaltyShootoutShot entity = service.findById(id)
            .orElseThrow(() -> new com.fscore.app.exception.ResourceNotFoundException("MatchPenaltyShootoutShot not found with id: " + id));
        return ResponseEntity.ok(mapToResponse(entity));
    }

    @PostMapping
    public ResponseEntity<MatchPenaltyShootoutShotResponse> save(@Valid @RequestBody MatchPenaltyShootoutShotRequest request) {
        MatchPenaltyShootoutShot entity = mapToEntity(request);
        MatchPenaltyShootoutShot saved = service.save(entity);
        return ResponseEntity.created(URI.create("/api/matchpenaltyshootoutshots/" + saved.getId()))
            .body(mapToResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MatchPenaltyShootoutShotResponse> update(@PathVariable String id, @Valid @RequestBody MatchPenaltyShootoutShotRequest request) {
        MatchPenaltyShootoutShot entity = mapToEntity(request);
        MatchPenaltyShootoutShot updated = service.update(id, entity);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private MatchPenaltyShootoutShotResponse mapToResponse(MatchPenaltyShootoutShot entity) {
        MatchPenaltyShootoutShotResponse response = MatchPenaltyShootoutShotResponse.builder()
            .goalkeeperId(entity.getGoalkeeper() != null ? entity.getGoalkeeper().getId() : null)
            .id(entity.getId())
            .matchId(entity.getMatch() != null ? entity.getMatch().getId() : null)
            .playerId(entity.getPlayer() != null ? entity.getPlayer().getId() : null)
            .round(entity.getRound())
            .shotOrder(entity.getShotOrder())
            .status(entity.getStatus())
            .teamId(entity.getTeam() != null ? entity.getTeam().getId() : null)
            .build();
        return response;
    }

    private MatchPenaltyShootoutShot mapToEntity(MatchPenaltyShootoutShotRequest request) {
        MatchPenaltyShootoutShot matchPenaltyShootoutShot = MatchPenaltyShootoutShot.builder()
            .goalkeeper(request.getGoalkeeperId() != null ? Player.builder().id(request.getGoalkeeperId()).build() : null)
            .match(request.getMatchId() != null ? Match.builder().id(request.getMatchId()).build() : null)
            .player(request.getPlayerId() != null ? Player.builder().id(request.getPlayerId()).build() : null)
            .team(request.getTeamId() != null ? Team.builder().id(request.getTeamId()).build() : null)
            .round(request.getRound())
            .shotOrder(request.getShotOrder())
            .status(request.getStatus())
            .build();
        return matchPenaltyShootoutShot;
    }
}
