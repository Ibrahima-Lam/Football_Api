package com.fscore.app.service.impl;

import com.fscore.app.entity.StoredFile;
import com.fscore.app.exception.ResourceNotFoundException;
import com.fscore.app.repository.StoredFileRepository;
import com.fscore.app.service.FileStorageService;
import com.fscore.app.service.StoredFileService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class StoredFileServiceImpl implements StoredFileService {

    private final StoredFileRepository repository;
    private final FileStorageService fileStorageService;

    public StoredFileServiceImpl(StoredFileRepository repository, FileStorageService fileStorageService) {
        this.repository = repository;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public Page<StoredFile> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<StoredFile> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public StoredFile save(StoredFile entity) {
        return repository.save(entity);
    }

    @Override
    public void delete(String id) {
        StoredFile entity = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Fichier introuvable avec l'id: " + id));
        fileStorageService.delete(entity.getUrlPath().substring("/uploads/".length()));
        repository.delete(entity);
    }
}
