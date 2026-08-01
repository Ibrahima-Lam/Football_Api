package com.fscore.app.service.impl;

import com.fscore.app.dto.response.MatchStatisticsPlayerResponse;
import com.fscore.app.entity.MatchStatisticsPlayer;
import com.fscore.app.exception.ResourceNotFoundException;
import com.fscore.app.repository.MatchStatisticsPlayerRepository;
import com.fscore.app.service.LiveScoreService;
import com.fscore.app.service.MatchStatisticsPlayerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class MatchStatisticsPlayerServiceImpl implements MatchStatisticsPlayerService {

    private final MatchStatisticsPlayerRepository repository;
    private final LiveScoreService liveScoreService;

    public MatchStatisticsPlayerServiceImpl(MatchStatisticsPlayerRepository repository, LiveScoreService liveScoreService) {
        this.repository = repository;
        this.liveScoreService = liveScoreService;
    }

    @Override
    public Page<MatchStatisticsPlayer> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<MatchStatisticsPlayer> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public MatchStatisticsPlayer save(MatchStatisticsPlayer entity) {
        MatchStatisticsPlayer saved = repository.save(entity);
        liveScoreService.broadcastPlayerStats(saved);
        return saved;
    }

    @Override
    public MatchStatisticsPlayer update(String id, MatchStatisticsPlayer entity) {
        MatchStatisticsPlayer existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("MatchStatisticsPlayer not found with id: " + id));
        entity.setId(existing.getId());
        MatchStatisticsPlayer updated = repository.save(entity);
        liveScoreService.broadcastPlayerStats(updated);
        return updated;
    }

    @Override
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("MatchStatisticsPlayer not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
