package com.fscore.app.service.impl;

import com.fscore.app.dto.response.StageResponse;
import com.fscore.app.entity.Stage;
import com.fscore.app.exception.ResourceNotFoundException;
import com.fscore.app.repository.StageRepository;
import com.fscore.app.service.StageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class StageServiceImpl implements StageService {

    private final StageRepository repository;

    public StageServiceImpl(StageRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Stage> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<Stage> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Stage save(Stage entity) {
        return repository.save(entity);
    }

    @Override
    public Stage update(String id, Stage entity) {
        Stage existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Stage not found with id: " + id));
        entity.setId(existing.getId());
        return repository.save(entity);
    }

    @Override
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Stage not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
