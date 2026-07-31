package com.fscore.app.service;

import com.fscore.app.entity.MatchStatisticsTeam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface MatchStatisticsTeamService {
    Page<MatchStatisticsTeam> findAll(Pageable pageable);
    Optional<MatchStatisticsTeam> findById(String id);
    MatchStatisticsTeam save(MatchStatisticsTeam entity);
    MatchStatisticsTeam update(String id, MatchStatisticsTeam entity);
    void delete(String id);
}
