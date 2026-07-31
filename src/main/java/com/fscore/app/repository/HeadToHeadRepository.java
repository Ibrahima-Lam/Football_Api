package com.fscore.app.repository;

import com.fscore.app.entity.HeadToHead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface HeadToHeadRepository extends JpaRepository<HeadToHead, String>, JpaSpecificationExecutor<HeadToHead> {
}
