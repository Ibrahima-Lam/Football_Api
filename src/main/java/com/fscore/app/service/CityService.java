package com.fscore.app.service;

import com.fscore.app.entity.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface CityService {
    Page<City> findAll(Pageable pageable);
    Optional<City> findById(String id);
    City save(City entity);
    City update(String id, City entity);
    void delete(String id);
}
