package com.fscore.app.service.impl;

import com.fscore.app.dto.response.ContractResponse;
import com.fscore.app.entity.Contract;
import com.fscore.app.exception.ResourceNotFoundException;
import com.fscore.app.repository.ContractRepository;
import com.fscore.app.service.ContractService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class ContractServiceImpl implements ContractService {

    private final ContractRepository repository;

    public ContractServiceImpl(ContractRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Contract> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<Contract> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Contract save(Contract entity) {
        return repository.save(entity);
    }

    @Override
    public Contract update(String id, Contract entity) {
        Contract existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Contract not found with id: " + id));
        entity.setId(existing.getId());
        return repository.save(entity);
    }

    @Override
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Contract not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
