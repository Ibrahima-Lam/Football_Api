package com.fscore.app.repository;

import com.fscore.app.entity.MatchEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchEventRepository extends JpaRepository<MatchEvent, String>, JpaSpecificationExecutor<MatchEvent> {

    java.util.List<MatchEvent> findByMatchIdOrderByMinuteAscExtraMinuteAsc(String matchId);
}
