package com.fscore.app.service.impl;

import com.fscore.app.dto.request.ApiKeyRequest;
import com.fscore.app.entity.ApiKey;
import com.fscore.app.entity.ApiUser;
import com.fscore.app.exception.ResourceNotFoundException;
import com.fscore.app.repository.ApiKeyRepository;
import com.fscore.app.security.ApiKeyHasher;
import com.fscore.app.service.ApiKeyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class ApiKeyServiceImpl implements ApiKeyService {

    private final ApiKeyRepository repository;
    private final ApiKeyHasher hasher;

    public ApiKeyServiceImpl(ApiKeyRepository repository, ApiKeyHasher hasher) {
        this.repository = repository;
        this.hasher = hasher;
    }

    @Override
    public Page<ApiKey> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<ApiKey> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public ApiKey save(ApiKey entity) {
        return repository.save(entity);
    }

    @Override
    public ApiKey update(String id, ApiKey entity) {
        ApiKey existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("ApiKey not found with id: " + id));
        entity.setId(existing.getId());
        return repository.save(entity);
    }

    @Override
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("ApiKey not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public GeneratedKey generate(ApiKeyRequest request) {
        String secret = hasher.generateSecret();
        ApiKey key = ApiKey.builder()
            .keyHash(hasher.hash(secret))
            .name(request.getName() != null ? request.getName() : "API key")
            .user(request.getUserId() != null ? ApiUser.builder().id(request.getUserId()).build() : null)
            .active(true)
            .expiresAt(request.getExpiresAt())
            .build();
        ApiKey saved = repository.save(key);
        return new GeneratedKey(saved, secret);
    }

    @Override
    public Optional<ApiUser> authenticate(String apiKey) {
        String hash = hasher.hash(apiKey);
        return repository.findByKeyHash(hash)
            .filter(k -> Boolean.TRUE.equals(k.getActive()))
            .filter(k -> k.getExpiresAt() == null || k.getExpiresAt().isAfter(LocalDateTime.now()))
            .flatMap(k -> {
                ApiUser user = k.getUser();
                if (user == null) {
                    return Optional.empty();
                }
                user.getName();
                k.setLastUsedAt(LocalDateTime.now());
                repository.save(k);
                return Optional.of(user);
            });
    }

    @Override
    public void touch(String id) {
        repository.findById(id).ifPresent(key -> {
            key.setLastUsedAt(LocalDateTime.now());
            repository.save(key);
        });
    }

    @Override
    public String mask(String keyHash) {
        if (keyHash == null || keyHash.isBlank()) {
            return "";
        }
        if (keyHash.length() <= 10) {
            return "••••••••••";
        }
        return "fscore_" + "••••••••••" + keyHash.substring(keyHash.length() - 6);
    }
}
