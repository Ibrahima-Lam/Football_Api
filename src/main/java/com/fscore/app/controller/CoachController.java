package com.fscore.app.controller;

import com.fscore.app.dto.request.CoachRequest;
import com.fscore.app.dto.response.CoachResponse;
import com.fscore.app.dto.response.PageResponse;
import com.fscore.app.entity.Coach;
import com.fscore.app.entity.*;
import com.fscore.app.service.CoachService;
import com.fscore.app.service.FilterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.net.URI;

@RestController
@RequestMapping("/api/coaches")
public class CoachController {

    private final CoachService service;
    private final FilterService filterService;

    public CoachController(CoachService service, FilterService filterService) {
        this.service = service;
        this.filterService = filterService;
    }

    @GetMapping
    public ResponseEntity<PageResponse> findAll(Pageable pageable, @RequestParam Map<String, String> params) {
        Page<Coach> page = filterService.find("coachRepository", pageable, params);
        return ResponseEntity.ok(PageResponse.fromPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CoachResponse> findById(@PathVariable String id) {
        Coach entity = service.findById(id)
            .orElseThrow(() -> new com.fscore.app.exception.ResourceNotFoundException("Coach not found with id: " + id));
        return ResponseEntity.ok(mapToResponse(entity));
    }

    @PostMapping
    public ResponseEntity<CoachResponse> save(@Valid @RequestBody CoachRequest request) {
        Coach entity = mapToEntity(request);
        Coach saved = service.save(entity);
        return ResponseEntity.created(URI.create("/api/coaches/" + saved.getId()))
            .body(mapToResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CoachResponse> update(@PathVariable String id, @Valid @RequestBody CoachRequest request) {
        Coach entity = mapToEntity(request);
        Coach updated = service.update(id, entity);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private CoachResponse mapToResponse(Coach entity) {
        CoachResponse response = CoachResponse.builder()
            .active(entity.getActive())
            .countryId(entity.getCountry() != null ? entity.getCountry().getId() : null)
            .firstName(entity.getFirstName())
            .fullName(entity.getFullName())
            .id(entity.getId())
            .lastName(entity.getLastName())
            .photo(entity.getPhoto())
            .role(entity.getRole())
            .build();
        return response;
    }

    private Coach mapToEntity(CoachRequest request) {
        Coach coach = Coach.builder()
            .country(request.getCountryId() != null ? Country.builder().id(request.getCountryId()).build() : null)
            .active(request.getActive())
            .firstName(request.getFirstName())
            .fullName(request.getFullName())
            .lastName(request.getLastName())
            .photo(request.getPhoto())
            .role(request.getRole())
            .build();
        return coach;
    }
}
