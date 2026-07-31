package com.fscore.app.service;

import com.fscore.app.entity.PlayerSeasonStat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface PlayerSeasonStatService {
    Page<PlayerSeasonStat> findAll(Pageable pageable);
    Optional<PlayerSeasonStat> findById(String id);
    PlayerSeasonStat save(PlayerSeasonStat entity);
    PlayerSeasonStat update(String id, PlayerSeasonStat entity);
    void delete(String id);
}
