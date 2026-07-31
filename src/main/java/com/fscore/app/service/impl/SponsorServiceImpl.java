package com.fscore.app.service.impl;

import com.fscore.app.dto.response.SponsorResponse;
import com.fscore.app.entity.Sponsor;
import com.fscore.app.exception.ResourceNotFoundException;
import com.fscore.app.repository.SponsorRepository;
import com.fscore.app.service.SponsorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class SponsorServiceImpl implements SponsorService {

    private final SponsorRepository repository;

    public SponsorServiceImpl(SponsorRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Sponsor> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<Sponsor> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Sponsor save(Sponsor entity) {
        return repository.save(entity);
    }

    @Override
    public Sponsor update(String id, Sponsor entity) {
        Sponsor existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Sponsor not found with id: " + id));
        entity.setId(existing.getId());
        return repository.save(entity);
    }

    @Override
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Sponsor not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
