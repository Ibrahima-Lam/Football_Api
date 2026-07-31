package com.fscore.app.service.impl;

import com.fscore.app.dto.response.StadiumResponse;
import com.fscore.app.entity.Stadium;
import com.fscore.app.exception.ResourceNotFoundException;
import com.fscore.app.repository.StadiumRepository;
import com.fscore.app.service.StadiumService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class StadiumServiceImpl implements StadiumService {

    private final StadiumRepository repository;

    public StadiumServiceImpl(StadiumRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Stadium> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<Stadium> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Stadium save(Stadium entity) {
        return repository.save(entity);
    }

    @Override
    public Stadium update(String id, Stadium entity) {
        Stadium existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Stadium not found with id: " + id));
        entity.setId(existing.getId());
        return repository.save(entity);
    }

    @Override
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Stadium not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
