package com.fscore.app.controller;

import com.fscore.app.dto.request.ContractRequest;
import com.fscore.app.dto.response.ContractResponse;
import com.fscore.app.dto.response.PageResponse;
import com.fscore.app.entity.Contract;
import com.fscore.app.entity.*;
import com.fscore.app.service.ContractService;
import com.fscore.app.service.FilterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.net.URI;

@RestController
@RequestMapping("/api/contracts")
public class ContractController {

    private final ContractService service;
    private final FilterService filterService;

    public ContractController(ContractService service, FilterService filterService) {
        this.service = service;
        this.filterService = filterService;
    }

    @GetMapping
    public ResponseEntity<PageResponse> findAll(Pageable pageable, @RequestParam Map<String, String> params) {
        Page<Contract> page = filterService.find("contractRepository", pageable, params);
        return ResponseEntity.ok(PageResponse.fromPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContractResponse> findById(@PathVariable String id) {
        Contract entity = service.findById(id)
            .orElseThrow(() -> new com.fscore.app.exception.ResourceNotFoundException("Contract not found with id: " + id));
        return ResponseEntity.ok(mapToResponse(entity));
    }

    @PostMapping
    public ResponseEntity<ContractResponse> save(@Valid @RequestBody ContractRequest request) {
        Contract entity = mapToEntity(request);
        Contract saved = service.save(entity);
        return ResponseEntity.created(URI.create("/api/contracts/" + saved.getId()))
            .body(mapToResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContractResponse> update(@PathVariable String id, @Valid @RequestBody ContractRequest request) {
        Contract entity = mapToEntity(request);
        Contract updated = service.update(id, entity);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private ContractResponse mapToResponse(Contract entity) {
        ContractResponse response = ContractResponse.builder()
            .current(entity.getCurrent())
            .id(entity.getId())
            .playerId(entity.getPlayer() != null ? entity.getPlayer().getId() : null)
            .shirtNumber(entity.getShirtNumber())
            .teamId(entity.getTeam() != null ? entity.getTeam().getId() : null)
            .build();
        return response;
    }

    private Contract mapToEntity(ContractRequest request) {
        Contract contract = Contract.builder()
            .player(request.getPlayerId() != null ? Player.builder().id(request.getPlayerId()).build() : null)
            .team(request.getTeamId() != null ? Team.builder().id(request.getTeamId()).build() : null)
            .current(request.getCurrent())
            .shirtNumber(request.getShirtNumber())
            .build();
        return contract;
    }
}
