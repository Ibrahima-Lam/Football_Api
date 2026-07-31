package com.fscore.app.service;

import com.fscore.app.entity.MatchFormation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface MatchFormationService {
    Page<MatchFormation> findAll(Pageable pageable);
    Optional<MatchFormation> findById(String id);
    MatchFormation save(MatchFormation entity);
    MatchFormation update(String id, MatchFormation entity);
    void delete(String id);
}
