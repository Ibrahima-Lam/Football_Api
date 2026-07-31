package com.fscore.app.service;

import com.fscore.app.entity.MatchEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface MatchEventService {
    Page<MatchEvent> findAll(Pageable pageable);
    Optional<MatchEvent> findById(String id);
    MatchEvent save(MatchEvent entity);
    MatchEvent update(String id, MatchEvent entity);
    void delete(String id);
}
