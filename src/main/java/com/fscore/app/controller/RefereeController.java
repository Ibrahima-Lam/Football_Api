package com.fscore.app.controller;

import com.fscore.app.dto.request.RefereeRequest;
import com.fscore.app.dto.response.RefereeResponse;
import com.fscore.app.dto.response.PageResponse;
import com.fscore.app.entity.Referee;
import com.fscore.app.entity.*;
import com.fscore.app.service.RefereeService;
import com.fscore.app.service.FilterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.net.URI;

@RestController
@RequestMapping("/api/referees")
public class RefereeController {

    private final RefereeService service;
    private final FilterService filterService;

    public RefereeController(RefereeService service, FilterService filterService) {
        this.service = service;
        this.filterService = filterService;
    }

    @GetMapping
    public ResponseEntity<PageResponse> findAll(Pageable pageable, @RequestParam Map<String, String> params) {
        Page<Referee> page = filterService.find("refereeRepository", pageable, params);
        return ResponseEntity.ok(PageResponse.fromPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RefereeResponse> findById(@PathVariable String id) {
        Referee entity = service.findById(id)
            .orElseThrow(() -> new com.fscore.app.exception.ResourceNotFoundException("Referee not found with id: " + id));
        return ResponseEntity.ok(mapToResponse(entity));
    }

    @PostMapping
    public ResponseEntity<RefereeResponse> save(@Valid @RequestBody RefereeRequest request) {
        Referee entity = mapToEntity(request);
        Referee saved = service.save(entity);
        return ResponseEntity.created(URI.create("/api/referees/" + saved.getId()))
            .body(mapToResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RefereeResponse> update(@PathVariable String id, @Valid @RequestBody RefereeRequest request) {
        Referee entity = mapToEntity(request);
        Referee updated = service.update(id, entity);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private RefereeResponse mapToResponse(Referee entity) {
        RefereeResponse response = RefereeResponse.builder()
            .active(entity.getActive())
            .category(entity.getCategory())
            .countryId(entity.getCountry() != null ? entity.getCountry().getId() : null)
            .firstName(entity.getFirstName())
            .fullName(entity.getFullName())
            .id(entity.getId())
            .lastName(entity.getLastName())
            .photo(entity.getPhoto())
            .build();
        return response;
    }

    private Referee mapToEntity(RefereeRequest request) {
        Referee referee = Referee.builder()
            .country(request.getCountryId() != null ? Country.builder().id(request.getCountryId()).build() : null)
            .active(request.getActive())
            .category(request.getCategory())
            .firstName(request.getFirstName())
            .fullName(request.getFullName())
            .lastName(request.getLastName())
            .photo(request.getPhoto())
            .build();
        return referee;
    }
}
