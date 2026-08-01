package com.fscore.app.repository;

import com.fscore.app.entity.DeviceToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceTokenRepository extends JpaRepository<DeviceToken, String>, JpaSpecificationExecutor<DeviceToken> {

    Optional<DeviceToken> findByToken(String token);

    Optional<DeviceToken> findByUserIdAndToken(String userId, String token);

    List<DeviceToken> findByUserId(String userId);
}
