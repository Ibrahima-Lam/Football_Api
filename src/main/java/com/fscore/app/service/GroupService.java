package com.fscore.app.service;

import com.fscore.app.entity.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface GroupService {
    Page<Group> findAll(Pageable pageable);
    Optional<Group> findById(String id);
    Group save(Group entity);
    Group update(String id, Group entity);
    void delete(String id);
}
