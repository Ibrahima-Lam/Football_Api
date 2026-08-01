package com.fscore.app.service.impl;

import com.fscore.app.dto.request.DeviceTokenRequest;
import com.fscore.app.entity.ApiUser;
import com.fscore.app.entity.DeviceToken;
import com.fscore.app.repository.DeviceTokenRepository;
import com.fscore.app.service.DeviceTokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DeviceTokenServiceImpl implements DeviceTokenService {

    private final DeviceTokenRepository repository;

    public DeviceTokenServiceImpl(DeviceTokenRepository repository) {
        this.repository = repository;
    }

    @Override
    public DeviceToken register(ApiUser user, DeviceTokenRequest request) {
        return repository.findByToken(request.getToken())
            .map(existing -> {
                existing.setUser(user);
                existing.setPlatform(request.getPlatform());
                return repository.save(existing);
            })
            .orElseGet(() -> {
                DeviceToken token = DeviceToken.builder()
                    .user(user)
                    .token(request.getToken())
                    .platform(request.getPlatform())
                    .build();
                return repository.save(token);
            });
    }

    @Override
    public void unregister(String userId, String token) {
        repository.findByUserIdAndToken(userId, token).ifPresent(repository::delete);
    }

    @Override
    public List<DeviceToken> findByUser(String userId) {
        return repository.findByUserId(userId);
    }
}
