package com.fscore.app.controller;

import com.fscore.app.dto.request.DeviceTokenRequest;
import com.fscore.app.dto.response.DeviceTokenResponse;
import com.fscore.app.entity.ApiUser;
import com.fscore.app.entity.DeviceToken;
import com.fscore.app.exception.ResourceNotFoundException;
import com.fscore.app.service.DeviceTokenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/device-tokens")
public class DeviceTokenController {

    private final DeviceTokenService service;

    public DeviceTokenController(DeviceTokenService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<DeviceTokenResponse>> findAll() {
        ApiUser user = currentUser();
        List<DeviceTokenResponse> tokens = service.findByUser(user.getId()).stream()
            .map(this::mapToResponse)
            .toList();
        return ResponseEntity.ok(tokens);
    }

    @PostMapping
    public ResponseEntity<DeviceTokenResponse> register(@Valid @RequestBody DeviceTokenRequest request) {
        DeviceToken saved = service.register(currentUser(), request);
        return ResponseEntity.created(URI.create("/api/device-tokens/" + saved.getId()))
            .body(mapToResponse(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> unregister(@PathVariable String id) {
        ApiUser user = currentUser();
        DeviceToken token = service.findByUser(user.getId()).stream()
            .filter(candidate -> candidate.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("DeviceToken not found with id: " + id));
        service.unregister(user.getId(), token.getToken());
        return ResponseEntity.noContent().build();
    }

    private DeviceTokenResponse mapToResponse(DeviceToken entity) {
        return DeviceTokenResponse.builder()
            .id(entity.getId())
            .userId(entity.getUser() != null ? entity.getUser().getId() : null)
            .token(entity.getToken())
            .platform(entity.getPlatform())
            .createdAt(entity.getCreatedAt())
            .build();
    }

    private ApiUser currentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof ApiUser user) {
            return user;
        }
        throw new IllegalStateException("No authenticated user");
    }
}
