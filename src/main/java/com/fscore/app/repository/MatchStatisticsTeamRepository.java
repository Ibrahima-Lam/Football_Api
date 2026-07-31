package com.fscore.app.repository;

import com.fscore.app.entity.MatchStatisticsTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchStatisticsTeamRepository extends JpaRepository<MatchStatisticsTeam, String>, JpaSpecificationExecutor<MatchStatisticsTeam> {
}
