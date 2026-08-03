package com.fscore.app.repository;

import com.fscore.app.entity.Injury;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface InjuryRepository extends JpaRepository<Injury, String>, JpaSpecificationExecutor<Injury> {
    java.util.List<Injury> findByTeamIdOrderByStartDateDesc(String teamId);
}
