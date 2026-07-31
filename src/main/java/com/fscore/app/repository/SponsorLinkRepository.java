package com.fscore.app.repository;

import com.fscore.app.entity.SponsorLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SponsorLinkRepository extends JpaRepository<SponsorLink, String>, JpaSpecificationExecutor<SponsorLink> {
}
