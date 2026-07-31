package com.fscore.app.repository;

import com.fscore.app.entity.PlayerAward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerAwardRepository extends JpaRepository<PlayerAward, String>, JpaSpecificationExecutor<PlayerAward> {
}
