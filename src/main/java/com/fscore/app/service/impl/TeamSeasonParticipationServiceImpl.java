package com.fscore.app.service.impl;

import com.fscore.app.dto.response.TeamSeasonParticipationResponse;
import com.fscore.app.entity.TeamSeasonParticipation;
import com.fscore.app.exception.ResourceNotFoundException;
import com.fscore.app.repository.TeamSeasonParticipationRepository;
import com.fscore.app.service.TeamSeasonParticipationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class TeamSeasonParticipationServiceImpl implements TeamSeasonParticipationService {

    private final TeamSeasonParticipationRepository repository;

    public TeamSeasonParticipationServiceImpl(TeamSeasonParticipationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<TeamSeasonParticipation> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<TeamSeasonParticipation> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public TeamSeasonParticipation save(TeamSeasonParticipation entity) {
        return repository.save(entity);
    }

    @Override
    public TeamSeasonParticipation update(String id, TeamSeasonParticipation entity) {
        TeamSeasonParticipation existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("TeamSeasonParticipation not found with id: " + id));
        entity.setId(existing.getId());
        return repository.save(entity);
    }

    @Override
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("TeamSeasonParticipation not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
