package com.fscore.app.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.storage")
public class StorageProperties {

    private String location = "./data/uploads";

    private long maxImageSize = 10 * 1024 * 1024;

    private long maxVideoSize = 200 * 1024 * 1024;
}
