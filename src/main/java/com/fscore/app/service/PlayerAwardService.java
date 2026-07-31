package com.fscore.app.service;

import com.fscore.app.entity.PlayerAward;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface PlayerAwardService {
    Page<PlayerAward> findAll(Pageable pageable);
    Optional<PlayerAward> findById(String id);
    PlayerAward save(PlayerAward entity);
    PlayerAward update(String id, PlayerAward entity);
    void delete(String id);
}
