package com.fscore.app.service.impl;

import com.fscore.app.dto.response.CoachResponse;
import com.fscore.app.entity.Coach;
import com.fscore.app.exception.ResourceNotFoundException;
import com.fscore.app.repository.CoachRepository;
import com.fscore.app.service.CoachService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class CoachServiceImpl implements CoachService {

    private final CoachRepository repository;

    public CoachServiceImpl(CoachRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Coach> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<Coach> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Coach save(Coach entity) {
        return repository.save(entity);
    }

    @Override
    public Coach update(String id, Coach entity) {
        Coach existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Coach not found with id: " + id));
        entity.setId(existing.getId());
        return repository.save(entity);
    }

    @Override
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Coach not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
