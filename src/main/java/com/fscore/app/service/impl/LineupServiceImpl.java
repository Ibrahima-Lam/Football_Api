package com.fscore.app.service.impl;

import com.fscore.app.dto.response.LineupResponse;
import com.fscore.app.entity.Lineup;
import com.fscore.app.exception.ResourceNotFoundException;
import com.fscore.app.repository.LineupRepository;
import com.fscore.app.service.LineupService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class LineupServiceImpl implements LineupService {

    private final LineupRepository repository;

    public LineupServiceImpl(LineupRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Lineup> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<Lineup> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Lineup save(Lineup entity) {
        return repository.save(entity);
    }

    @Override
    public Lineup update(String id, Lineup entity) {
        Lineup existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Lineup not found with id: " + id));
        entity.setId(existing.getId());
        return repository.save(entity);
    }

    @Override
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Lineup not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
