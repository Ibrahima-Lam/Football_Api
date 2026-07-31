package com.fscore.app.service;

import com.fscore.app.entity.TeamCoach;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface TeamCoachService {
    Page<TeamCoach> findAll(Pageable pageable);
    Optional<TeamCoach> findById(String id);
    TeamCoach save(TeamCoach entity);
    TeamCoach update(String id, TeamCoach entity);
    void delete(String id);
}
