package com.fscore.app.service;

import com.fscore.app.dto.request.DeviceTokenRequest;
import com.fscore.app.entity.ApiUser;
import com.fscore.app.entity.DeviceToken;

import java.util.List;

public interface DeviceTokenService {
    DeviceToken register(ApiUser user, DeviceTokenRequest request);
    void unregister(String userId, String token);
    List<DeviceToken> findByUser(String userId);
}
