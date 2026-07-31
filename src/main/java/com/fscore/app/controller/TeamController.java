package com.fscore.app.controller;

import com.fscore.app.dto.request.TeamRequest;
import com.fscore.app.dto.response.TeamResponse;
import com.fscore.app.dto.response.PageResponse;
import com.fscore.app.entity.Team;
import com.fscore.app.entity.*;
import com.fscore.app.service.TeamService;
import com.fscore.app.service.FilterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.net.URI;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService service;
    private final FilterService filterService;

    public TeamController(TeamService service, FilterService filterService) {
        this.service = service;
        this.filterService = filterService;
    }

    @GetMapping
    public ResponseEntity<PageResponse> findAll(Pageable pageable, @RequestParam Map<String, String> params) {
        Page<Team> page = filterService.find("teamRepository", pageable, params);
        return ResponseEntity.ok(PageResponse.fromPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamResponse> findById(@PathVariable String id) {
        Team entity = service.findById(id)
            .orElseThrow(() -> new com.fscore.app.exception.ResourceNotFoundException("Team not found with id: " + id));
        return ResponseEntity.ok(mapToResponse(entity));
    }

    @PostMapping
    public ResponseEntity<TeamResponse> save(@Valid @RequestBody TeamRequest request) {
        Team entity = mapToEntity(request);
        Team saved = service.save(entity);
        return ResponseEntity.created(URI.create("/api/teams/" + saved.getId()))
            .body(mapToResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeamResponse> update(@PathVariable String id, @Valid @RequestBody TeamRequest request) {
        Team entity = mapToEntity(request);
        Team updated = service.update(id, entity);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private TeamResponse mapToResponse(Team entity) {
        TeamResponse response = TeamResponse.builder()
            .active(entity.getActive())
            .address(entity.getAddress())
            .code(entity.getCode())
            .countryId(entity.getCountry() != null ? entity.getCountry().getId() : null)
            .description(entity.getDescription())
            .email(entity.getEmail())
            .founded(entity.getFounded())
            .id(entity.getId())
            .kitPrimaryColor(entity.getKitPrimaryColor())
            .kitSecondaryColor(entity.getKitSecondaryColor())
            .logo(entity.getLogo())
            .name(entity.getName())
            .nationalTeam(entity.getNationalTeam())
            .phone(entity.getPhone())
            .shortName(entity.getShortName())
            .stadiumId(entity.getStadium() != null ? entity.getStadium().getId() : null)
            .type(entity.getType())
            .website(entity.getWebsite())
            .build();
        return response;
    }

    private Team mapToEntity(TeamRequest request) {
        Team team = Team.builder()
            .country(request.getCountryId() != null ? Country.builder().id(request.getCountryId()).build() : null)
            .stadium(request.getStadiumId() != null ? Stadium.builder().id(request.getStadiumId()).build() : null)
            .active(request.getActive())
            .address(request.getAddress())
            .code(request.getCode())
            .description(request.getDescription())
            .email(request.getEmail())
            .founded(request.getFounded())
            .kitPrimaryColor(request.getKitPrimaryColor())
            .kitSecondaryColor(request.getKitSecondaryColor())
            .logo(request.getLogo())
            .name(request.getName())
            .nationalTeam(request.getNationalTeam())
            .phone(request.getPhone())
            .shortName(request.getShortName())
            .type(request.getType())
            .website(request.getWebsite())
            .build();
        return team;
    }
}
