package com.fscore.app.repository;

import com.fscore.app.entity.MatchFormation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchFormationRepository extends JpaRepository<MatchFormation, String>, JpaSpecificationExecutor<MatchFormation> {
}
