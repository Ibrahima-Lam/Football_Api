package com.fscore.app.service.impl;

import com.fscore.app.dto.response.TransferResponse;
import com.fscore.app.entity.Transfer;
import com.fscore.app.exception.ResourceNotFoundException;
import com.fscore.app.repository.TransferRepository;
import com.fscore.app.service.TransferService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class TransferServiceImpl implements TransferService {

    private final TransferRepository repository;

    public TransferServiceImpl(TransferRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Transfer> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<Transfer> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Transfer save(Transfer entity) {
        return repository.save(entity);
    }

    @Override
    public Transfer update(String id, Transfer entity) {
        Transfer existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Transfer not found with id: " + id));
        entity.setId(existing.getId());
        return repository.save(entity);
    }

    @Override
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Transfer not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
