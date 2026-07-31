package com.fscore.app.service;

import com.fscore.app.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface NewsService {
    Page<News> findAll(Pageable pageable);
    Optional<News> findById(String id);
    News save(News entity);
    News update(String id, News entity);
    void delete(String id);
}
