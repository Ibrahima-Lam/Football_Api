package com.fscore.app.repository;

import com.fscore.app.entity.Trophy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TrophyRepository extends JpaRepository<Trophy, String>, JpaSpecificationExecutor<Trophy> {
}
