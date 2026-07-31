package com.fscore.app.repository;

import com.fscore.app.entity.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, String>, JpaSpecificationExecutor<ApiKey> {

    Optional<ApiKey> findByKeyHash(String keyHash);
}
