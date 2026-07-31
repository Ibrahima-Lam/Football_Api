package com.fscore.app.service;

import com.fscore.app.entity.Confederation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface ConfederationService {
    Page<Confederation> findAll(Pageable pageable);
    Optional<Confederation> findById(String id);
    Confederation save(Confederation entity);
    Confederation update(String id, Confederation entity);
    void delete(String id);
}
