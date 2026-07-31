package com.fscore.app.service;

import com.fscore.app.entity.Suspension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface SuspensionService {
    Page<Suspension> findAll(Pageable pageable);
    Optional<Suspension> findById(String id);
    Suspension save(Suspension entity);
    Suspension update(String id, Suspension entity);
    void delete(String id);
}
