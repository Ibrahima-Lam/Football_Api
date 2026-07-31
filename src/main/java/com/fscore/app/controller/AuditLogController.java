package com.fscore.app.controller;

import com.fscore.app.dto.request.AuditLogRequest;
import com.fscore.app.dto.response.AuditLogResponse;
import com.fscore.app.dto.response.PageResponse;
import com.fscore.app.entity.AuditLog;
import com.fscore.app.entity.*;
import com.fscore.app.service.AuditLogService;
import com.fscore.app.service.FilterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.net.URI;

@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogController {

    private final AuditLogService service;
    private final FilterService filterService;

    public AuditLogController(AuditLogService service, FilterService filterService) {
        this.service = service;
        this.filterService = filterService;
    }

    @GetMapping
    public ResponseEntity<PageResponse> findAll(Pageable pageable, @RequestParam Map<String, String> params) {
        Page<AuditLog> page = filterService.find("auditLogRepository", pageable, params);
        return ResponseEntity.ok(PageResponse.fromPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuditLogResponse> findById(@PathVariable String id) {
        AuditLog entity = service.findById(id)
            .orElseThrow(() -> new com.fscore.app.exception.ResourceNotFoundException("AuditLog not found with id: " + id));
        return ResponseEntity.ok(mapToResponse(entity));
    }

    @PostMapping
    public ResponseEntity<AuditLogResponse> save(@Valid @RequestBody AuditLogRequest request) {
        AuditLog entity = mapToEntity(request);
        AuditLog saved = service.save(entity);
        return ResponseEntity.created(URI.create("/api/audit-logs/" + saved.getId()))
            .body(mapToResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuditLogResponse> update(@PathVariable String id, @Valid @RequestBody AuditLogRequest request) {
        AuditLog entity = mapToEntity(request);
        AuditLog updated = service.update(id, entity);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private AuditLogResponse mapToResponse(AuditLog entity) {
        AuditLogResponse response = AuditLogResponse.builder()
            .apiKeyId(entity.getApiKey() != null ? entity.getApiKey().getId() : null)
            .endpoint(entity.getEndpoint())
            .id(entity.getId())
            .ipAddress(entity.getIpAddress() != null ? entity.getIpAddress().getHostAddress() : null)
            .method(entity.getMethod())
            .responseTimeMs(entity.getResponseTimeMs())
            .statusCode(entity.getStatusCode())
            .build();
        return response;
    }

    private AuditLog mapToEntity(AuditLogRequest request) {
        AuditLog auditLog = AuditLog.builder()
            .apiKey(request.getApiKeyId() != null ? ApiKey.builder().id(request.getApiKeyId()).build() : null)
            .endpoint(request.getEndpoint())
            .ipAddress(toInetAddress(request.getIpAddress()))
            .method(request.getMethod())
            .responseTimeMs(request.getResponseTimeMs())
            .statusCode(request.getStatusCode())
            .build();
        return auditLog;
    }

    private java.net.InetAddress toInetAddress(String ip) {
        if (ip == null) return null;
        try {
            return java.net.InetAddress.getByName(ip);
        } catch (java.net.UnknownHostException e) {
            throw new RuntimeException("Invalid IP address: " + ip, e);
        }
    }
}
