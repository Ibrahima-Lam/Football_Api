package com.fscore.app.service;

import com.fscore.app.dto.request.MatchLiveUpdateRequest;
import com.fscore.app.entity.Match;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface MatchService {
    Page<Match> findAll(Pageable pageable);
    Optional<Match> findById(String id);
    Match save(Match entity);
    Match update(String id, Match entity);
    Match applyLiveUpdate(String id, MatchLiveUpdateRequest request);
    void delete(String id);
}
