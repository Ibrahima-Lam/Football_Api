package com.fscore.app.repository;

import com.fscore.app.entity.Standing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface StandingRepository extends JpaRepository<Standing, String>, JpaSpecificationExecutor<Standing> {

    java.util.List<Standing> findBySeasonIdOrderByRankPositionAsc(String seasonId);

    java.util.List<Standing> findBySeasonIdAndStageIdOrderByRankPositionAsc(String seasonId, String stageId);

    void deleteByGroupId(String groupId);
}
