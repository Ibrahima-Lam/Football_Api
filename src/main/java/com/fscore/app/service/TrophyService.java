package com.fscore.app.service;

import com.fscore.app.entity.Trophy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface TrophyService {
    Page<Trophy> findAll(Pageable pageable);
    Optional<Trophy> findById(String id);
    Trophy save(Trophy entity);
    Trophy update(String id, Trophy entity);
    void delete(String id);
}
