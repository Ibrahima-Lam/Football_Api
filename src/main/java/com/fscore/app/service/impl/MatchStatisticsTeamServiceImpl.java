package com.fscore.app.service.impl;

import com.fscore.app.dto.response.MatchStatisticsTeamResponse;
import com.fscore.app.entity.MatchStatisticsTeam;
import com.fscore.app.exception.ResourceNotFoundException;
import com.fscore.app.repository.MatchStatisticsTeamRepository;
import com.fscore.app.service.MatchStatisticsTeamService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class MatchStatisticsTeamServiceImpl implements MatchStatisticsTeamService {

    private final MatchStatisticsTeamRepository repository;

    public MatchStatisticsTeamServiceImpl(MatchStatisticsTeamRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<MatchStatisticsTeam> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<MatchStatisticsTeam> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public MatchStatisticsTeam save(MatchStatisticsTeam entity) {
        return repository.save(entity);
    }

    @Override
    public MatchStatisticsTeam update(String id, MatchStatisticsTeam entity) {
        MatchStatisticsTeam existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("MatchStatisticsTeam not found with id: " + id));
        entity.setId(existing.getId());
        return repository.save(entity);
    }

    @Override
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("MatchStatisticsTeam not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
