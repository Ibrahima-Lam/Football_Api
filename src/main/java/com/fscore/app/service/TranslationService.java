package com.fscore.app.service;

import com.fscore.app.entity.Translation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface TranslationService {
    Page<Translation> findAll(Pageable pageable);
    Optional<Translation> findById(String id);
    Translation save(Translation entity);
    Translation update(String id, Translation entity);
    void delete(String id);
}
