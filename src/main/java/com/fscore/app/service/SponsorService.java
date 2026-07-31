package com.fscore.app.service;

import com.fscore.app.entity.Sponsor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface SponsorService {
    Page<Sponsor> findAll(Pageable pageable);
    Optional<Sponsor> findById(String id);
    Sponsor save(Sponsor entity);
    Sponsor update(String id, Sponsor entity);
    void delete(String id);
}
