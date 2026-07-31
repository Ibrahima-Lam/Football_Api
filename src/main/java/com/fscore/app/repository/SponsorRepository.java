package com.fscore.app.repository;

import com.fscore.app.entity.Sponsor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SponsorRepository extends JpaRepository<Sponsor, String>, JpaSpecificationExecutor<Sponsor> {
}
