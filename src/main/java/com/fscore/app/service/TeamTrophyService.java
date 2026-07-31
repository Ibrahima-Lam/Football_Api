package com.fscore.app.service;

import com.fscore.app.entity.TeamTrophy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface TeamTrophyService {
    Page<TeamTrophy> findAll(Pageable pageable);
    Optional<TeamTrophy> findById(String id);
    TeamTrophy save(TeamTrophy entity);
    TeamTrophy update(String id, TeamTrophy entity);
    void delete(String id);
}
