package com.fscore.app.service.impl;

import com.fscore.app.dto.response.MatchEventResponse;
import com.fscore.app.entity.MatchEvent;
import com.fscore.app.exception.ResourceNotFoundException;
import com.fscore.app.repository.MatchEventRepository;
import com.fscore.app.service.MatchEventService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class MatchEventServiceImpl implements MatchEventService {

    private final MatchEventRepository repository;

    public MatchEventServiceImpl(MatchEventRepository repository) {
        this.repository = repository;
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
        return repository.save(entity);
    }

    @Override
    public MatchEvent update(String id, MatchEvent entity) {
        MatchEvent existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("MatchEvent not found with id: " + id));
        entity.setId(existing.getId());
        return repository.save(entity);
    }

    @Override
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("MatchEvent not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
