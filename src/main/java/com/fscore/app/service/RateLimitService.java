package com.fscore.app.service;

import com.fscore.app.entity.RateLimit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface RateLimitService {
    Page<RateLimit> findAll(Pageable pageable);
    Optional<RateLimit> findById(String id);
    RateLimit save(RateLimit entity);
    RateLimit update(String id, RateLimit entity);
    void delete(String id);
}
