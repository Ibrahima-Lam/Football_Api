package com.fscore.app.service.impl;

import com.fscore.app.dto.response.NewsResponse;
import com.fscore.app.entity.News;
import com.fscore.app.exception.ResourceNotFoundException;
import com.fscore.app.repository.NewsRepository;
import com.fscore.app.service.NewsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class NewsServiceImpl implements NewsService {

    private final NewsRepository repository;

    public NewsServiceImpl(NewsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<News> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<News> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public News save(News entity) {
        return repository.save(entity);
    }

    @Override
    public News update(String id, News entity) {
        News existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("News not found with id: " + id));
        entity.setId(existing.getId());
        return repository.save(entity);
    }

    @Override
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("News not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
