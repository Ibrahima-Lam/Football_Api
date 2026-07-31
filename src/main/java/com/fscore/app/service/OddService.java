package com.fscore.app.service;

import com.fscore.app.entity.Odd;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface OddService {
    Page<Odd> findAll(Pageable pageable);
    Optional<Odd> findById(String id);
    Odd save(Odd entity);
    Odd update(String id, Odd entity);
    void delete(String id);
}
