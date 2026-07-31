package com.fscore.app.repository;

import com.fscore.app.entity.Odd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OddRepository extends JpaRepository<Odd, String>, JpaSpecificationExecutor<Odd> {
}
