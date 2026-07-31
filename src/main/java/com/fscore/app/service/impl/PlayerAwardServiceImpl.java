package com.fscore.app.service.impl;

import com.fscore.app.dto.response.PlayerAwardResponse;
import com.fscore.app.entity.PlayerAward;
import com.fscore.app.exception.ResourceNotFoundException;
import com.fscore.app.repository.PlayerAwardRepository;
import com.fscore.app.service.PlayerAwardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class PlayerAwardServiceImpl implements PlayerAwardService {

    private final PlayerAwardRepository repository;

    public PlayerAwardServiceImpl(PlayerAwardRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<PlayerAward> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<PlayerAward> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public PlayerAward save(PlayerAward entity) {
        return repository.save(entity);
    }

    @Override
    public PlayerAward update(String id, PlayerAward entity) {
        PlayerAward existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("PlayerAward not found with id: " + id));
        entity.setId(existing.getId());
        return repository.save(entity);
    }

    @Override
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("PlayerAward not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
