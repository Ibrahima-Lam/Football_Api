package com.fscore.app.service.impl;

import com.fscore.app.dto.response.OddResponse;
import com.fscore.app.entity.Odd;
import com.fscore.app.exception.ResourceNotFoundException;
import com.fscore.app.repository.OddRepository;
import com.fscore.app.service.OddService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class OddServiceImpl implements OddService {

    private final OddRepository repository;

    public OddServiceImpl(OddRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Odd> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<Odd> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Odd save(Odd entity) {
        return repository.save(entity);
    }

    @Override
    public Odd update(String id, Odd entity) {
        Odd existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Odd not found with id: " + id));
        entity.setId(existing.getId());
        return repository.save(entity);
    }

    @Override
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Odd not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
