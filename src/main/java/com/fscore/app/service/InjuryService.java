package com.fscore.app.service;

import com.fscore.app.entity.Injury;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface InjuryService {
    Page<Injury> findAll(Pageable pageable);
    Optional<Injury> findById(String id);
    Injury save(Injury entity);
    Injury update(String id, Injury entity);
    void delete(String id);
}
