package com.fscore.app.service.impl;

import com.fscore.app.dto.response.SponsorLinkResponse;
import com.fscore.app.entity.SponsorLink;
import com.fscore.app.exception.ResourceNotFoundException;
import com.fscore.app.repository.SponsorLinkRepository;
import com.fscore.app.service.SponsorLinkService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class SponsorLinkServiceImpl implements SponsorLinkService {

    private final SponsorLinkRepository repository;

    public SponsorLinkServiceImpl(SponsorLinkRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<SponsorLink> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<SponsorLink> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public SponsorLink save(SponsorLink entity) {
        return repository.save(entity);
    }

    @Override
    public SponsorLink update(String id, SponsorLink entity) {
        SponsorLink existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("SponsorLink not found with id: " + id));
        entity.setId(existing.getId());
        return repository.save(entity);
    }

    @Override
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("SponsorLink not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
