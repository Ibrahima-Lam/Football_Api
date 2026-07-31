package com.fscore.app.service.impl;

import com.fscore.app.dto.response.BookmakerResponse;
import com.fscore.app.entity.Bookmaker;
import com.fscore.app.exception.ResourceNotFoundException;
import com.fscore.app.repository.BookmakerRepository;
import com.fscore.app.service.BookmakerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class BookmakerServiceImpl implements BookmakerService {

    private final BookmakerRepository repository;

    public BookmakerServiceImpl(BookmakerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Bookmaker> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<Bookmaker> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Bookmaker save(Bookmaker entity) {
        return repository.save(entity);
    }

    @Override
    public Bookmaker update(String id, Bookmaker entity) {
        Bookmaker existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Bookmaker not found with id: " + id));
        entity.setId(existing.getId());
        return repository.save(entity);
    }

    @Override
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Bookmaker not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
