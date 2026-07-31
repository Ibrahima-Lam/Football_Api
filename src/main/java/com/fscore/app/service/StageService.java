package com.fscore.app.service;

import com.fscore.app.entity.Stage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface StageService {
    Page<Stage> findAll(Pageable pageable);
    Optional<Stage> findById(String id);
    Stage save(Stage entity);
    Stage update(String id, Stage entity);
    void delete(String id);
}
