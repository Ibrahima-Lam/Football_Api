package com.fscore.app.controller;

import com.fscore.app.dto.request.PlayerRequest;
import com.fscore.app.dto.response.PlayerResponse;
import com.fscore.app.dto.response.PageResponse;
import com.fscore.app.entity.Player;
import com.fscore.app.entity.*;
import com.fscore.app.service.PlayerService;
import com.fscore.app.service.FilterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.net.URI;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private final PlayerService service;
    private final FilterService filterService;

    public PlayerController(PlayerService service, FilterService filterService) {
        this.service = service;
        this.filterService = filterService;
    }

    @GetMapping
    public ResponseEntity<PageResponse> findAll(Pageable pageable, @RequestParam Map<String, String> params) {
        Page<Player> page = filterService.find("playerRepository", pageable, params);
        return ResponseEntity.ok(PageResponse.fromPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerResponse> findById(@PathVariable String id) {
        Player entity = service.findById(id)
            .orElseThrow(() -> new com.fscore.app.exception.ResourceNotFoundException("Player not found with id: " + id));
        return ResponseEntity.ok(mapToResponse(entity));
    }

    @PostMapping
    public ResponseEntity<PlayerResponse> save(@Valid @RequestBody PlayerRequest request) {
        Player entity = mapToEntity(request);
        Player saved = service.save(entity);
        return ResponseEntity.created(URI.create("/api/players/" + saved.getId()))
            .body(mapToResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlayerResponse> update(@PathVariable String id, @Valid @RequestBody PlayerRequest request) {
        Player entity = mapToEntity(request);
        Player updated = service.update(id, entity);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private PlayerResponse mapToResponse(Player entity) {
        PlayerResponse response = PlayerResponse.builder()
            .birthPlace(entity.getBirthPlace())
            .countryId(entity.getCountry() != null ? entity.getCountry().getId() : null)
            .firstName(entity.getFirstName())
            .fullName(entity.getFullName())
            .id(entity.getId())
            .instagram(entity.getInstagram())
            .lastName(entity.getLastName())
            .nationalityId(entity.getNationality() != null ? entity.getNationality().getId() : null)
            .photo(entity.getPhoto())
            .position(entity.getPosition())
            .preferredFoot(entity.getPreferredFoot())
            .secondNationalityId(entity.getSecondNationality() != null ? entity.getSecondNationality().getId() : null)
            .status(entity.getStatus())
            .twitter(entity.getTwitter())
            .build();
        return response;
    }

    private Player mapToEntity(PlayerRequest request) {
        Player player = Player.builder()
            .country(request.getCountryId() != null ? Country.builder().id(request.getCountryId()).build() : null)
            .nationality(request.getNationalityId() != null ? Country.builder().id(request.getNationalityId()).build() : null)
            .secondNationality(request.getSecondNationalityId() != null ? Country.builder().id(request.getSecondNationalityId()).build() : null)
            .birthPlace(request.getBirthPlace())
            .firstName(request.getFirstName())
            .fullName(request.getFullName())
            .instagram(request.getInstagram())
            .lastName(request.getLastName())
            .photo(request.getPhoto())
            .position(request.getPosition())
            .preferredFoot(request.getPreferredFoot())
            .status(request.getStatus())
            .twitter(request.getTwitter())
            .build();
        return player;
    }
}
