package com.fscore.app.controller;

import com.fscore.app.dto.request.TransferRequest;
import com.fscore.app.dto.response.TransferResponse;
import com.fscore.app.dto.response.PageResponse;
import com.fscore.app.entity.Transfer;
import com.fscore.app.entity.*;
import com.fscore.app.service.TransferService;
import com.fscore.app.service.FilterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.net.URI;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private final TransferService service;
    private final FilterService filterService;

    public TransferController(TransferService service, FilterService filterService) {
        this.service = service;
        this.filterService = filterService;
    }

    @GetMapping
    public ResponseEntity<PageResponse> findAll(Pageable pageable, @RequestParam Map<String, String> params) {
        Page<Transfer> page = filterService.find("transferRepository", pageable, params);
        return ResponseEntity.ok(PageResponse.fromPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransferResponse> findById(@PathVariable String id) {
        Transfer entity = service.findById(id)
            .orElseThrow(() -> new com.fscore.app.exception.ResourceNotFoundException("Transfer not found with id: " + id));
        return ResponseEntity.ok(mapToResponse(entity));
    }

    @PostMapping
    public ResponseEntity<TransferResponse> save(@Valid @RequestBody TransferRequest request) {
        Transfer entity = mapToEntity(request);
        Transfer saved = service.save(entity);
        return ResponseEntity.created(URI.create("/api/transfers/" + saved.getId()))
            .body(mapToResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransferResponse> update(@PathVariable String id, @Valid @RequestBody TransferRequest request) {
        Transfer entity = mapToEntity(request);
        Transfer updated = service.update(id, entity);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private TransferResponse mapToResponse(Transfer entity) {
        TransferResponse response = TransferResponse.builder()
            .currency(entity.getCurrency())
            .fromTeamId(entity.getFromTeam() != null ? entity.getFromTeam().getId() : null)
            .id(entity.getId())
            .playerId(entity.getPlayer() != null ? entity.getPlayer().getId() : null)
            .seasonId(entity.getSeason() != null ? entity.getSeason().getId() : null)
            .toTeamId(entity.getToTeam() != null ? entity.getToTeam().getId() : null)
            .transferType(entity.getTransferType())
            .build();
        return response;
    }

    private Transfer mapToEntity(TransferRequest request) {
        Transfer transfer = Transfer.builder()
            .fromTeam(request.getFromTeamId() != null ? Team.builder().id(request.getFromTeamId()).build() : null)
            .player(request.getPlayerId() != null ? Player.builder().id(request.getPlayerId()).build() : null)
            .season(request.getSeasonId() != null ? Season.builder().id(request.getSeasonId()).build() : null)
            .toTeam(request.getToTeamId() != null ? Team.builder().id(request.getToTeamId()).build() : null)
            .currency(request.getCurrency())
            .transferType(request.getTransferType())
            .build();
        return transfer;
    }
}
