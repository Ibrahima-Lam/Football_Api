package com.fscore.app.service.impl;

import com.fscore.app.dto.response.RefereeResponse;
import com.fscore.app.entity.Referee;
import com.fscore.app.exception.ResourceNotFoundException;
import com.fscore.app.repository.RefereeRepository;
import com.fscore.app.service.RefereeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class RefereeServiceImpl implements RefereeService {

    private final RefereeRepository repository;

    public RefereeServiceImpl(RefereeRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Referee> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<Referee> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Referee save(Referee entity) {
        return repository.save(entity);
    }

    @Override
    public Referee update(String id, Referee entity) {
        Referee existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Referee not found with id: " + id));
        entity.setId(existing.getId());
        return repository.save(entity);
    }

    @Override
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Referee not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
