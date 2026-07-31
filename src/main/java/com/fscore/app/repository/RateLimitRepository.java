package com.fscore.app.repository;

import com.fscore.app.entity.RateLimit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RateLimitRepository extends JpaRepository<RateLimit, String>, JpaSpecificationExecutor<RateLimit> {
}
