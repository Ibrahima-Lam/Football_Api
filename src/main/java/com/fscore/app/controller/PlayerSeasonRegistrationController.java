package com.fscore.app.controller;

import com.fscore.app.dto.request.PlayerSeasonRegistrationRequest;
import com.fscore.app.dto.response.PlayerSeasonRegistrationResponse;
import com.fscore.app.dto.response.PageResponse;
import com.fscore.app.entity.PlayerSeasonRegistration;
import com.fscore.app.entity.*;
import com.fscore.app.service.PlayerSeasonRegistrationService;
import com.fscore.app.service.FilterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.net.URI;

@RestController
@RequestMapping("/api/playerseasonregistrations")
public class PlayerSeasonRegistrationController {

    private final PlayerSeasonRegistrationService service;
    private final FilterService filterService;

    public PlayerSeasonRegistrationController(PlayerSeasonRegistrationService service, FilterService filterService) {
        this.service = service;
        this.filterService = filterService;
    }

    @GetMapping
    public ResponseEntity<PageResponse> findAll(Pageable pageable, @RequestParam Map<String, String> params) {
        Page<PlayerSeasonRegistration> page = filterService.find("playerSeasonRegistrationRepository", pageable, params);
        return ResponseEntity.ok(PageResponse.fromPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerSeasonRegistrationResponse> findById(@PathVariable String id) {
        PlayerSeasonRegistration entity = service.findById(id)
            .orElseThrow(() -> new com.fscore.app.exception.ResourceNotFoundException("PlayerSeasonRegistration not found with id: " + id));
        return ResponseEntity.ok(mapToResponse(entity));
    }

    @PostMapping
    public ResponseEntity<PlayerSeasonRegistrationResponse> save(@Valid @RequestBody PlayerSeasonRegistrationRequest request) {
        PlayerSeasonRegistration entity = mapToEntity(request);
        PlayerSeasonRegistration saved = service.save(entity);
        return ResponseEntity.created(URI.create("/api/playerseasonregistrations/" + saved.getId()))
            .body(mapToResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlayerSeasonRegistrationResponse> update(@PathVariable String id, @Valid @RequestBody PlayerSeasonRegistrationRequest request) {
        PlayerSeasonRegistration entity = mapToEntity(request);
        PlayerSeasonRegistration updated = service.update(id, entity);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private PlayerSeasonRegistrationResponse mapToResponse(PlayerSeasonRegistration entity) {
        PlayerSeasonRegistrationResponse response = PlayerSeasonRegistrationResponse.builder()
            .captain(entity.getCaptain())
            .competitionId(entity.getCompetition() != null ? entity.getCompetition().getId() : null)
            .id(entity.getId())
            .playerId(entity.getPlayer() != null ? entity.getPlayer().getId() : null)
            .position(entity.getPosition())
            .seasonId(entity.getSeason() != null ? entity.getSeason().getId() : null)
            .shirtNumber(entity.getShirtNumber())
            .status(entity.getStatus())
            .teamId(entity.getTeam() != null ? entity.getTeam().getId() : null)
            .build();
        return response;
    }

    private PlayerSeasonRegistration mapToEntity(PlayerSeasonRegistrationRequest request) {
        PlayerSeasonRegistration playerSeasonRegistration = PlayerSeasonRegistration.builder()
            .competition(request.getCompetitionId() != null ? Competition.builder().id(request.getCompetitionId()).build() : null)
            .player(request.getPlayerId() != null ? Player.builder().id(request.getPlayerId()).build() : null)
            .season(request.getSeasonId() != null ? Season.builder().id(request.getSeasonId()).build() : null)
            .team(request.getTeamId() != null ? Team.builder().id(request.getTeamId()).build() : null)
            .captain(request.getCaptain())
            .position(request.getPosition())
            .shirtNumber(request.getShirtNumber())
            .status(request.getStatus())
            .build();
        return playerSeasonRegistration;
    }
}
