package com.fscore.app.service.impl;

import com.fscore.app.dto.response.TeamCoachResponse;
import com.fscore.app.entity.TeamCoach;
import com.fscore.app.exception.ResourceNotFoundException;
import com.fscore.app.repository.TeamCoachRepository;
import com.fscore.app.service.TeamCoachService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class TeamCoachServiceImpl implements TeamCoachService {

    private final TeamCoachRepository repository;

    public TeamCoachServiceImpl(TeamCoachRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<TeamCoach> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<TeamCoach> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public TeamCoach save(TeamCoach entity) {
        return repository.save(entity);
    }

    @Override
    public TeamCoach update(String id, TeamCoach entity) {
        TeamCoach existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("TeamCoach not found with id: " + id));
        entity.setId(existing.getId());
        return repository.save(entity);
    }

    @Override
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("TeamCoach not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
