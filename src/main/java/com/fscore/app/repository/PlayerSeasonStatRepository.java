package com.fscore.app.repository;

import com.fscore.app.entity.PlayerSeasonStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerSeasonStatRepository extends JpaRepository<PlayerSeasonStat, String>, JpaSpecificationExecutor<PlayerSeasonStat> {
    java.util.List<PlayerSeasonStat> findBySeasonId(String seasonId);
    java.util.List<PlayerSeasonStat> findByTeamIdAndSeasonId(String teamId, String seasonId);
}
