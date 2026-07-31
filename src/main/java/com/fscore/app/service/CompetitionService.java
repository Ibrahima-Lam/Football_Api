package com.fscore.app.service;

import com.fscore.app.entity.Competition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface CompetitionService {
    Page<Competition> findAll(Pageable pageable);
    Optional<Competition> findById(String id);
    Competition save(Competition entity);
    Competition update(String id, Competition entity);
    void delete(String id);
}
