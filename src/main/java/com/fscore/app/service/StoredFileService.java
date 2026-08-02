package com.fscore.app.service;

import com.fscore.app.entity.StoredFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface StoredFileService {
    Page<StoredFile> findAll(Pageable pageable);
    Optional<StoredFile> findById(String id);
    StoredFile save(StoredFile entity);
    void delete(String id);
}
