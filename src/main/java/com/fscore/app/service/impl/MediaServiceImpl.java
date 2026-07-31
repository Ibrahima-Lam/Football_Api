package com.fscore.app.service.impl;

import com.fscore.app.dto.response.MediaResponse;
import com.fscore.app.entity.Media;
import com.fscore.app.exception.ResourceNotFoundException;
import com.fscore.app.repository.MediaRepository;
import com.fscore.app.service.MediaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class MediaServiceImpl implements MediaService {

    private final MediaRepository repository;

    public MediaServiceImpl(MediaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Media> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<Media> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Media save(Media entity) {
        return repository.save(entity);
    }

    @Override
    public Media update(String id, Media entity) {
        Media existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Media not found with id: " + id));
        entity.setId(existing.getId());
        return repository.save(entity);
    }

    @Override
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Media not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
