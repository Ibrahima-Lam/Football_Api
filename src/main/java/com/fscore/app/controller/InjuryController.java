package com.fscore.app.controller;

import com.fscore.app.dto.request.InjuryRequest;
import com.fscore.app.dto.response.InjuryResponse;
import com.fscore.app.dto.response.PageResponse;
import com.fscore.app.entity.Injury;
import com.fscore.app.entity.*;
import com.fscore.app.service.InjuryService;
import com.fscore.app.service.FilterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.net.URI;

@RestController
@RequestMapping("/api/injurys")
public class InjuryController {

    private final InjuryService service;
    private final FilterService filterService;

    public InjuryController(InjuryService service, FilterService filterService) {
        this.service = service;
        this.filterService = filterService;
    }

    @GetMapping
    public ResponseEntity<PageResponse> findAll(Pageable pageable, @RequestParam Map<String, String> params) {
        Page<Injury> page = filterService.find("injuryRepository", pageable, params);
        return ResponseEntity.ok(PageResponse.fromPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InjuryResponse> findById(@PathVariable String id) {
        Injury entity = service.findById(id)
            .orElseThrow(() -> new com.fscore.app.exception.ResourceNotFoundException("Injury not found with id: " + id));
        return ResponseEntity.ok(mapToResponse(entity));
    }

    @PostMapping
    public ResponseEntity<InjuryResponse> save(@Valid @RequestBody InjuryRequest request) {
        Injury entity = mapToEntity(request);
        Injury saved = service.save(entity);
        return ResponseEntity.created(URI.create("/api/injurys/" + saved.getId()))
            .body(mapToResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InjuryResponse> update(@PathVariable String id, @Valid @RequestBody InjuryRequest request) {
        Injury entity = mapToEntity(request);
        Injury updated = service.update(id, entity);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private InjuryResponse mapToResponse(Injury entity) {
        InjuryResponse response = InjuryResponse.builder()
            .bodyPart(entity.getBodyPart())
            .id(entity.getId())
            .injuryType(entity.getInjuryType())
            .matchId(entity.getMatch() != null ? entity.getMatch().getId() : null)
            .playerId(entity.getPlayer() != null ? entity.getPlayer().getId() : null)
            .severity(entity.getSeverity())
            .status(entity.getStatus())
            .teamId(entity.getTeam() != null ? entity.getTeam().getId() : null)
            .build();
        return response;
    }

    private Injury mapToEntity(InjuryRequest request) {
        Injury injury = Injury.builder()
            .match(request.getMatchId() != null ? Match.builder().id(request.getMatchId()).build() : null)
            .player(request.getPlayerId() != null ? Player.builder().id(request.getPlayerId()).build() : null)
            .team(request.getTeamId() != null ? Team.builder().id(request.getTeamId()).build() : null)
            .bodyPart(request.getBodyPart())
            .injuryType(request.getInjuryType())
            .severity(request.getSeverity())
            .status(request.getStatus())
            .build();
        return injury;
    }
}
