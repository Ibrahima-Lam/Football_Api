package com.fscore.app.controller;

import com.fscore.app.dto.request.PlayerAwardRequest;
import com.fscore.app.dto.response.PlayerAwardResponse;
import com.fscore.app.dto.response.PageResponse;
import com.fscore.app.entity.PlayerAward;
import com.fscore.app.entity.*;
import com.fscore.app.service.PlayerAwardService;
import com.fscore.app.service.FilterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.net.URI;

@RestController
@RequestMapping("/api/playerawards")
public class PlayerAwardController {

    private final PlayerAwardService service;
    private final FilterService filterService;

    public PlayerAwardController(PlayerAwardService service, FilterService filterService) {
        this.service = service;
        this.filterService = filterService;
    }

    @GetMapping
    public ResponseEntity<PageResponse> findAll(Pageable pageable, @RequestParam Map<String, String> params) {
        Page<PlayerAward> page = filterService.find("playerAwardRepository", pageable, params);
        return ResponseEntity.ok(PageResponse.fromPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerAwardResponse> findById(@PathVariable String id) {
        PlayerAward entity = service.findById(id)
            .orElseThrow(() -> new com.fscore.app.exception.ResourceNotFoundException("PlayerAward not found with id: " + id));
        return ResponseEntity.ok(mapToResponse(entity));
    }

    @PostMapping
    public ResponseEntity<PlayerAwardResponse> save(@Valid @RequestBody PlayerAwardRequest request) {
        PlayerAward entity = mapToEntity(request);
        PlayerAward saved = service.save(entity);
        return ResponseEntity.created(URI.create("/api/playerawards/" + saved.getId()))
            .body(mapToResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlayerAwardResponse> update(@PathVariable String id, @Valid @RequestBody PlayerAwardRequest request) {
        PlayerAward entity = mapToEntity(request);
        PlayerAward updated = service.update(id, entity);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private PlayerAwardResponse mapToResponse(PlayerAward entity) {
        PlayerAwardResponse response = PlayerAwardResponse.builder()
            .id(entity.getId())
            .playerId(entity.getPlayer() != null ? entity.getPlayer().getId() : null)
            .seasonId(entity.getSeason() != null ? entity.getSeason().getId() : null)
            .teamId(entity.getTeam() != null ? entity.getTeam().getId() : null)
            .trophyId(entity.getTrophy() != null ? entity.getTrophy().getId() : null)
            .build();
        return response;
    }

    private PlayerAward mapToEntity(PlayerAwardRequest request) {
        PlayerAward playerAward = PlayerAward.builder()
            .player(request.getPlayerId() != null ? Player.builder().id(request.getPlayerId()).build() : null)
            .season(request.getSeasonId() != null ? Season.builder().id(request.getSeasonId()).build() : null)
            .team(request.getTeamId() != null ? Team.builder().id(request.getTeamId()).build() : null)
            .trophy(request.getTrophyId() != null ? Trophy.builder().id(request.getTrophyId()).build() : null)
            .build();
        return playerAward;
    }
}
