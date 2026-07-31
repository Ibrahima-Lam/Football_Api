package com.fscore.app.controller;

import com.fscore.app.dto.request.ContinentRequest;
import com.fscore.app.dto.response.ContinentResponse;
import com.fscore.app.dto.response.PageResponse;
import com.fscore.app.entity.Continent;
import com.fscore.app.entity.*;
import com.fscore.app.service.ContinentService;
import com.fscore.app.service.FilterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.net.URI;

@RestController
@RequestMapping("/api/continents")
public class ContinentController {

    private final ContinentService service;
    private final FilterService filterService;

    public ContinentController(ContinentService service, FilterService filterService) {
        this.service = service;
        this.filterService = filterService;
    }

    @GetMapping
    public ResponseEntity<PageResponse> findAll(Pageable pageable, @RequestParam Map<String, String> params) {
        Page<Continent> page = filterService.find("continentRepository", pageable, params);
        return ResponseEntity.ok(PageResponse.fromPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContinentResponse> findById(@PathVariable String id) {
        Continent entity = service.findById(id)
            .orElseThrow(() -> new com.fscore.app.exception.ResourceNotFoundException("Continent not found with id: " + id));
        return ResponseEntity.ok(mapToResponse(entity));
    }

    @PostMapping
    public ResponseEntity<ContinentResponse> save(@Valid @RequestBody ContinentRequest request) {
        Continent entity = mapToEntity(request);
        Continent saved = service.save(entity);
        return ResponseEntity.created(URI.create("/api/continents/" + saved.getId()))
            .body(mapToResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContinentResponse> update(@PathVariable String id, @Valid @RequestBody ContinentRequest request) {
        Continent entity = mapToEntity(request);
        Continent updated = service.update(id, entity);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private ContinentResponse mapToResponse(Continent entity) {
        ContinentResponse response = ContinentResponse.builder()
            .code(entity.getCode())
            .id(entity.getId())
            .name(entity.getName())
            .build();
        return response;
    }

    private Continent mapToEntity(ContinentRequest request) {
        Continent continent = Continent.builder()
            .code(request.getCode())
            .name(request.getName())
            .build();
        return continent;
    }
}
