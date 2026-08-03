package com.fscore.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;

@Configuration
public class SpaConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/admin/**", "/admin")
            .addResourceLocations("classpath:/static/admin/")
            .resourceChain(false)
            .addResolver(spaResolver());
        registry.addResourceHandler("/**")
            .addResourceLocations("classpath:/static/")
            .resourceChain(false)
            .addResolver(spaResolver());
    }

    private PathResourceResolver spaResolver() {
        return new PathResourceResolver() {
            @Override
            protected Resource getResource(String resourcePath, Resource location) throws IOException {
                Resource requested = location.createRelative(resourcePath);
                if (requested.exists() && requested.isReadable() && !isDirectory(requested)) {
                    return requested;
                }
                return location.createRelative("index.html");
            }

            private boolean isDirectory(Resource resource) {
                try {
                    return resource.getFile().isDirectory();
                } catch (IOException e) {
                    return false;
                }
            }
        };
    }
}
