package com.fscore.app.service.impl;

import com.fscore.app.dto.response.MatchRefereeResponse;
import com.fscore.app.entity.MatchReferee;
import com.fscore.app.exception.ResourceNotFoundException;
import com.fscore.app.repository.MatchRefereeRepository;
import com.fscore.app.service.MatchRefereeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class MatchRefereeServiceImpl implements MatchRefereeService {

    private final MatchRefereeRepository repository;

    public MatchRefereeServiceImpl(MatchRefereeRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<MatchReferee> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<MatchReferee> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public MatchReferee save(MatchReferee entity) {
        return repository.save(entity);
    }

    @Override
    public MatchReferee update(String id, MatchReferee entity) {
        MatchReferee existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("MatchReferee not found with id: " + id));
        entity.setId(existing.getId());
        return repository.save(entity);
    }

    @Override
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("MatchReferee not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
