package com.fscore.app.controller;

import com.fscore.app.dto.request.MatchRefereeRequest;
import com.fscore.app.dto.response.MatchRefereeResponse;
import com.fscore.app.dto.response.PageResponse;
import com.fscore.app.entity.MatchReferee;
import com.fscore.app.entity.*;
import com.fscore.app.service.MatchRefereeService;
import com.fscore.app.service.FilterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.net.URI;

@RestController
@RequestMapping("/api/matchreferees")
public class MatchRefereeController {

    private final MatchRefereeService service;
    private final FilterService filterService;

    public MatchRefereeController(MatchRefereeService service, FilterService filterService) {
        this.service = service;
        this.filterService = filterService;
    }

    @GetMapping
    public ResponseEntity<PageResponse> findAll(Pageable pageable, @RequestParam Map<String, String> params) {
        Page<MatchReferee> page = filterService.find("matchRefereeRepository", pageable, params);
        return ResponseEntity.ok(PageResponse.fromPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatchRefereeResponse> findById(@PathVariable String id) {
        MatchReferee entity = service.findById(id)
            .orElseThrow(() -> new com.fscore.app.exception.ResourceNotFoundException("MatchReferee not found with id: " + id));
        return ResponseEntity.ok(mapToResponse(entity));
    }

    @PostMapping
    public ResponseEntity<MatchRefereeResponse> save(@Valid @RequestBody MatchRefereeRequest request) {
        MatchReferee entity = mapToEntity(request);
        MatchReferee saved = service.save(entity);
        return ResponseEntity.created(URI.create("/api/matchreferees/" + saved.getId()))
            .body(mapToResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MatchRefereeResponse> update(@PathVariable String id, @Valid @RequestBody MatchRefereeRequest request) {
        MatchReferee entity = mapToEntity(request);
        MatchReferee updated = service.update(id, entity);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private MatchRefereeResponse mapToResponse(MatchReferee entity) {
        MatchRefereeResponse response = MatchRefereeResponse.builder()
            .id(entity.getId())
            .matchId(entity.getMatch() != null ? entity.getMatch().getId() : null)
            .refereeId(entity.getReferee() != null ? entity.getReferee().getId() : null)
            .role(entity.getRole())
            .build();
        return response;
    }

    private MatchReferee mapToEntity(MatchRefereeRequest request) {
        MatchReferee matchReferee = MatchReferee.builder()
            .match(request.getMatchId() != null ? Match.builder().id(request.getMatchId()).build() : null)
            .referee(request.getRefereeId() != null ? Referee.builder().id(request.getRefereeId()).build() : null)
            .role(request.getRole())
            .build();
        return matchReferee;
    }
}
