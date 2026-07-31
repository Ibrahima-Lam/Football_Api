package com.fscore.app.service;

import com.fscore.app.entity.Media;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface MediaService {
    Page<Media> findAll(Pageable pageable);
    Optional<Media> findById(String id);
    Media save(Media entity);
    Media update(String id, Media entity);
    void delete(String id);
}
