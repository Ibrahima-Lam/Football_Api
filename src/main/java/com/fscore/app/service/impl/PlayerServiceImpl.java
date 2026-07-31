package com.fscore.app.service.impl;

import com.fscore.app.dto.response.PlayerResponse;
import com.fscore.app.entity.Player;
import com.fscore.app.exception.ResourceNotFoundException;
import com.fscore.app.repository.PlayerRepository;
import com.fscore.app.service.PlayerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository repository;

    public PlayerServiceImpl(PlayerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Player> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<Player> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Player save(Player entity) {
        return repository.save(entity);
    }

    @Override
    public Player update(String id, Player entity) {
        Player existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Player not found with id: " + id));
        entity.setId(existing.getId());
        return repository.save(entity);
    }

    @Override
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Player not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
