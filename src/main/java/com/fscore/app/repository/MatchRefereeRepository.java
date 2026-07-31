package com.fscore.app.repository;

import com.fscore.app.entity.MatchReferee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRefereeRepository extends JpaRepository<MatchReferee, String>, JpaSpecificationExecutor<MatchReferee> {
}
