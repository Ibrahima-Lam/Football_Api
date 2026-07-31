package com.fscore.app.service.impl;

import com.fscore.app.dto.response.GroupTeamResponse;
import com.fscore.app.entity.GroupTeam;
import com.fscore.app.exception.ResourceNotFoundException;
import com.fscore.app.repository.GroupTeamRepository;
import com.fscore.app.service.GroupTeamService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class GroupTeamServiceImpl implements GroupTeamService {

    private final GroupTeamRepository repository;

    public GroupTeamServiceImpl(GroupTeamRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<GroupTeam> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<GroupTeam> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public GroupTeam save(GroupTeam entity) {
        return repository.save(entity);
    }

    @Override
    public GroupTeam update(String id, GroupTeam entity) {
        GroupTeam existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("GroupTeam not found with id: " + id));
        entity.setId(existing.getId());
        return repository.save(entity);
    }

    @Override
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("GroupTeam not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
