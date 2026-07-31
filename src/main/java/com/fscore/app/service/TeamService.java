package com.fscore.app.service;

import com.fscore.app.entity.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface TeamService {
    Page<Team> findAll(Pageable pageable);
    Optional<Team> findById(String id);
    Team save(Team entity);
    Team update(String id, Team entity);
    void delete(String id);
}
