package com.fscore.app.service;

import com.fscore.app.entity.ApiUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface ApiUserService {
    Page<ApiUser> findAll(Pageable pageable);
    Optional<ApiUser> findById(String id);
    ApiUser save(ApiUser entity);
    ApiUser update(String id, ApiUser entity);
    void delete(String id);
}
