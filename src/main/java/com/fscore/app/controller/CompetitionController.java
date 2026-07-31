package com.fscore.app.controller;

import com.fscore.app.dto.request.CompetitionRequest;
import com.fscore.app.dto.response.CompetitionResponse;
import com.fscore.app.dto.response.PageResponse;
import com.fscore.app.entity.Competition;
import com.fscore.app.entity.*;
import com.fscore.app.service.CompetitionService;
import com.fscore.app.service.FilterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.net.URI;

@RestController
@RequestMapping("/api/competitions")
public class CompetitionController {

    private final CompetitionService service;
    private final FilterService filterService;

    public CompetitionController(CompetitionService service, FilterService filterService) {
        this.service = service;
        this.filterService = filterService;
    }

    @GetMapping
    public ResponseEntity<PageResponse> findAll(Pageable pageable, @RequestParam Map<String, String> params) {
        Page<Competition> page = filterService.find("competitionRepository", pageable, params);
        return ResponseEntity.ok(PageResponse.fromPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompetitionResponse> findById(@PathVariable String id) {
        Competition entity = service.findById(id)
            .orElseThrow(() -> new com.fscore.app.exception.ResourceNotFoundException("Competition not found with id: " + id));
        return ResponseEntity.ok(mapToResponse(entity));
    }

    @PostMapping
    public ResponseEntity<CompetitionResponse> save(@Valid @RequestBody CompetitionRequest request) {
        Competition entity = mapToEntity(request);
        Competition saved = service.save(entity);
        return ResponseEntity.created(URI.create("/api/competitions/" + saved.getId()))
            .body(mapToResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompetitionResponse> update(@PathVariable String id, @Valid @RequestBody CompetitionRequest request) {
        Competition entity = mapToEntity(request);
        Competition updated = service.update(id, entity);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private CompetitionResponse mapToResponse(Competition entity) {
        CompetitionResponse response = CompetitionResponse.builder()
            .active(entity.getActive())
            .ageLevel(entity.getAgeLevel())
            .confederationId(entity.getConfederation() != null ? entity.getConfederation().getId() : null)
            .countryId(entity.getCountry() != null ? entity.getCountry().getId() : null)
            .founded(entity.getFounded())
            .gender(entity.getGender())
            .id(entity.getId())
            .level(entity.getLevel())
            .logo(entity.getLogo())
            .name(entity.getName())
            .shortName(entity.getShortName())
            .sport(entity.getSport())
            .type(entity.getType())
            .website(entity.getWebsite())
            .build();
        return response;
    }

    private Competition mapToEntity(CompetitionRequest request) {
        Competition competition = Competition.builder()
            .confederation(request.getConfederationId() != null ? Confederation.builder().id(request.getConfederationId()).build() : null)
            .country(request.getCountryId() != null ? Country.builder().id(request.getCountryId()).build() : null)
            .active(request.getActive())
            .ageLevel(request.getAgeLevel())
            .founded(request.getFounded())
            .gender(request.getGender())
            .level(request.getLevel())
            .logo(request.getLogo())
            .name(request.getName())
            .shortName(request.getShortName())
            .sport(request.getSport())
            .type(request.getType())
            .website(request.getWebsite())
            .build();
        return competition;
    }
}
