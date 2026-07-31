package com.fscore.app.service.impl;

import com.fscore.app.dto.response.GroupResponse;
import com.fscore.app.entity.Group;
import com.fscore.app.exception.ResourceNotFoundException;
import com.fscore.app.repository.GroupRepository;
import com.fscore.app.service.GroupService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class GroupServiceImpl implements GroupService {

    private final GroupRepository repository;

    public GroupServiceImpl(GroupRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Group> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<Group> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Group save(Group entity) {
        return repository.save(entity);
    }

    @Override
    public Group update(String id, Group entity) {
        Group existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Group not found with id: " + id));
        entity.setId(existing.getId());
        return repository.save(entity);
    }

    @Override
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Group not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
