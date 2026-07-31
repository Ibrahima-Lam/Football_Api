package com.fscore.app.service;

import com.fscore.app.entity.Season;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface SeasonService {
    Page<Season> findAll(Pageable pageable);
    Optional<Season> findById(String id);
    Season save(Season entity);
    Season update(String id, Season entity);
    void delete(String id);
}
