package com.fscore.app.service;

import com.fscore.app.entity.SponsorLink;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface SponsorLinkService {
    Page<SponsorLink> findAll(Pageable pageable);
    Optional<SponsorLink> findById(String id);
    SponsorLink save(SponsorLink entity);
    SponsorLink update(String id, SponsorLink entity);
    void delete(String id);
}
