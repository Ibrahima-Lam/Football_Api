package com.fscore.app.service;

import com.fscore.app.dto.request.ApiKeyRequest;
import com.fscore.app.entity.ApiKey;
import com.fscore.app.entity.ApiUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ApiKeyService {

    record GeneratedKey(ApiKey apiKey, String secret) {}

    Page<ApiKey> findAll(Pageable pageable);

    Optional<ApiKey> findById(String id);

    ApiKey save(ApiKey entity);

    ApiKey update(String id, ApiKey entity);

    void delete(String id);

    GeneratedKey generate(ApiKeyRequest request);

    Optional<ApiUser> authenticate(String apiKey);

    void touch(String id);

    String mask(String keyHash);
}
