package com.fscore.app.service.impl;

import com.fscore.app.dto.response.TeamTrophyResponse;
import com.fscore.app.entity.TeamTrophy;
import com.fscore.app.exception.ResourceNotFoundException;
import com.fscore.app.repository.TeamTrophyRepository;
import com.fscore.app.service.TeamTrophyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class TeamTrophyServiceImpl implements TeamTrophyService {

    private final TeamTrophyRepository repository;

    public TeamTrophyServiceImpl(TeamTrophyRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<TeamTrophy> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<TeamTrophy> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public TeamTrophy save(TeamTrophy entity) {
        return repository.save(entity);
    }

    @Override
    public TeamTrophy update(String id, TeamTrophy entity) {
        TeamTrophy existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("TeamTrophy not found with id: " + id));
        entity.setId(existing.getId());
        return repository.save(entity);
    }

    @Override
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("TeamTrophy not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
