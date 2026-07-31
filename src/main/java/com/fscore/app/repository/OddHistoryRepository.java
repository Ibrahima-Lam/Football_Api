package com.fscore.app.repository;

import com.fscore.app.entity.OddHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OddHistoryRepository extends JpaRepository<OddHistory, String>, JpaSpecificationExecutor<OddHistory> {
}
