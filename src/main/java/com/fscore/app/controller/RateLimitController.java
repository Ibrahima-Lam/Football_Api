package com.fscore.app.controller;

import com.fscore.app.dto.request.RateLimitRequest;
import com.fscore.app.dto.response.RateLimitResponse;
import com.fscore.app.dto.response.PageResponse;
import com.fscore.app.entity.RateLimit;
import com.fscore.app.entity.*;
import com.fscore.app.service.RateLimitService;
import com.fscore.app.service.FilterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.net.URI;

@RestController
@RequestMapping("/api/ratelimits")
public class RateLimitController {

    private final RateLimitService service;
    private final FilterService filterService;

    public RateLimitController(RateLimitService service, FilterService filterService) {
        this.service = service;
        this.filterService = filterService;
    }

    @GetMapping
    public ResponseEntity<PageResponse> findAll(Pageable pageable, @RequestParam Map<String, String> params) {
        Page<RateLimit> page = filterService.find("rateLimitRepository", pageable, params);
        return ResponseEntity.ok(PageResponse.fromPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RateLimitResponse> findById(@PathVariable String id) {
        RateLimit entity = service.findById(id)
            .orElseThrow(() -> new com.fscore.app.exception.ResourceNotFoundException("RateLimit not found with id: " + id));
        return ResponseEntity.ok(mapToResponse(entity));
    }

    @PostMapping
    public ResponseEntity<RateLimitResponse> save(@Valid @RequestBody RateLimitRequest request) {
        RateLimit entity = mapToEntity(request);
        RateLimit saved = service.save(entity);
        return ResponseEntity.created(URI.create("/api/ratelimits/" + saved.getId()))
            .body(mapToResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RateLimitResponse> update(@PathVariable String id, @Valid @RequestBody RateLimitRequest request) {
        RateLimit entity = mapToEntity(request);
        RateLimit updated = service.update(id, entity);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private RateLimitResponse mapToResponse(RateLimit entity) {
        RateLimitResponse response = RateLimitResponse.builder()
            .apiKeyId(entity.getApiKey() != null ? entity.getApiKey().getId() : null)
            .id(entity.getId())
            .requestsPerDay(entity.getRequestsPerDay())
            .requestsPerMinute(entity.getRequestsPerMinute())
            .requestsPerMonth(entity.getRequestsPerMonth())
            .build();
        return response;
    }

    private RateLimit mapToEntity(RateLimitRequest request) {
        RateLimit rateLimit = RateLimit.builder()
            .apiKey(request.getApiKeyId() != null ? ApiKey.builder().id(request.getApiKeyId()).build() : null)
            .requestsPerDay(request.getRequestsPerDay())
            .requestsPerMinute(request.getRequestsPerMinute())
            .requestsPerMonth(request.getRequestsPerMonth())
            .build();
        return rateLimit;
    }
}
