package com.fscore.app.controller;

import com.fscore.app.dto.request.OddRequest;
import com.fscore.app.dto.response.OddResponse;
import com.fscore.app.dto.response.PageResponse;
import com.fscore.app.entity.Odd;
import com.fscore.app.entity.*;
import com.fscore.app.service.OddService;
import com.fscore.app.service.FilterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.net.URI;

@RestController
@RequestMapping("/api/odds")
public class OddController {

    private final OddService service;
    private final FilterService filterService;

    public OddController(OddService service, FilterService filterService) {
        this.service = service;
        this.filterService = filterService;
    }

    @GetMapping
    public ResponseEntity<PageResponse> findAll(Pageable pageable, @RequestParam Map<String, String> params) {
        Page<Odd> page = filterService.find("oddRepository", pageable, params);
        return ResponseEntity.ok(PageResponse.fromPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OddResponse> findById(@PathVariable String id) {
        Odd entity = service.findById(id)
            .orElseThrow(() -> new com.fscore.app.exception.ResourceNotFoundException("Odd not found with id: " + id));
        return ResponseEntity.ok(mapToResponse(entity));
    }

    @PostMapping
    public ResponseEntity<OddResponse> save(@Valid @RequestBody OddRequest request) {
        Odd entity = mapToEntity(request);
        Odd saved = service.save(entity);
        return ResponseEntity.created(URI.create("/api/odds/" + saved.getId()))
            .body(mapToResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OddResponse> update(@PathVariable String id, @Valid @RequestBody OddRequest request) {
        Odd entity = mapToEntity(request);
        Odd updated = service.update(id, entity);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private OddResponse mapToResponse(Odd entity) {
        OddResponse response = OddResponse.builder()
            .active(entity.getActive())
            .bookmakerId(entity.getBookmaker() != null ? entity.getBookmaker().getId() : null)
            .id(entity.getId())
            .market(entity.getMarket())
            .matchId(entity.getMatch() != null ? entity.getMatch().getId() : null)
            .selection(entity.getSelection())
            .build();
        return response;
    }

    private Odd mapToEntity(OddRequest request) {
        Odd odd = Odd.builder()
            .bookmaker(request.getBookmakerId() != null ? Bookmaker.builder().id(request.getBookmakerId()).build() : null)
            .match(request.getMatchId() != null ? Match.builder().id(request.getMatchId()).build() : null)
            .active(request.getActive())
            .market(request.getMarket())
            .selection(request.getSelection())
            .build();
        return odd;
    }
}
