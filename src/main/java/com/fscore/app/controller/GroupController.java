package com.fscore.app.controller;

import com.fscore.app.dto.request.GroupRequest;
import com.fscore.app.dto.response.GroupResponse;
import com.fscore.app.dto.response.PageResponse;
import com.fscore.app.entity.Group;
import com.fscore.app.entity.*;
import com.fscore.app.service.GroupService;
import com.fscore.app.service.FilterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.net.URI;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupService service;
    private final FilterService filterService;

    public GroupController(GroupService service, FilterService filterService) {
        this.service = service;
        this.filterService = filterService;
    }

    @GetMapping
    public ResponseEntity<PageResponse> findAll(Pageable pageable, @RequestParam Map<String, String> params) {
        Page<Group> page = filterService.find("groupRepository", pageable, params);
        return ResponseEntity.ok(PageResponse.fromPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupResponse> findById(@PathVariable String id) {
        Group entity = service.findById(id)
            .orElseThrow(() -> new com.fscore.app.exception.ResourceNotFoundException("Group not found with id: " + id));
        return ResponseEntity.ok(mapToResponse(entity));
    }

    @PostMapping
    public ResponseEntity<GroupResponse> save(@Valid @RequestBody GroupRequest request) {
        Group entity = mapToEntity(request);
        Group saved = service.save(entity);
        return ResponseEntity.created(URI.create("/api/groups/" + saved.getId()))
            .body(mapToResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupResponse> update(@PathVariable String id, @Valid @RequestBody GroupRequest request) {
        Group entity = mapToEntity(request);
        Group updated = service.update(id, entity);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private GroupResponse mapToResponse(Group entity) {
        GroupResponse response = GroupResponse.builder()
            .id(entity.getId())
            .name(entity.getName())
            .stageId(entity.getStage() != null ? entity.getStage().getId() : null)
            .build();
        return response;
    }

    private Group mapToEntity(GroupRequest request) {
        Group group = Group.builder()
            .stage(request.getStageId() != null ? Stage.builder().id(request.getStageId()).build() : null)
            .name(request.getName())
            .build();
        return group;
    }
}
