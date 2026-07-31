package com.fscore.app.service;

import com.fscore.app.entity.OddHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface OddHistoryService {
    Page<OddHistory> findAll(Pageable pageable);
    Optional<OddHistory> findById(String id);
    OddHistory save(OddHistory entity);
    OddHistory update(String id, OddHistory entity);
    void delete(String id);
}
