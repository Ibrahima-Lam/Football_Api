package com.fscore.app.service.impl;

import com.fscore.app.dto.response.ConfederationResponse;
import com.fscore.app.entity.Confederation;
import com.fscore.app.exception.ResourceNotFoundException;
import com.fscore.app.repository.ConfederationRepository;
import com.fscore.app.service.ConfederationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class ConfederationServiceImpl implements ConfederationService {

    private final ConfederationRepository repository;

    public ConfederationServiceImpl(ConfederationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Confederation> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<Confederation> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Confederation save(Confederation entity) {
        return repository.save(entity);
    }

    @Override
    public Confederation update(String id, Confederation entity) {
        Confederation existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Confederation not found with id: " + id));
        entity.setId(existing.getId());
        return repository.save(entity);
    }

    @Override
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Confederation not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
