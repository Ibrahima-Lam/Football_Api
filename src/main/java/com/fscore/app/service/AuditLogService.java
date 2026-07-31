package com.fscore.app.service;

import com.fscore.app.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface AuditLogService {
    Page<AuditLog> findAll(Pageable pageable);
    Optional<AuditLog> findById(String id);
    AuditLog save(AuditLog entity);
    AuditLog update(String id, AuditLog entity);
    void delete(String id);
}
