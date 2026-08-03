package com.fscore.app.repository;

import com.fscore.app.entity.TeamSeasonParticipation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamSeasonParticipationRepository extends JpaRepository<TeamSeasonParticipation, String>, JpaSpecificationExecutor<TeamSeasonParticipation> {
    java.util.List<TeamSeasonParticipation> findBySeasonId(String seasonId);
    java.util.List<TeamSeasonParticipation> findByTeamId(String teamId);
}
