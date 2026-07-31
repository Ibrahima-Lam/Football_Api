package com.fscore.app.repository;

import com.fscore.app.entity.MatchPenaltyShootoutShot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchPenaltyShootoutShotRepository extends JpaRepository<MatchPenaltyShootoutShot, String>, JpaSpecificationExecutor<MatchPenaltyShootoutShot> {
}
