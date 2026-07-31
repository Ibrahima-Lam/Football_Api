package com.fscore.app.service;

import com.fscore.app.entity.MatchStatisticsPlayer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface MatchStatisticsPlayerService {
    Page<MatchStatisticsPlayer> findAll(Pageable pageable);
    Optional<MatchStatisticsPlayer> findById(String id);
    MatchStatisticsPlayer save(MatchStatisticsPlayer entity);
    MatchStatisticsPlayer update(String id, MatchStatisticsPlayer entity);
    void delete(String id);
}
