package com.fscore.app.service;

import com.fscore.app.entity.Standing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface StandingService {
    Page<Standing> findAll(Pageable pageable);
    Optional<Standing> findById(String id);
    Standing save(Standing entity);
    Standing update(String id, Standing entity);
    void delete(String id);
}
