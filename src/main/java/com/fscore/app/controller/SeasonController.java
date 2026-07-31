package com.fscore.app.controller;

import com.fscore.app.dto.request.SeasonRequest;
import com.fscore.app.dto.response.SeasonResponse;
import com.fscore.app.dto.response.PageResponse;
import com.fscore.app.entity.Season;
import com.fscore.app.entity.*;
import com.fscore.app.service.SeasonService;
import com.fscore.app.service.FilterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.net.URI;

@RestController
@RequestMapping("/api/seasons")
public class SeasonController {

    private final SeasonService service;
    private final FilterService filterService;

    public SeasonController(SeasonService service, FilterService filterService) {
        this.service = service;
        this.filterService = filterService;
    }

    @GetMapping
    public ResponseEntity<PageResponse> findAll(Pageable pageable, @RequestParam Map<String, String> params) {
        Page<Season> page = filterService.find("seasonRepository", pageable, params);
        return ResponseEntity.ok(PageResponse.fromPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeasonResponse> findById(@PathVariable String id) {
        Season entity = service.findById(id)
            .orElseThrow(() -> new com.fscore.app.exception.ResourceNotFoundException("Season not found with id: " + id));
        return ResponseEntity.ok(mapToResponse(entity));
    }

    @PostMapping
    public ResponseEntity<SeasonResponse> save(@Valid @RequestBody SeasonRequest request) {
        Season entity = mapToEntity(request);
        Season saved = service.save(entity);
        return ResponseEntity.created(URI.create("/api/seasons/" + saved.getId()))
            .body(mapToResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SeasonResponse> update(@PathVariable String id, @Valid @RequestBody SeasonRequest request) {
        Season entity = mapToEntity(request);
        Season updated = service.update(id, entity);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private SeasonResponse mapToResponse(Season entity) {
        SeasonResponse response = SeasonResponse.builder()
            .competitionId(entity.getCompetition() != null ? entity.getCompetition().getId() : null)
            .current(entity.getCurrent())
            .id(entity.getId())
            .name(entity.getName())
            .status(entity.getStatus())
            .yearEnd(entity.getYearEnd())
            .yearStart(entity.getYearStart())
            .startDate(entity.getStartDate())
            .endDate(entity.getEndDate())
            .build();
        return response;
    }

    private Season mapToEntity(SeasonRequest request) {
        Season season = Season.builder()
            .competition(request.getCompetitionId() != null ? Competition.builder().id(request.getCompetitionId()).build() : null)
            .current(request.getCurrent())
            .name(request.getName())
            .status(request.getStatus())
            .yearEnd(request.getYearEnd())
            .yearStart(request.getYearStart())
            .startDate(request.getStartDate())
            .endDate(request.getEndDate())
            .build();
        return season;
    }
}
