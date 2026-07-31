package com.fscore.app.service;

import com.fscore.app.entity.PlayerSeasonRegistration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface PlayerSeasonRegistrationService {
    Page<PlayerSeasonRegistration> findAll(Pageable pageable);
    Optional<PlayerSeasonRegistration> findById(String id);
    PlayerSeasonRegistration save(PlayerSeasonRegistration entity);
    PlayerSeasonRegistration update(String id, PlayerSeasonRegistration entity);
    void delete(String id);
}
