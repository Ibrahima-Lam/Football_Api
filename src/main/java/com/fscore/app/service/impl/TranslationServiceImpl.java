package com.fscore.app.service.impl;

import com.fscore.app.dto.response.TranslationResponse;
import com.fscore.app.entity.Translation;
import com.fscore.app.exception.ResourceNotFoundException;
import com.fscore.app.repository.TranslationRepository;
import com.fscore.app.service.TranslationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class TranslationServiceImpl implements TranslationService {

    private final TranslationRepository repository;

    public TranslationServiceImpl(TranslationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Translation> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<Translation> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Translation save(Translation entity) {
        return repository.save(entity);
    }

    @Override
    public Translation update(String id, Translation entity) {
        Translation existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Translation not found with id: " + id));
        entity.setId(existing.getId());
        return repository.save(entity);
    }

    @Override
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Translation not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
