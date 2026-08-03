package com.fscore.app.repository;

import com.fscore.app.entity.Suspension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SuspensionRepository extends JpaRepository<Suspension, String>, JpaSpecificationExecutor<Suspension> {
    java.util.List<Suspension> findByTeamIdOrderByStartDateDesc(String teamId);
}
