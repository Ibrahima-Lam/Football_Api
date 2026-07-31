package com.fscore.app.service.impl;

import com.fscore.app.dto.response.CityResponse;
import com.fscore.app.entity.City;
import com.fscore.app.exception.ResourceNotFoundException;
import com.fscore.app.repository.CityRepository;
import com.fscore.app.service.CityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class CityServiceImpl implements CityService {

    private final CityRepository repository;

    public CityServiceImpl(CityRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<City> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<City> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public City save(City entity) {
        return repository.save(entity);
    }

    @Override
    public City update(String id, City entity) {
        City existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + id));
        entity.setId(existing.getId());
        return repository.save(entity);
    }

    @Override
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("City not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
