package com.fscore.app.service;

import com.fscore.app.entity.Lineup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface LineupService {
    Page<Lineup> findAll(Pageable pageable);
    Optional<Lineup> findById(String id);
    Lineup save(Lineup entity);
    Lineup update(String id, Lineup entity);
    void delete(String id);
}
