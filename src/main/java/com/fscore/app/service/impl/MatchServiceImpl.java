package com.fscore.app.service.impl;

import com.fscore.app.dto.request.MatchLiveUpdateRequest;
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
    public Match applyLiveUpdate(String id, MatchLiveUpdateRequest request) {
        Match existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Match not found with id: " + id));
        if (request.getKickoff() != null) existing.setKickoff(request.getKickoff());
        if (request.getStatus() != null) existing.setStatus(request.getStatus());
        if (request.getPeriod() != null) existing.setPeriod(request.getPeriod());
        if (request.getMinute() != null) existing.setMinute(request.getMinute());
        if (request.getMinuteExtra() != null) existing.setMinuteExtra(request.getMinuteExtra());
        if (request.getHomeScore() != null) existing.setHomeScore(request.getHomeScore());
        if (request.getAwayScore() != null) existing.setAwayScore(request.getAwayScore());
        if (request.getHomeHtScore() != null) existing.setHomeHtScore(request.getHomeHtScore());
        if (request.getAwayHtScore() != null) existing.setAwayHtScore(request.getAwayHtScore());
        if (request.getHomeEtScore() != null) existing.setHomeEtScore(request.getHomeEtScore());
        if (request.getAwayEtScore() != null) existing.setAwayEtScore(request.getAwayEtScore());
        if (request.getHomePenaltyScore() != null) existing.setHomePenaltyScore(request.getHomePenaltyScore());
        if (request.getAwayPenaltyScore() != null) existing.setAwayPenaltyScore(request.getAwayPenaltyScore());
        if (request.getHomePenaltyForm() != null) existing.setHomePenaltyForm(request.getHomePenaltyForm());
        if (request.getAwayPenaltyForm() != null) existing.setAwayPenaltyForm(request.getAwayPenaltyForm());
        Match updated = repository.save(existing);
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
