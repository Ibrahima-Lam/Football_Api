package com.fscore.app.service;

import com.fscore.app.entity.MatchPenaltyShootoutShot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface MatchPenaltyShootoutShotService {
    Page<MatchPenaltyShootoutShot> findAll(Pageable pageable);
    Optional<MatchPenaltyShootoutShot> findById(String id);
    MatchPenaltyShootoutShot save(MatchPenaltyShootoutShot entity);
    MatchPenaltyShootoutShot update(String id, MatchPenaltyShootoutShot entity);
    void delete(String id);
}
