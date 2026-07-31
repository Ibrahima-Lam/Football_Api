package com.fscore.app.repository;

import com.fscore.app.entity.Confederation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfederationRepository extends JpaRepository<Confederation, String>, JpaSpecificationExecutor<Confederation> {
}
