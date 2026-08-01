package com.fscore.app.service.impl;

import com.fscore.app.dto.response.MatchEventResponse;
import com.fscore.app.entity.MatchEvent;
import com.fscore.app.exception.ResourceNotFoundException;
import com.fscore.app.repository.MatchEventRepository;
import com.fscore.app.service.LiveScoreService;
import com.fscore.app.service.MatchEventService;
import com.fscore.app.service.PushNotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class MatchEventServiceImpl implements MatchEventService {

    private final MatchEventRepository repository;
    private final LiveScoreService liveScoreService;
    private final PushNotificationService pushNotificationService;

    public MatchEventServiceImpl(MatchEventRepository repository,
                                 LiveScoreService liveScoreService,
                                 PushNotificationService pushNotificationService) {
        this.repository = repository;
        this.liveScoreService = liveScoreService;
        this.pushNotificationService = pushNotificationService;
    }

    @Override
    public Page<MatchEvent> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<MatchEvent> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public MatchEvent save(MatchEvent entity) {
        MatchEvent saved = repository.save(entity);
        liveScoreService.broadcastMatchEvent(saved);
        pushNotificationService.notifyMatchEvent(saved);
        return saved;
    }

    @Override
    public MatchEvent update(String id, MatchEvent entity) {
        MatchEvent existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("MatchEvent not found with id: " + id));
        entity.setId(existing.getId());
        MatchEvent updated = repository.save(entity);
        liveScoreService.broadcastMatchEvent(updated);
        pushNotificationService.notifyMatchEvent(updated);
        return updated;
    }

    @Override
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("MatchEvent not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
