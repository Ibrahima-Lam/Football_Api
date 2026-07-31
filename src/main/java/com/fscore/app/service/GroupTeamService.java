package com.fscore.app.service;

import com.fscore.app.entity.GroupTeam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface GroupTeamService {
    Page<GroupTeam> findAll(Pageable pageable);
    Optional<GroupTeam> findById(String id);
    GroupTeam save(GroupTeam entity);
    GroupTeam update(String id, GroupTeam entity);
    void delete(String id);
}
