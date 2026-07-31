package com.fscore.app.repository;

import com.fscore.app.entity.TeamTrophy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamTrophyRepository extends JpaRepository<TeamTrophy, String>, JpaSpecificationExecutor<TeamTrophy> {
}
