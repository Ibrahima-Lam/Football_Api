package com.fscore.app.service.impl;

import com.fscore.app.dto.response.ContinentResponse;
import com.fscore.app.entity.Continent;
import com.fscore.app.exception.ResourceNotFoundException;
import com.fscore.app.repository.ContinentRepository;
import com.fscore.app.service.ContinentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class ContinentServiceImpl implements ContinentService {

    private final ContinentRepository repository;

    public ContinentServiceImpl(ContinentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Continent> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<Continent> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Continent save(Continent entity) {
        return repository.save(entity);
    }

    @Override
    public Continent update(String id, Continent entity) {
        Continent existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Continent not found with id: " + id));
        entity.setId(existing.getId());
        return repository.save(entity);
    }

    @Override
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Continent not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
