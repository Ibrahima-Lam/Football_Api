package com.fscore.app.repository;

import com.fscore.app.entity.ApiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ApiUserRepository extends JpaRepository<ApiUser, String>, JpaSpecificationExecutor<ApiUser> {
    Optional<ApiUser> findByEmail(String email);
}
