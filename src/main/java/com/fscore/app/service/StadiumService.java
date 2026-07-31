package com.fscore.app.service;

import com.fscore.app.entity.Stadium;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface StadiumService {
    Page<Stadium> findAll(Pageable pageable);
    Optional<Stadium> findById(String id);
    Stadium save(Stadium entity);
    Stadium update(String id, Stadium entity);
    void delete(String id);
}
