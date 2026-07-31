package com.fscore.app.service;

import com.fscore.app.entity.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface CountryService {
    Page<Country> findAll(Pageable pageable);
    Optional<Country> findById(String id);
    Country save(Country entity);
    Country update(String id, Country entity);
    void delete(String id);
}
