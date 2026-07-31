package com.fscore.app.service;

import com.fscore.app.entity.Bookmaker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface BookmakerService {
    Page<Bookmaker> findAll(Pageable pageable);
    Optional<Bookmaker> findById(String id);
    Bookmaker save(Bookmaker entity);
    Bookmaker update(String id, Bookmaker entity);
    void delete(String id);
}
