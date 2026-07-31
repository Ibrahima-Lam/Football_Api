package com.fscore.app.service;

import com.fscore.app.entity.Coach;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface CoachService {
    Page<Coach> findAll(Pageable pageable);
    Optional<Coach> findById(String id);
    Coach save(Coach entity);
    Coach update(String id, Coach entity);
    void delete(String id);
}
