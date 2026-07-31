package com.fscore.app.controller;

import com.fscore.app.dto.request.BookmakerRequest;
import com.fscore.app.dto.response.BookmakerResponse;
import com.fscore.app.dto.response.PageResponse;
import com.fscore.app.entity.Bookmaker;
import com.fscore.app.entity.*;
import com.fscore.app.service.BookmakerService;
import com.fscore.app.service.FilterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.net.URI;

@RestController
@RequestMapping("/api/bookmakers")
public class BookmakerController {

    private final BookmakerService service;
    private final FilterService filterService;

    public BookmakerController(BookmakerService service, FilterService filterService) {
        this.service = service;
        this.filterService = filterService;
    }

    @GetMapping
    public ResponseEntity<PageResponse> findAll(Pageable pageable, @RequestParam Map<String, String> params) {
        Page<Bookmaker> page = filterService.find("bookmakerRepository", pageable, params);
        return ResponseEntity.ok(PageResponse.fromPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookmakerResponse> findById(@PathVariable String id) {
        Bookmaker entity = service.findById(id)
            .orElseThrow(() -> new com.fscore.app.exception.ResourceNotFoundException("Bookmaker not found with id: " + id));
        return ResponseEntity.ok(mapToResponse(entity));
    }

    @PostMapping
    public ResponseEntity<BookmakerResponse> save(@Valid @RequestBody BookmakerRequest request) {
        Bookmaker entity = mapToEntity(request);
        Bookmaker saved = service.save(entity);
        return ResponseEntity.created(URI.create("/api/bookmakers/" + saved.getId()))
            .body(mapToResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookmakerResponse> update(@PathVariable String id, @Valid @RequestBody BookmakerRequest request) {
        Bookmaker entity = mapToEntity(request);
        Bookmaker updated = service.update(id, entity);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private BookmakerResponse mapToResponse(Bookmaker entity) {
        BookmakerResponse response = BookmakerResponse.builder()
            .active(entity.getActive())
            .id(entity.getId())
            .logo(entity.getLogo())
            .name(entity.getName())
            .website(entity.getWebsite())
            .build();
        return response;
    }

    private Bookmaker mapToEntity(BookmakerRequest request) {
        Bookmaker bookmaker = Bookmaker.builder()
            .active(request.getActive())
            .logo(request.getLogo())
            .name(request.getName())
            .website(request.getWebsite())
            .build();
        return bookmaker;
    }
}
