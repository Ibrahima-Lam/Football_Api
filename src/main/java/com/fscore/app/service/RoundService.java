package com.fscore.app.service;

import com.fscore.app.entity.Round;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface RoundService {
    Page<Round> findAll(Pageable pageable);
    Optional<Round> findById(String id);
    Round save(Round entity);
    Round update(String id, Round entity);
    void delete(String id);
}
