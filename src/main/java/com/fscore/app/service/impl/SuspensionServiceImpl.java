package com.fscore.app.service.impl;

import com.fscore.app.dto.response.SuspensionResponse;
import com.fscore.app.entity.Suspension;
import com.fscore.app.exception.ResourceNotFoundException;
import com.fscore.app.repository.SuspensionRepository;
import com.fscore.app.service.SuspensionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class SuspensionServiceImpl implements SuspensionService {

    private final SuspensionRepository repository;

    public SuspensionServiceImpl(SuspensionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Suspension> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<Suspension> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Suspension save(Suspension entity) {
        return repository.save(entity);
    }

    @Override
    public Suspension update(String id, Suspension entity) {
        Suspension existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Suspension not found with id: " + id));
        entity.setId(existing.getId());
        return repository.save(entity);
    }

    @Override
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Suspension not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
