package com.fscore.app.service;

import com.fscore.app.entity.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface PlayerService {
    Page<Player> findAll(Pageable pageable);
    Optional<Player> findById(String id);
    Player save(Player entity);
    Player update(String id, Player entity);
    void delete(String id);
}
