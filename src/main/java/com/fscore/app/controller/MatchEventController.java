package com.fscore.app.controller;

import com.fscore.app.dto.request.MatchEventRequest;
import com.fscore.app.dto.response.MatchEventResponse;
import com.fscore.app.dto.response.PageResponse;
import com.fscore.app.entity.MatchEvent;
import com.fscore.app.entity.*;
import com.fscore.app.service.MatchEventService;
import com.fscore.app.service.FilterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.net.URI;

@RestController
@RequestMapping("/api/matchevents")
public class MatchEventController {

    private final MatchEventService service;
    private final FilterService filterService;

    public MatchEventController(MatchEventService service, FilterService filterService) {
        this.service = service;
        this.filterService = filterService;
    }

    @GetMapping
    public ResponseEntity<PageResponse> findAll(Pageable pageable, @RequestParam Map<String, String> params) {
        Page<MatchEvent> page = filterService.find("matchEventRepository", pageable, params);
        return ResponseEntity.ok(PageResponse.fromPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatchEventResponse> findById(@PathVariable String id) {
        MatchEvent entity = service.findById(id)
            .orElseThrow(() -> new com.fscore.app.exception.ResourceNotFoundException("MatchEvent not found with id: " + id));
        return ResponseEntity.ok(mapToResponse(entity));
    }

    @PostMapping
    public ResponseEntity<MatchEventResponse> save(@Valid @RequestBody MatchEventRequest request) {
        MatchEvent entity = mapToEntity(request);
        MatchEvent saved = service.save(entity);
        return ResponseEntity.created(URI.create("/api/matchevents/" + saved.getId()))
            .body(mapToResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MatchEventResponse> update(@PathVariable String id, @Valid @RequestBody MatchEventRequest request) {
        MatchEvent entity = mapToEntity(request);
        MatchEvent updated = service.update(id, entity);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private MatchEventResponse mapToResponse(MatchEvent entity) {
        MatchEventResponse response = MatchEventResponse.builder()
            .comments(entity.getComments())
            .detail(entity.getDetail())
            .eventType(entity.getEventType())
            .extraMinute(entity.getExtraMinute())
            .id(entity.getId())
            .matchId(entity.getMatch() != null ? entity.getMatch().getId() : null)
            .minute(entity.getMinute())
            .period(entity.getPeriod())
            .playerId(entity.getPlayer() != null ? entity.getPlayer().getId() : null)
            .relatedPlayerId(entity.getRelatedPlayer() != null ? entity.getRelatedPlayer().getId() : null)
            .teamId(entity.getTeam() != null ? entity.getTeam().getId() : null)
            .varReviewed(entity.getVarReviewed())
            .build();
        return response;
    }

    private MatchEvent mapToEntity(MatchEventRequest request) {
        MatchEvent matchEvent = MatchEvent.builder()
            .match(request.getMatchId() != null ? Match.builder().id(request.getMatchId()).build() : null)
            .player(request.getPlayerId() != null ? Player.builder().id(request.getPlayerId()).build() : null)
            .relatedPlayer(request.getRelatedPlayerId() != null ? Player.builder().id(request.getRelatedPlayerId()).build() : null)
            .team(request.getTeamId() != null ? Team.builder().id(request.getTeamId()).build() : null)
            .comments(request.getComments())
            .detail(request.getDetail())
            .eventType(request.getEventType())
            .extraMinute(request.getExtraMinute())
            .minute(request.getMinute())
            .period(request.getPeriod())
            .varReviewed(request.getVarReviewed())
            .build();
        return matchEvent;
    }
}
