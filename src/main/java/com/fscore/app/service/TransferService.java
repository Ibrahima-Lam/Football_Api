package com.fscore.app.service;

import com.fscore.app.entity.Transfer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface TransferService {
    Page<Transfer> findAll(Pageable pageable);
    Optional<Transfer> findById(String id);
    Transfer save(Transfer entity);
    Transfer update(String id, Transfer entity);
    void delete(String id);
}
