package com.fscore.app.service.impl;

import com.fscore.app.dto.response.HeadToHeadResponse;
import com.fscore.app.entity.HeadToHead;
import com.fscore.app.exception.ResourceNotFoundException;
import com.fscore.app.repository.HeadToHeadRepository;
import com.fscore.app.service.HeadToHeadService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class HeadToHeadServiceImpl implements HeadToHeadService {

    private final HeadToHeadRepository repository;

    public HeadToHeadServiceImpl(HeadToHeadRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<HeadToHead> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<HeadToHead> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public HeadToHead save(HeadToHead entity) {
        return repository.save(entity);
    }

    @Override
    public HeadToHead update(String id, HeadToHead entity) {
        HeadToHead existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("HeadToHead not found with id: " + id));
        entity.setId(existing.getId());
        return repository.save(entity);
    }

    @Override
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("HeadToHead not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
