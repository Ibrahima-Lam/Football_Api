package com.fscore.app.service;

import com.fscore.app.entity.HeadToHead;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface HeadToHeadService {
    Page<HeadToHead> findAll(Pageable pageable);
    Optional<HeadToHead> findById(String id);
    HeadToHead save(HeadToHead entity);
    HeadToHead update(String id, HeadToHead entity);
    void delete(String id);
}
