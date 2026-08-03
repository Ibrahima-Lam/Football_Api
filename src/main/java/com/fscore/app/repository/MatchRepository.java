package com.fscore.app.repository;

import com.fscore.app.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, String>, JpaSpecificationExecutor<Match> {

    List<Match> findByGroupId(String groupId);
}
