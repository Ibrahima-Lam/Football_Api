package com.fscore.app.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DeviceTokenRequest {
    @NotBlank(message = "Token is required")
    @Size(max = 500, message = "Token must not exceed 500 characters")
    private String token;

    @Pattern(regexp = "ANDROID|IOS|WEB", message = "Platform must be ANDROID, IOS or WEB")
    private String platform;
}
