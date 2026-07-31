package com.fscore.app.controller;

import com.fscore.app.dto.request.StadiumRequest;
import com.fscore.app.dto.response.StadiumResponse;
import com.fscore.app.dto.response.PageResponse;
import com.fscore.app.entity.Stadium;
import com.fscore.app.entity.*;
import com.fscore.app.service.StadiumService;
import com.fscore.app.service.FilterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.net.URI;

@RestController
@RequestMapping("/api/stadiums")
public class StadiumController {

    private final StadiumService service;
    private final FilterService filterService;

    public StadiumController(StadiumService service, FilterService filterService) {
        this.service = service;
        this.filterService = filterService;
    }

    @GetMapping
    public ResponseEntity<PageResponse> findAll(Pageable pageable, @RequestParam Map<String, String> params) {
        Page<Stadium> page = filterService.find("stadiumRepository", pageable, params);
        return ResponseEntity.ok(PageResponse.fromPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StadiumResponse> findById(@PathVariable String id) {
        Stadium entity = service.findById(id)
            .orElseThrow(() -> new com.fscore.app.exception.ResourceNotFoundException("Stadium not found with id: " + id));
        return ResponseEntity.ok(mapToResponse(entity));
    }

    @PostMapping
    public ResponseEntity<StadiumResponse> save(@Valid @RequestBody StadiumRequest request) {
        Stadium entity = mapToEntity(request);
        Stadium saved = service.save(entity);
        return ResponseEntity.created(URI.create("/api/stadiums/" + saved.getId()))
            .body(mapToResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StadiumResponse> update(@PathVariable String id, @Valid @RequestBody StadiumRequest request) {
        Stadium entity = mapToEntity(request);
        Stadium updated = service.update(id, entity);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private StadiumResponse mapToResponse(Stadium entity) {
        StadiumResponse response = StadiumResponse.builder()
            .address(entity.getAddress())
            .capacity(entity.getCapacity())
            .cityId(entity.getCity() != null ? entity.getCity().getId() : null)
            .countryId(entity.getCountry() != null ? entity.getCountry().getId() : null)
            .id(entity.getId())
            .image(entity.getImage())
            .name(entity.getName())
            .opened(entity.getOpened())
            .surface(entity.getSurface())
            .build();
        return response;
    }

    private Stadium mapToEntity(StadiumRequest request) {
        Stadium stadium = Stadium.builder()
            .city(request.getCityId() != null ? City.builder().id(request.getCityId()).build() : null)
            .country(request.getCountryId() != null ? Country.builder().id(request.getCountryId()).build() : null)
            .address(request.getAddress())
            .capacity(request.getCapacity())
            .image(request.getImage())
            .name(request.getName())
            .opened(request.getOpened())
            .surface(request.getSurface())
            .build();
        return stadium;
    }
}
