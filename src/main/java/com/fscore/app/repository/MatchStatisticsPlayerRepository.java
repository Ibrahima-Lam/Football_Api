package com.fscore.app.repository;

import com.fscore.app.entity.MatchStatisticsPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchStatisticsPlayerRepository extends JpaRepository<MatchStatisticsPlayer, String>, JpaSpecificationExecutor<MatchStatisticsPlayer> {

    java.util.List<MatchStatisticsPlayer> findByMatchIdOrderByGoalsDescAssistsDesc(String matchId);
}
