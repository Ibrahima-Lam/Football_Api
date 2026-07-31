package com.fscore.app.controller;

import com.fscore.app.dto.request.CityRequest;
import com.fscore.app.dto.response.CityResponse;
import com.fscore.app.dto.response.PageResponse;
import com.fscore.app.entity.City;
import com.fscore.app.entity.*;
import com.fscore.app.service.CityService;
import com.fscore.app.service.FilterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.net.URI;

@RestController
@RequestMapping("/api/cities")
public class CityController {

    private final CityService service;
    private final FilterService filterService;

    public CityController(CityService service, FilterService filterService) {
        this.service = service;
        this.filterService = filterService;
    }

    @GetMapping
    public ResponseEntity<PageResponse> findAll(Pageable pageable, @RequestParam Map<String, String> params) {
        Page<City> page = filterService.find("cityRepository", pageable, params);
        return ResponseEntity.ok(PageResponse.fromPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CityResponse> findById(@PathVariable String id) {
        City entity = service.findById(id)
            .orElseThrow(() -> new com.fscore.app.exception.ResourceNotFoundException("City not found with id: " + id));
        return ResponseEntity.ok(mapToResponse(entity));
    }

    @PostMapping
    public ResponseEntity<CityResponse> save(@Valid @RequestBody CityRequest request) {
        City entity = mapToEntity(request);
        City saved = service.save(entity);
        return ResponseEntity.created(URI.create("/api/cities/" + saved.getId()))
            .body(mapToResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CityResponse> update(@PathVariable String id, @Valid @RequestBody CityRequest request) {
        City entity = mapToEntity(request);
        City updated = service.update(id, entity);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private CityResponse mapToResponse(City entity) {
        CityResponse response = CityResponse.builder()
            .countryId(entity.getCountry() != null ? entity.getCountry().getId() : null)
            .id(entity.getId())
            .name(entity.getName())
            .timezone(entity.getTimezone())
            .build();
        return response;
    }

    private City mapToEntity(CityRequest request) {
        City city = City.builder()
            .country(request.getCountryId() != null ? Country.builder().id(request.getCountryId()).build() : null)
            .name(request.getName())
            .timezone(request.getTimezone())
            .build();
        return city;
    }
}
