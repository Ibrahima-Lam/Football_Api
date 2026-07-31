package com.fscore.app.controller;

import com.fscore.app.dto.request.GroupTeamRequest;
import com.fscore.app.dto.response.GroupTeamResponse;
import com.fscore.app.dto.response.PageResponse;
import com.fscore.app.entity.GroupTeam;
import com.fscore.app.entity.*;
import com.fscore.app.service.GroupTeamService;
import com.fscore.app.service.FilterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.net.URI;

@RestController
@RequestMapping("/api/groupteams")
public class GroupTeamController {

    private final GroupTeamService service;
    private final FilterService filterService;

    public GroupTeamController(GroupTeamService service, FilterService filterService) {
        this.service = service;
        this.filterService = filterService;
    }

    @GetMapping
    public ResponseEntity<PageResponse> findAll(Pageable pageable, @RequestParam Map<String, String> params) {
        Page<GroupTeam> page = filterService.find("groupTeamRepository", pageable, params);
        return ResponseEntity.ok(PageResponse.fromPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupTeamResponse> findById(@PathVariable String id) {
        GroupTeam entity = service.findById(id)
            .orElseThrow(() -> new com.fscore.app.exception.ResourceNotFoundException("GroupTeam not found with id: " + id));
        return ResponseEntity.ok(mapToResponse(entity));
    }

    @PostMapping
    public ResponseEntity<GroupTeamResponse> save(@Valid @RequestBody GroupTeamRequest request) {
        GroupTeam entity = mapToEntity(request);
        GroupTeam saved = service.save(entity);
        return ResponseEntity.created(URI.create("/api/groupteams/" + saved.getId()))
            .body(mapToResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupTeamResponse> update(@PathVariable String id, @Valid @RequestBody GroupTeamRequest request) {
        GroupTeam entity = mapToEntity(request);
        GroupTeam updated = service.update(id, entity);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private GroupTeamResponse mapToResponse(GroupTeam entity) {
        GroupTeamResponse response = GroupTeamResponse.builder()
            .groupId(entity.getGroup() != null ? entity.getGroup().getId() : null)
            .id(entity.getId())
            .pot(entity.getPot())
            .qualification(entity.getQualification())
            .qualifiedFrom(entity.getQualifiedFrom())
            .seed(entity.getSeed())
            .teamId(entity.getTeam() != null ? entity.getTeam().getId() : null)
            .build();
        return response;
    }

    private GroupTeam mapToEntity(GroupTeamRequest request) {
        GroupTeam groupTeam = GroupTeam.builder()
            .group(request.getGroupId() != null ? Group.builder().id(request.getGroupId()).build() : null)
            .team(request.getTeamId() != null ? Team.builder().id(request.getTeamId()).build() : null)
            .pot(request.getPot())
            .qualification(request.getQualification())
            .qualifiedFrom(request.getQualifiedFrom())
            .seed(request.getSeed())
            .build();
        return groupTeam;
    }
}
