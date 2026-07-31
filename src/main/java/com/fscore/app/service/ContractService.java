package com.fscore.app.service;

import com.fscore.app.entity.Contract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface ContractService {
    Page<Contract> findAll(Pageable pageable);
    Optional<Contract> findById(String id);
    Contract save(Contract entity);
    Contract update(String id, Contract entity);
    void delete(String id);
}
