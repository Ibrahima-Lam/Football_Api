package com.fscore.app.service.impl;

import com.fscore.app.dto.response.SeasonResponse;
import com.fscore.app.entity.Season;
import com.fscore.app.exception.ResourceNotFoundException;
import com.fscore.app.repository.SeasonRepository;
import com.fscore.app.service.SeasonService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class SeasonServiceImpl implements SeasonService {

    private final SeasonRepository repository;

    public SeasonServiceImpl(SeasonRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Season> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<Season> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Season save(Season entity) {
        return repository.save(entity);
    }

    @Override
    public Season update(String id, Season entity) {
        Season existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Season not found with id: " + id));
        entity.setId(existing.getId());
        return repository.save(entity);
    }

    @Override
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Season not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
