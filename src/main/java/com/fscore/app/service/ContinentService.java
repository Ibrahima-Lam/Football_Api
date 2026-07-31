package com.fscore.app.service;

import com.fscore.app.entity.Continent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface ContinentService {
    Page<Continent> findAll(Pageable pageable);
    Optional<Continent> findById(String id);
    Continent save(Continent entity);
    Continent update(String id, Continent entity);
    void delete(String id);
}
