package com.fscore.app.service.impl;

import com.fscore.app.dto.response.MatchResponse;
import com.fscore.app.entity.Match;
import com.fscore.app.exception.ResourceNotFoundException;
import com.fscore.app.repository.MatchRepository;
import com.fscore.app.service.LiveScoreService;
import com.fscore.app.service.MatchService;
import com.fscore.app.service.PushNotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class MatchServiceImpl implements MatchService {

    private final MatchRepository repository;
    private final LiveScoreService liveScoreService;
    private final PushNotificationService pushNotificationService;

    public MatchServiceImpl(MatchRepository repository,
                            LiveScoreService liveScoreService,
                            PushNotificationService pushNotificationService) {
        this.repository = repository;
        this.liveScoreService = liveScoreService;
        this.pushNotificationService = pushNotificationService;
    }

    @Override
    public Page<Match> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<Match> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Match save(Match entity) {
        Match saved = repository.save(entity);
        liveScoreService.broadcastMatchUpdate(saved);
        return saved;
    }

    @Override
    public Match update(String id, Match entity) {
        Match existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Match not found with id: " + id));
        entity.setId(existing.getId());
        Match updated = repository.save(entity);
        liveScoreService.broadcastMatchUpdate(updated);
        pushNotificationService.notifyMatchUpdate(existing, updated);
        return updated;
    }

    @Override
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Match not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
