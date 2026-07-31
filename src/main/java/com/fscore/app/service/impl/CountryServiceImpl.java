package com.fscore.app.service.impl;

import com.fscore.app.dto.response.CountryResponse;
import com.fscore.app.entity.Country;
import com.fscore.app.exception.ResourceNotFoundException;
import com.fscore.app.repository.CountryRepository;
import com.fscore.app.service.CountryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class CountryServiceImpl implements CountryService {

    private final CountryRepository repository;

    public CountryServiceImpl(CountryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Country> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<Country> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Country save(Country entity) {
        return repository.save(entity);
    }

    @Override
    public Country update(String id, Country entity) {
        Country existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Country not found with id: " + id));
        entity.setId(existing.getId());
        return repository.save(entity);
    }

    @Override
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Country not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
