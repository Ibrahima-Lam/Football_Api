package com.fscore.app.service.impl;

import com.fscore.app.dto.response.RoundResponse;
import com.fscore.app.entity.Round;
import com.fscore.app.exception.ResourceNotFoundException;
import com.fscore.app.repository.RoundRepository;
import com.fscore.app.service.RoundService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class RoundServiceImpl implements RoundService {

    private final RoundRepository repository;

    public RoundServiceImpl(RoundRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Round> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<Round> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Round save(Round entity) {
        return repository.save(entity);
    }

    @Override
    public Round update(String id, Round entity) {
        Round existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Round not found with id: " + id));
        entity.setId(existing.getId());
        return repository.save(entity);
    }

    @Override
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Round not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
