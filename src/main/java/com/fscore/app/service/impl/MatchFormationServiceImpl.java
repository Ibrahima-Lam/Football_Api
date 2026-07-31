package com.fscore.app.service.impl;

import com.fscore.app.dto.response.MatchFormationResponse;
import com.fscore.app.entity.MatchFormation;
import com.fscore.app.exception.ResourceNotFoundException;
import com.fscore.app.repository.MatchFormationRepository;
import com.fscore.app.service.MatchFormationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class MatchFormationServiceImpl implements MatchFormationService {

    private final MatchFormationRepository repository;

    public MatchFormationServiceImpl(MatchFormationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<MatchFormation> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<MatchFormation> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public MatchFormation save(MatchFormation entity) {
        return repository.save(entity);
    }

    @Override
    public MatchFormation update(String id, MatchFormation entity) {
        MatchFormation existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("MatchFormation not found with id: " + id));
        entity.setId(existing.getId());
        return repository.save(entity);
    }

    @Override
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("MatchFormation not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
