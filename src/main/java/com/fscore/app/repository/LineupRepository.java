package com.fscore.app.repository;

import com.fscore.app.entity.Lineup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LineupRepository extends JpaRepository<Lineup, String>, JpaSpecificationExecutor<Lineup> {

    java.util.List<Lineup> findByMatchIdOrderByStarterDescShirtNumberAsc(String matchId);
}
