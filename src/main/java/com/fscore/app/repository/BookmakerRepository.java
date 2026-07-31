package com.fscore.app.repository;

import com.fscore.app.entity.Bookmaker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmakerRepository extends JpaRepository<Bookmaker, String>, JpaSpecificationExecutor<Bookmaker> {
}
