package com.fscore.app.service.impl;

import com.fscore.app.dto.response.CompetitionResponse;
import com.fscore.app.entity.Competition;
import com.fscore.app.exception.ResourceNotFoundException;
import com.fscore.app.repository.CompetitionRepository;
import com.fscore.app.service.CompetitionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class CompetitionServiceImpl implements CompetitionService {

    private final CompetitionRepository repository;

    public CompetitionServiceImpl(CompetitionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Competition> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<Competition> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Competition save(Competition entity) {
        return repository.save(entity);
    }

    @Override
    public Competition update(String id, Competition entity) {
        Competition existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Competition not found with id: " + id));
        entity.setId(existing.getId());
        return repository.save(entity);
    }

    @Override
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Competition not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
