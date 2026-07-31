package com.fscore.app.service.impl;

import com.fscore.app.dto.response.ApiUserResponse;
import com.fscore.app.entity.ApiUser;
import com.fscore.app.exception.ResourceNotFoundException;
import com.fscore.app.repository.ApiUserRepository;
import com.fscore.app.service.ApiUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class ApiUserServiceImpl implements ApiUserService {

    private final ApiUserRepository repository;

    public ApiUserServiceImpl(ApiUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<ApiUser> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<ApiUser> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public ApiUser save(ApiUser entity) {
        return repository.save(entity);
    }

    @Override
    public ApiUser update(String id, ApiUser entity) {
        ApiUser existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("ApiUser not found with id: " + id));
        entity.setId(existing.getId());
        return repository.save(entity);
    }

    @Override
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("ApiUser not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
