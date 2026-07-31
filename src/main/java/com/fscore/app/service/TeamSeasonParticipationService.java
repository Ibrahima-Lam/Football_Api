package com.fscore.app.service;

import com.fscore.app.entity.TeamSeasonParticipation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface TeamSeasonParticipationService {
    Page<TeamSeasonParticipation> findAll(Pageable pageable);
    Optional<TeamSeasonParticipation> findById(String id);
    TeamSeasonParticipation save(TeamSeasonParticipation entity);
    TeamSeasonParticipation update(String id, TeamSeasonParticipation entity);
    void delete(String id);
}
