package com.fscore.app.repository;

import com.fscore.app.entity.TeamCoach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamCoachRepository extends JpaRepository<TeamCoach, String>, JpaSpecificationExecutor<TeamCoach> {
}
