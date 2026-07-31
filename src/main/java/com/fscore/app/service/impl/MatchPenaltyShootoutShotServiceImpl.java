package com.fscore.app.service.impl;

import com.fscore.app.dto.response.MatchPenaltyShootoutShotResponse;
import com.fscore.app.entity.MatchPenaltyShootoutShot;
import com.fscore.app.exception.ResourceNotFoundException;
import com.fscore.app.repository.MatchPenaltyShootoutShotRepository;
import com.fscore.app.service.MatchPenaltyShootoutShotService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class MatchPenaltyShootoutShotServiceImpl implements MatchPenaltyShootoutShotService {

    private final MatchPenaltyShootoutShotRepository repository;

    public MatchPenaltyShootoutShotServiceImpl(MatchPenaltyShootoutShotRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<MatchPenaltyShootoutShot> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<MatchPenaltyShootoutShot> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public MatchPenaltyShootoutShot save(MatchPenaltyShootoutShot entity) {
        return repository.save(entity);
    }

    @Override
    public MatchPenaltyShootoutShot update(String id, MatchPenaltyShootoutShot entity) {
        MatchPenaltyShootoutShot existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("MatchPenaltyShootoutShot not found with id: " + id));
        entity.setId(existing.getId());
        return repository.save(entity);
    }

    @Override
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("MatchPenaltyShootoutShot not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
