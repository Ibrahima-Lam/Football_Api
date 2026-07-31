package com.fscore.app.service;

import com.fscore.app.entity.Referee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface RefereeService {
    Page<Referee> findAll(Pageable pageable);
    Optional<Referee> findById(String id);
    Referee save(Referee entity);
    Referee update(String id, Referee entity);
    void delete(String id);
}
