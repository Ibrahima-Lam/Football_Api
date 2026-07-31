package com.fscore.app.service.impl;

import com.fscore.app.dto.response.AuditLogResponse;
import com.fscore.app.entity.AuditLog;
import com.fscore.app.exception.ResourceNotFoundException;
import com.fscore.app.repository.AuditLogRepository;
import com.fscore.app.service.AuditLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository repository;

    public AuditLogServiceImpl(AuditLogRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<AuditLog> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<AuditLog> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public AuditLog save(AuditLog entity) {
        return repository.save(entity);
    }

    @Override
    public AuditLog update(String id, AuditLog entity) {
        AuditLog existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("AuditLog not found with id: " + id));
        entity.setId(existing.getId());
        return repository.save(entity);
    }

    @Override
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("AuditLog not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
