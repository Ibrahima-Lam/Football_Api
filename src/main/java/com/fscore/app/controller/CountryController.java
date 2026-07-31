package com.fscore.app.controller;

import com.fscore.app.dto.request.CountryRequest;
import com.fscore.app.dto.response.CountryResponse;
import com.fscore.app.dto.response.PageResponse;
import com.fscore.app.entity.Country;
import com.fscore.app.entity.*;
import com.fscore.app.service.CountryService;
import com.fscore.app.service.FilterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.net.URI;

@RestController
@RequestMapping("/api/countries")
public class CountryController {

    private final CountryService service;
    private final FilterService filterService;

    public CountryController(CountryService service, FilterService filterService) {
        this.service = service;
        this.filterService = filterService;
    }

    @GetMapping
    public ResponseEntity<PageResponse> findAll(Pageable pageable, @RequestParam Map<String, String> params) {
        Page<Country> page = filterService.find("countryRepository", pageable, params);
        return ResponseEntity.ok(PageResponse.fromPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CountryResponse> findById(@PathVariable String id) {
        Country entity = service.findById(id)
            .orElseThrow(() -> new com.fscore.app.exception.ResourceNotFoundException("Country not found with id: " + id));
        return ResponseEntity.ok(mapToResponse(entity));
    }

    @PostMapping
    public ResponseEntity<CountryResponse> save(@Valid @RequestBody CountryRequest request) {
        Country entity = mapToEntity(request);
        Country saved = service.save(entity);
        return ResponseEntity.created(URI.create("/api/countries/" + saved.getId()))
            .body(mapToResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CountryResponse> update(@PathVariable String id, @Valid @RequestBody CountryRequest request) {
        Country entity = mapToEntity(request);
        Country updated = service.update(id, entity);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private CountryResponse mapToResponse(Country entity) {
        CountryResponse response = CountryResponse.builder()
            .continentId(entity.getContinent() != null ? entity.getContinent().getId() : null)
            .fifaCode(entity.getFifaCode())
            .flagUrl(entity.getFlagUrl())
            .id(entity.getId())
            .iso2(entity.getIso2())
            .iso3(entity.getIso3())
            .name(entity.getName())
            .officialName(entity.getOfficialName())
            .build();
        return response;
    }

    private Country mapToEntity(CountryRequest request) {
        Country country = Country.builder()
            .continent(request.getContinentId() != null ? Continent.builder().id(request.getContinentId()).build() : null)
            .fifaCode(request.getFifaCode())
            .flagUrl(request.getFlagUrl())
            .iso2(request.getIso2())
            .iso3(request.getIso3())
            .name(request.getName())
            .officialName(request.getOfficialName())
            .build();
        return country;
    }
}
